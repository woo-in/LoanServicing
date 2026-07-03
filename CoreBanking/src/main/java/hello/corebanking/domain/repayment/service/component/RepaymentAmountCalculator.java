package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.repayment.dto.RepaymentAmount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class RepaymentAmountCalculator {

    private static final int MONEY_SCALE = 4;
    private static final RoundingMode MONEY_ROUNDING = RoundingMode.HALF_UP;
    private static final MathContext DIVISION_CONTEXT = new MathContext(50, RoundingMode.HALF_UP);

    public RepaymentAmount calculate(RepaymentMethod method, BigDecimal balance, BigDecimal rate,
                                      int remainingInstallments, BigDecimal originalPrincipal, int totalInstallments) {
        return switch (method) {
            case LEVEL_PAYMENT -> calculateLevelPayment(balance, rate, remainingInstallments);
            case EQUAL_PRINCIPAL -> calculateEqualPrincipal(balance, rate, remainingInstallments, originalPrincipal, totalInstallments);
            case BULLET -> calculateBullet(balance, rate, remainingInstallments);
        };
    }

    private RepaymentAmount calculateLevelPayment(BigDecimal balance, BigDecimal rate, int remainingInstallments) {
        if (remainingInstallments == 1) {
            return lastInstallment(balance, rate);
        }

        BigDecimal onePlusRatePowN = BigDecimal.ONE.add(rate).pow(remainingInstallments);
        BigDecimal payment = balance.multiply(rate).multiply(onePlusRatePowN)
                .divide(onePlusRatePowN.subtract(BigDecimal.ONE), DIVISION_CONTEXT);
        BigDecimal interest = balance.multiply(rate);
        BigDecimal principal = payment.subtract(interest);

        return new RepaymentAmount(
                principal.setScale(MONEY_SCALE, MONEY_ROUNDING),
                interest.setScale(MONEY_SCALE, MONEY_ROUNDING));
    }

    private RepaymentAmount calculateEqualPrincipal(BigDecimal balance, BigDecimal rate, int remainingInstallments,
                                                      BigDecimal originalPrincipal, int totalInstallments) {
        if (remainingInstallments == 1) {
            return lastInstallment(balance, rate);
        }

        BigDecimal principal = originalPrincipal.divide(BigDecimal.valueOf(totalInstallments), DIVISION_CONTEXT);
        BigDecimal interest = balance.multiply(rate);

        return new RepaymentAmount(
                principal.setScale(MONEY_SCALE, MONEY_ROUNDING),
                interest.setScale(MONEY_SCALE, MONEY_ROUNDING));
    }

    private RepaymentAmount calculateBullet(BigDecimal balance, BigDecimal rate, int remainingInstallments) {
        if (remainingInstallments == 1) {
            return lastInstallment(balance, rate);
        }

        BigDecimal interest = balance.multiply(rate);

        return new RepaymentAmount(
                BigDecimal.ZERO.setScale(MONEY_SCALE, MONEY_ROUNDING),
                interest.setScale(MONEY_SCALE, MONEY_ROUNDING));
    }

    private RepaymentAmount lastInstallment(BigDecimal balance, BigDecimal rate) {
        BigDecimal principal = balance.setScale(MONEY_SCALE, MONEY_ROUNDING);
        BigDecimal interest = balance.multiply(rate).setScale(MONEY_SCALE, MONEY_ROUNDING);
        return new RepaymentAmount(principal, interest);
    }
}
