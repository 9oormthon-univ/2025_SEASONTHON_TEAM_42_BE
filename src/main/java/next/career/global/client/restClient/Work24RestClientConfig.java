package next.career.global.client.restClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Work24RestClientConfig {

    @Bean
    public RestClient work24RestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://www.work24.go.kr")
                .build();
    }
}