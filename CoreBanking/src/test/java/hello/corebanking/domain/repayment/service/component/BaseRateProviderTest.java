package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.global.client.bok.BokApiClient;
import hello.corebanking.global.exception.BaseRateFetchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseRateProviderTest {

    @Mock
    private BokApiClient bokApiClient;

    @InjectMocks
    private BaseRateProvider baseRateProvider;

    @Test
    @DisplayName("오늘 날짜에 데이터가 있으면 즉시 반환한다")
    void getBaseRate_todayHasData() {
        when(bokApiClient.fetchDailyMarketRate(LocalDate.now()))
                .thenReturn(Optional.of(new BigDecimal("3.56")));

        BigDecimal result = baseRateProvider.getBaseRate();

        assertThat(result).isEqualByComparingTo("3.56");
    }

    @Test
    @DisplayName("오늘 데이터가 없고 3일 전에 데이터가 있으면 3일 전 값을 반환한다")
    void getBaseRate_fallbackToThreeDaysAgo() {
        when(bokApiClient.fetchDailyMarketRate(LocalDate.now())).thenReturn(Optional.empty());
        when(bokApiClient.fetchDailyMarketRate(LocalDate.now().minusDays(1))).thenReturn(Optional.empty());
        when(bokApiClient.fetchDailyMarketRate(LocalDate.now().minusDays(2))).thenReturn(Optional.empty());
        when(bokApiClient.fetchDailyMarketRate(LocalDate.now().minusDays(3)))
                .thenReturn(Optional.of(new BigDecimal("3.50")));

        BigDecimal result = baseRateProvider.getBaseRate();

        assertThat(result).isEqualByComparingTo("3.50");
    }

    @Test
    @DisplayName("15일 모두 데이터가 없으면 BaseRateFetchException 을 던진다")
    void getBaseRate_allFail_throwsException() {
        when(bokApiClient.fetchDailyMarketRate(any(LocalDate.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> baseRateProvider.getBaseRate())
                .isInstanceOf(BaseRateFetchException.class)
                .hasMessageContaining("15");
    }
}
