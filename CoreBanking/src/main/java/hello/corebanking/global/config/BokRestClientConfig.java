package hello.corebanking.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BokRestClientConfig {

    @Value("${external.api.koreaBank.requestUrl}")
    private String requestUrl;

    @Bean
    public RestClient bokRestClient() {
        return RestClient.builder()
                .baseUrl(requestUrl)
                .build();
    }
}
