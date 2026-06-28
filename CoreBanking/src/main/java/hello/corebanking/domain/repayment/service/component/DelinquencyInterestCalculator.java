package hello.corebanking.domain.repayment.service.component;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DelinquencyInterestCalculator {

    private static final BigDecimal DELINQUENCY_PENALTY_SPREAD = new BigDecimal("0.03");
    private static final BigDecimal MAX_LEGAL_INTEREST_RATE    = new BigDecimal("0.15");

    public BigDecimal calculatePenaltyRate(BigDecimal appliedRate) {
        if (appliedRate == null) return BigDecimal.ZERO;
        BigDecimal penaltyRate = appliedRate.add(DELINQUENCY_PENALTY_SPREAD);
        return penaltyRate.min(MAX_LEGAL_INTEREST_RATE);
    }
}
