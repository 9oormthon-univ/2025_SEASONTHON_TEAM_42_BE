ㄷpackage next.career.domain.pinecone.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.embedding.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PineconeService {

    @Value("${pinecone.api-key}")
    private String apiKey;

    private final EmbeddingService embeddingService;

    private String jobHost;

        public void saveJobVector(Long jobId) {

            String id = String.valueOf(jobId);
            List<Float> vector = embeddingService.getEmbeddingJob(jobId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Api-Key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(jobHost + "/vectors/upsert", HttpMethod.POST, entity, String.class);
        }

}
