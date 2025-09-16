package next.career.global.client.webClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${pinecone.api-key}")
    private String pineconeApiKey;

    @Value("${pinecone.host}")
    private String pineconeHost;

    @Value("${pinecone.edu.host}")
    private String pineconeEduHost;

    @Value("${seoul.job.host}")
    private String seoulJobHost;

    @Value("${seoul.job.api-key}")
    private String seoulJobApiKey;

    private HttpClient httpClient() {
        return HttpClient.create()
                .compress(true)
                .responseTimeout(Duration.ofSeconds(30))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS)));
    }

    @Bean
    public WebClient openAiClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean
    public WebClient pineconeClient(WebClient.Builder builder) {
        return builder
                .baseUrl("")
                .defaultHeader("Api-Key", pineconeApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean
    public WebClient seoulJobClient(WebClient.Builder builder) {
        return builder
                .baseUrl(String.format("%s/%s", seoulJobHost, seoulJobApiKey))
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }
}
