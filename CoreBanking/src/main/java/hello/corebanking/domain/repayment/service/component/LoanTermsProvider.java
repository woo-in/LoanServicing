package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.loan.repository.LoanContractMapper;
import hello.corebanking.domain.product.dto.LoanTerms;
import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.LoanProduct;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.product.repository.LoanProductMapper;
import hello.corebanking.domain.repayment.entity.RepaymentSchedule;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanTermsProvider {

    private final LoanContractMapper loanContractMapper;
    private final LoanProductMapper loanProductMapper;

    public LoanTerms getLoanTerms(LoanContract contract) {
        return resolveLoanTerms(contract);
    }

    public LoanTerms getLoanTerms(LoanAccount loanAccount) {
        LoanContract contract = loanContractMapper.findById(loanAccount.getLoanContractId())
                .orElseThrow(() -> new NotFoundException(
                        "계약을 찾을 수 없습니다. contractId=" + loanAccount.getLoanContractId()));
        return resolveLoanTerms(contract);
    }

    public LoanTerms getLoanTerms(RepaymentSchedule schedule) {
        LoanContract contract = loanContractMapper.findByLoanAccountId(schedule.getLoanAccountId())
                .orElseThrow(() -> new NotFoundException(
                        "계약을 찾을 수 없습니다. loanAccountId=" + schedule.getLoanAccountId()));
        return resolveLoanTerms(contract);
    }

    private LoanTerms resolveLoanTerms(LoanContract contract) {
        LoanProduct product = loanProductMapper.findById(contract.getLoanProductId())
                .orElseThrow(() -> new NotFoundException(
                        "대출상품을 찾을 수 없습니다. loanProductId=" + contract.getLoanProductId()));

        InterestType interestType = product.getInterestType();
        if (interestType == null) {
            throw new NotFoundException("금리종류를 찾을 수 없습니다. loanProductId=" + product.getLoanProductId());
        }

        RepaymentMethod repaymentMethod = product.getRepaymentMethod();
        if (repaymentMethod == null) {
            throw new NotFoundException("상환방법을 찾을 수 없습니다. loanProductId=" + product.getLoanProductId());
        }

        return new LoanTerms(interestType, repaymentMethod);
    }
}
