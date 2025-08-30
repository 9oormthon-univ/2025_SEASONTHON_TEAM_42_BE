package next.career.domain.embedding.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {


    private final WebClient openAiClient;
    private final JobRepository jobRepository;


    // open ai embedding
    private Mono<List<Float>> getEmbeddingMono(String text) {

        Map<String, Object> body = Map.of(
                "model", "text-embedding-3-small",
                "input", List.of(text)
        );

        return openAiClient.post()
                .uri("/embeddings")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> {
                    if (res == null) return List.of();
                    List<Map<String, Object>> data = (List<Map<String, Object>>) res.get("data");
                    if (data == null || data.isEmpty()) return List.of();
                    List<Number> nums = (List<Number>) data.get(0).get("embedding");
                    if (nums == null || nums.isEmpty()) return List.of();
                    return nums.stream().map(n -> n == null ? 0f : n.floatValue()).toList();
                });
    }

//    public List<Float> getEmbeddingJob(Long jobId) {
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOTFOUND));
//
//
//    }
}
