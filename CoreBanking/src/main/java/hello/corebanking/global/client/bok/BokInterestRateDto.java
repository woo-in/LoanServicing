package hello.corebanking.global.client.bok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BokInterestRateDto {

    @JsonProperty("StatisticSearch")
    private StatisticSearch statisticSearch;

    @Getter
    @NoArgsConstructor
    public static class StatisticSearch {

        @JsonProperty("list_total_count")
        private int listTotalCount;

        @JsonProperty("row")
        private List<Row> rows;
    }

    @Getter
    @NoArgsConstructor
    public static class Row {

        @JsonProperty("DATA_VALUE")
        private String dataValue;

        @JsonProperty("TIME")
        private String time;
    }
}
