package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.repayment.dto.RepaymentAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class RepaymentAmountCalculatorTest {

    private RepaymentAmountCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RepaymentAmountCalculator();
    }

    @Test
    @DisplayName("원리금균등 최초회차: 원금/이자를 정확히 계산한다")
    void levelPayment_firstInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                new BigDecimal("12000000"), new BigDecimal("0.01"), 12,
                BigDecimal.ZERO, 0);

        assertThat(result.principal()).isEqualByComparingTo("946185.4641");
        assertThat(result.interest()).isEqualByComparingTo("120000.0000");
    }

    @Test
    @DisplayName("원리금균등 중간회차: 원금/이자를 정확히 계산한다")
    void levelPayment_middleInstallment() {

        RepaymentAmount result = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                new BigDecimal("6000000"), new BigDecimal("0.01"), 6,
                BigDecimal.ZERO, 0);

        assertThat(result.principal()).isEqualByComparingTo("975290.2003");
        assertThat(result.interest()).isEqualByComparingTo("60000.0000");
    }

    @Test
    @DisplayName("원리금균등 마지막회차: 원금은 잔액과 정확히 일치한다")
    void levelPayment_lastInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                new BigDecimal("1000000"), new BigDecimal("0.01"), 1,
                BigDecimal.ZERO, 0);

        assertThat(result.principal()).isEqualByComparingTo("1000000.0000");
        assertThat(result.interest()).isEqualByComparingTo("10000.0000");
    }

    @Test
    @DisplayName("원금균등 최초회차: 원금은 최초 대출원금을 총회차로 나눈 값이다")
    void equalPrincipal_firstInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                new BigDecimal("10000000"), new BigDecimal("0.01"), 3,
                new BigDecimal("10000000"), 3);

        assertThat(result.principal()).isEqualByComparingTo("3333333.3333");
        assertThat(result.interest()).isEqualByComparingTo("100000.0000");
    }

    @Test
    @DisplayName("원금균등 중간회차: 원금은 매회차 동일하고 이자는 잔액 기준으로 계산된다")
    void equalPrincipal_middleInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                new BigDecimal("6666666.6667"), new BigDecimal("0.01"), 2,
                new BigDecimal("10000000"), 3);

        assertThat(result.principal()).isEqualByComparingTo("3333333.3333");
        assertThat(result.interest()).isEqualByComparingTo("66666.6667");
    }

    @Test
    @DisplayName("원금균등 마지막회차: 균등분할 대신 잔액으로 보정되어 반올림 오차가 남지 않는다")
    void equalPrincipal_lastInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                new BigDecimal("3333333.3334"), new BigDecimal("0.01"), 1,
                new BigDecimal("10000000"), 3);

        assertThat(result.principal()).isEqualByComparingTo("3333333.3334");
        assertThat(result.interest()).isEqualByComparingTo("33333.3333");
    }

    @Test
    @DisplayName("만기일시상환 최초회차: 원금은 0이고 이자만 발생한다")
    void bullet_firstInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.BULLET,
                new BigDecimal("5000000"), new BigDecimal("0.02"), 5,
                BigDecimal.ZERO, 0);

        assertThat(result.principal()).isEqualByComparingTo("0.0000");
        assertThat(result.interest()).isEqualByComparingTo("100000.0000");
    }

    @Test
    @DisplayName("만기일시상환 중간회차: 원금은 0이고 이자만 발생한다")
    void bullet_middleInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.BULLET,
                new BigDecimal("3000000"), new BigDecimal("0.02"), 3,
                BigDecimal.ZERO, 0);

        assertThat(result.principal()).isEqualByComparingTo("0.0000");
        assertThat(result.interest()).isEqualByComparingTo("60000.0000");
    }

    @Test
    @DisplayName("만기일시상환 마지막회차: 원금은 잔액 전액이다")
    void bullet_lastInstallment() {
        RepaymentAmount result = calculator.calculate(RepaymentMethod.BULLET,
                new BigDecimal("1000000"), new BigDecimal("0.02"), 1,
                BigDecimal.ZERO, 0);

        assertThat(result.principal()).isEqualByComparingTo("1000000.0000");
        assertThat(result.interest()).isEqualByComparingTo("20000.0000");
    }

    @Test
    @DisplayName("원리금균등: 동일 입력에 대해 항상 동일한 결과를 반환한다")
    void levelPayment_isPure() {
        RepaymentAmount first = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                new BigDecimal("12000000"), new BigDecimal("0.01"), 12, BigDecimal.ZERO, 0);
        RepaymentAmount second = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                new BigDecimal("12000000"), new BigDecimal("0.01"), 12, BigDecimal.ZERO, 0);

        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("원금균등: 동일 입력에 대해 항상 동일한 결과를 반환한다")
    void equalPrincipal_isPure() {
        RepaymentAmount first = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                new BigDecimal("6666666.6667"), new BigDecimal("0.01"), 2, new BigDecimal("10000000"), 3);
        RepaymentAmount second = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                new BigDecimal("6666666.6667"), new BigDecimal("0.01"), 2, new BigDecimal("10000000"), 3);

        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("만기일시상환: 동일 입력에 대해 항상 동일한 결과를 반환한다")
    void bullet_isPure() {
        RepaymentAmount first = calculator.calculate(RepaymentMethod.BULLET,
                new BigDecimal("3000000"), new BigDecimal("0.02"), 3, BigDecimal.ZERO, 0);
        RepaymentAmount second = calculator.calculate(RepaymentMethod.BULLET,
                new BigDecimal("3000000"), new BigDecimal("0.02"), 3, BigDecimal.ZERO, 0);

        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("원리금균등: n회 반복 계산 시 원금 합계는 최초원금과 같고 잔액은 0으로 수렴한다")
    void levelPayment_convergesToZeroBalance() {
        BigDecimal originalPrincipal = new BigDecimal("12000000");
        BigDecimal rate = new BigDecimal("0.01");
        int totalInstallments = 12;

        BigDecimal balance = originalPrincipal;
        BigDecimal principalSum = BigDecimal.ZERO;

        for (int n = totalInstallments; n >= 1; n--) {
            RepaymentAmount amount = calculator.calculate(RepaymentMethod.LEVEL_PAYMENT,
                    balance, rate, n, BigDecimal.ZERO, 0);
            principalSum = principalSum.add(amount.principal());
            balance = balance.subtract(amount.principal());
        }

        assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(principalSum).isEqualByComparingTo(originalPrincipal);
    }

    @Test
    @DisplayName("원금균등: n회 반복 계산 시 원금 합계는 최초원금과 같고 잔액은 0으로 수렴한다")
    void equalPrincipal_convergesToZeroBalance() {
        BigDecimal originalPrincipal = new BigDecimal("10000000");
        BigDecimal rate = new BigDecimal("0.01");
        int totalInstallments = 3;

        BigDecimal balance = originalPrincipal;
        BigDecimal principalSum = BigDecimal.ZERO;

        for (int n = totalInstallments; n >= 1; n--) {
            RepaymentAmount amount = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                    balance, rate, n, originalPrincipal, totalInstallments);
            principalSum = principalSum.add(amount.principal());
            balance = balance.subtract(amount.principal());
        }

        assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(principalSum).isEqualByComparingTo(originalPrincipal);
    }

    @Test
    @DisplayName("만기일시상환: n회 반복 계산 시 원금 합계는 최초원금과 같고 잔액은 0으로 수렴한다")
    void bullet_convergesToZeroBalance() {
        BigDecimal originalPrincipal = new BigDecimal("1000000");
        BigDecimal rate = new BigDecimal("0.02");
        int totalInstallments = 3;

        BigDecimal balance = originalPrincipal;
        BigDecimal principalSum = BigDecimal.ZERO;

        for (int n = totalInstallments; n >= 1; n--) {
            RepaymentAmount amount = calculator.calculate(RepaymentMethod.BULLET,
                    balance, rate, n, BigDecimal.ZERO, 0);
            principalSum = principalSum.add(amount.principal());
            balance = balance.subtract(amount.principal());
        }

        assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(principalSum).isEqualByComparingTo(originalPrincipal);
    }

    @Test
    @DisplayName("상환방법에 따라 올바른 계산식으로 분기한다")
    void calculate_dispatchesByRepaymentMethod() {
        BigDecimal balance = new BigDecimal("1000000");
        BigDecimal rate = new BigDecimal("0.01");
        int remainingInstallments = 5;
        BigDecimal originalPrincipal = new BigDecimal("1000000");
        int totalInstallments = 5;

        RepaymentAmount equalPrincipal = calculator.calculate(RepaymentMethod.EQUAL_PRINCIPAL,
                balance, rate, remainingInstallments, originalPrincipal, totalInstallments);
        RepaymentAmount bullet = calculator.calculate(RepaymentMethod.BULLET,
                balance, rate, remainingInstallments, originalPrincipal, totalInstallments);

        assertThat(equalPrincipal.principal()).isEqualByComparingTo("200000.0000");
        assertThat(bullet.principal()).isEqualByComparingTo("0.0000");
        assertThat(equalPrincipal.principal()).isNotEqualByComparingTo(bullet.principal());
    }
}
