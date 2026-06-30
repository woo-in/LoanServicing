package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.global.client.bok.BokApiClient;
import hello.corebanking.global.exception.BaseRateFetchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BaseRateProvider {

    private static final int MAX_FALLBACK_DAYS = 15;

    private final BokApiClient bokApiClient;

    public BigDecimal getBaseRate() {
        for (int i = 0; i < MAX_FALLBACK_DAYS; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            Optional<BigDecimal> rate = bokApiClient.fetchDailyMarketRate(date);
            if (rate.isPresent()) {
                return rate.get();
            }
        }
        throw new BaseRateFetchException(
                "최근 " + MAX_FALLBACK_DAYS + "일간 기준금리 데이터를 조회하지 못했습니다.");
    }
}
