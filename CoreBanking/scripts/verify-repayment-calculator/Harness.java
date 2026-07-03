import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.repayment.dto.RepaymentAmount;
import hello.corebanking.domain.repayment.service.component.RepaymentAmountCalculator;

import java.math.BigDecimal;
import java.util.List;

public class Harness {

    private static final List<BigDecimal> BALANCES = List.of(
            new BigDecimal("500000"),
            new BigDecimal("1234567.89"),
            new BigDecimal("10000000"),
            new BigDecimal("87654321.12"),
            new BigDecimal("250000000")
    );

    private static final List<BigDecimal> RATES = List.of(
            new BigDecimal("0.0025"),
            new BigDecimal("0.005"),
            new BigDecimal("0.0083333"),
            new BigDecimal("0.01"),
            new BigDecimal("0.02")
    );

    private static final List<Integer> REMAINING_INSTALLMENTS = List.of(1, 2, 3, 6, 12, 24, 36, 60, 120, 360);

    private static final List<Integer> TOTAL_INSTALLMENTS = List.of(12, 36, 60, 120, 360);

    public static void main(String[] args) {
        RepaymentAmountCalculator calculator = new RepaymentAmountCalculator();

        System.out.println("method,balance,rate,n,originalPrincipal,totalInstallments,principal,interest");

        for (BigDecimal balance : BALANCES) {
            for (BigDecimal rate : RATES) {
                for (int n : REMAINING_INSTALLMENTS) {
                    RepaymentAmount amount = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                            balance, rate, n, BigDecimal.ZERO, 0);
                    printRow(RepaymentMethod.LEVEL_PAYMENT, balance, rate, n, BigDecimal.ZERO, 0, amount);
                }

                for (int n : REMAINING_INSTALLMENTS) {
                    RepaymentAmount amount = calculator.calculate(RepaymentMethod.BULLET,
                            balance, rate, n, BigDecimal.ZERO, 0);
                    printRow(RepaymentMethod.BULLET, balance, rate, n, BigDecimal.ZERO, 0, amount);
                }

                for (int totalInstallments : TOTAL_INSTALLMENTS) {
                    for (int n : REMAINING_INSTALLMENTS) {
                        if (n > totalInstallments) {
                            continue;
                        }
                        RepaymentAmount amount = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                                balance, rate, n, balance, totalInstallments);
                        printRow(RepaymentMethod.EQUAL_PRINCIPAL, balance, rate, n, balance, totalInstallments, amount);
                    }
                }
            }
        }
    }

    private static void printRow(RepaymentMethod method, BigDecimal balance, BigDecimal rate, int n,
                                  BigDecimal originalPrincipal, int totalInstallments, RepaymentAmount amount) {
        System.out.println(String.join(",",
                method.name(),
                balance.toPlainString(),
                rate.toPlainString(),
                String.valueOf(n),
                originalPrincipal.toPlainString(),
                String.valueOf(totalInstallments),
                amount.principal().toPlainString(),
                amount.interest().toPlainString()));
    }
}
