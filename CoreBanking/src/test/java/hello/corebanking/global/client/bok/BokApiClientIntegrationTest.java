package hello.corebanking.global.client.bok;

import hello.corebanking.domain.repayment.service.component.BaseRateProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class BokApiClientIntegrationTest {

    @Autowired
    private BokApiClient bokApiClient;

    @Autowired
    private BaseRateProvider baseRateProvider;

    @Test
    @DisplayName("평일 날짜로 CD 91일 금리를 조회하면 값이 반환된다")
    void fetchDailyMarketRate_weekday_returnsRate() {
        LocalDate weekday = LocalDate.of(2025, 6, 27); // 금요일

        Optional<BigDecimal> rate = bokApiClient.fetchDailyMarketRate(weekday);

        assertThat(rate).isPresent();
        assertThat(rate.get()).isGreaterThan(BigDecimal.ZERO);
        log.info("CD 91일 금리 ({}): {}%", weekday, rate.get());
    }

    @Test
    @DisplayName("주말 날짜로 조회하면 빈 값이 반환된다")
    void fetchDailyMarketRate_weekend_returnsEmpty() {
        LocalDate weekend = LocalDate.of(2025, 6, 28); // 토요일

        Optional<BigDecimal> rate = bokApiClient.fetchDailyMarketRate(weekend);

        assertThat(rate).isEmpty();
    }

    @Test
    @DisplayName("BaseRateProvider 가 fallback 으로 최근 금리를 반환한다")
    void getBaseRate_returnsMostRecentRate() {
        BigDecimal rate = baseRateProvider.getBaseRate();

        assertThat(rate).isGreaterThan(BigDecimal.ZERO);
        log.info("최근 기준금리: {}%", rate);
    }
}
