package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.dto.ContractedRate;
import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.product.dto.LoanTerms;
import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.repayment.entity.RepaymentSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ConfirmedRateProvider {

    private final ContractedRateProvider contractedRateProvider;
    private final LoanTermsProvider loanTermsProvider;
    private final BaseRateProvider baseRateProvider;

    public BigDecimal getConfirmedRate(LoanContract contract) {
        return calculateConfirmedRate(
                contractedRateProvider.getContractedRate(contract),
                loanTermsProvider.getLoanTerms(contract));
    }

    public BigDecimal getConfirmedRate(LoanAccount loanAccount) {
        return calculateConfirmedRate(
                contractedRateProvider.getContractedRate(loanAccount),
                loanTermsProvider.getLoanTerms(loanAccount));
    }

    public BigDecimal getConfirmedRate(RepaymentSchedule schedule) {
        return calculateConfirmedRate(
                contractedRateProvider.getContractedRate(schedule),
                loanTermsProvider.getLoanTerms(schedule));
    }

    private BigDecimal calculateConfirmedRate(ContractedRate contractedRate, LoanTerms loanTerms) {
        BigDecimal baseRate = loanTerms.interestType() == InterestType.FIXED
                ? contractedRate.contractBaseRate()
                : baseRateProvider.getBaseRate();
        return baseRate.add(contractedRate.contractAdditionalRate());
    }
}
