package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.dto.ContractedRate;
import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.loan.repository.LoanContractMapper;
import hello.corebanking.domain.repayment.entity.RepaymentSchedule;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractedRateProvider {

    private final LoanContractMapper loanContractMapper;

    public ContractedRate getContractedRate(LoanContract contract) {
        return new ContractedRate(contract.getContractBaseRate(), contract.getContractAdditionalRate());
    }

    public ContractedRate getContractedRate(LoanAccount loanAccount) {
        LoanContract contract = loanContractMapper.findById(loanAccount.getLoanContractId())
                .orElseThrow(() -> new NotFoundException(
                        "계약을 찾을 수 없습니다. contractId=" + loanAccount.getLoanContractId()));
        return new ContractedRate(contract.getContractBaseRate(), contract.getContractAdditionalRate());
    }

    public ContractedRate getContractedRate(RepaymentSchedule schedule) {
        LoanContract contract = loanContractMapper.findByLoanAccountId(schedule.getLoanAccountId())
                .orElseThrow(() -> new NotFoundException(
                        "계약을 찾을 수 없습니다. loanAccountId=" + schedule.getLoanAccountId()));
        return new ContractedRate(contract.getContractBaseRate(), contract.getContractAdditionalRate());
    }
}
