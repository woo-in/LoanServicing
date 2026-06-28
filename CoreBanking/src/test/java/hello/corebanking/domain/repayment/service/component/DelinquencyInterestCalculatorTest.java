package hello.corebanking.domain.repayment.service.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DelinquencyInterestCalculatorTest {

    private DelinquencyInterestCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DelinquencyInterestCalculator();
    }

    @Test
    @DisplayName("약정 금리에 연체 가산 금리 3%를 더한 값을 반환한다")
    void calculatePenaltyRate_normal() {
        BigDecimal result = calculator.calculatePenaltyRate(new BigDecimal("0.05"));

        assertThat(result).isEqualByComparingTo(new BigDecimal("0.08"));
    }

    @Test
    @DisplayName("계산 결과가 법정 최고 금리(15%)를 초과하면 15%를 반환한다")
    void calculatePenaltyRate_exceedsMaxLegalRate() {
        BigDecimal result = calculator.calculatePenaltyRate(new BigDecimal("0.14"));

        assertThat(result).isEqualByComparingTo(new BigDecimal("0.15"));
    }

    @Test
    @DisplayName("계산 결과가 법정 최고 금리(15%)와 동일하면 15%를 반환한다")
    void calculatePenaltyRate_exactlyAtMaxLegalRate() {
        BigDecimal result = calculator.calculatePenaltyRate(new BigDecimal("0.12"));

        assertThat(result).isEqualByComparingTo(new BigDecimal("0.15"));
    }

    @Test
    @DisplayName("약정 금리가 null이면 0을 반환한다")
    void calculatePenaltyRate_nullInput() {
        BigDecimal result = calculator.calculatePenaltyRate(null);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
