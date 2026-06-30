package hello.corebanking.global.client.bok;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class BokApiClient {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String CD_91_ITEM_CODE = "010502000";

    private final RestClient bokRestClient;
    private final String authKey;

    public BokApiClient(RestClient bokRestClient,
                        @Value("${external.api.koreaBank.auth-key}") String authKey) {
        this.bokRestClient = bokRestClient;
        this.authKey = authKey;
    }

    public Optional<BigDecimal> fetchDailyMarketRate(LocalDate date) {
        String dateStr = date.format(DATE_FORMAT);

        BokInterestRateDto response = bokRestClient.get()
                .uri("/StatisticSearch/{authKey}/json/kr/1/1/817Y002/D/{date}/{date}/{itemCode}",
                        authKey, dateStr, dateStr, CD_91_ITEM_CODE)
                .retrieve()
                .body(BokInterestRateDto.class);

        if (response == null || response.getStatisticSearch() == null
                || response.getStatisticSearch().getListTotalCount() == 0) {
            return Optional.empty();
        }

        List<BokInterestRateDto.Row> rows = response.getStatisticSearch().getRows();
        if (rows == null || rows.isEmpty()) {
            return Optional.empty();
        }

        String dataValue = rows.get(0).getDataValue();
        if (dataValue == null || dataValue.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(new BigDecimal(dataValue));
    }
}
