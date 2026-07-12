package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.loan.repository.LoanContractMapper;
import hello.corebanking.domain.repayment.entity.RepaymentScheduleStatus;
import hello.corebanking.domain.repayment.repository.RepaymentScheduleMapper;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ExpectedBalanceProvider {

    private final LoanContractMapper loanContractMapper;
    private final RepaymentScheduleMapper repaymentScheduleMapper;

    public BigDecimal getExpectedBalance(LoanAccount loanAccount) {
        LoanContract contract = loanContractMapper.findById(loanAccount.getLoanContractId())
                .orElseThrow(() -> new NotFoundException(
                        "계약을 찾을 수 없습니다. contractId=" + loanAccount.getLoanContractId()));

        BigDecimal confirmedPrincipalSum = repaymentScheduleMapper.sumScheduledPrincipalByLoanAccountId(
                loanAccount.getLoanAccountId(), RepaymentScheduleStatus.SCHEDULED);

        return contract.getContractPrincipal().subtract(confirmedPrincipalSum);
    }
}
