package next.career.domain.pinecone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.embedding.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PineconeService {

    @Value("${pinecone.api-key}")
    private String apiKey;

    private final WebClient pineconeClient;

    private final EmbeddingService embeddingService;

    public Mono<Void> saveJobVector(Long jobId) {
        String id = String.valueOf(jobId);

        return embeddingService.getEmbeddingJob(jobId)
                .flatMap(vector -> {
                    Map<String, Object> body = Map.of(
                            "vectors", List.of(Map.of("id", id, "values", vector))
                    );

                    return pineconeClient.post()
                            .uri("/vectors/upsert")
                            .header("Api-Key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body)
                            .retrieve()
                            .toBodilessEntity()
                            .then();
                })
                .doOnError(e -> log.warn("upsert failed id={}", id, e));
    }

}
