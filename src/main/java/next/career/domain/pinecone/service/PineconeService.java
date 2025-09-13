package next.career.domain.pinecone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.embedding.service.EmbeddingService;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.service.dto.PineconeRecommendDto;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
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
    private final JobRepository jobRepository;

    public Mono<Void> saveJobVector(Long jobId) {
        Mono<Job> jobMono = Mono.fromCallable(() ->
                jobRepository.findById(jobId)
                        .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR))
        ).subscribeOn(Schedulers.boundedElastic());

        return Mono.zip(embeddingService.getEmbeddingJob(jobId), jobMono)
                .flatMap(tuple -> {
                    List<Float> vector = tuple.getT1();
                    Job job = tuple.getT2();

                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("jobId", job.getJobId());

                    Map<String, Object> body = Map.of(
                            "vectors", List.of(Map.of(
                                    "id", String.valueOf(job.getJobId()),
                                    "values", vector,
                                    "metadata", metadata
                            ))
                    );
                    log.info("body = {}", body);

                    return pineconeClient.post()
                            .uri("/vectors/upsert")
                            .header("Api-Key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, r ->
                                    r.bodyToMono(String.class).flatMap(msg ->
                                            Mono.error(new RuntimeException("Pinecone query failed: " + msg))
                                    )
                            )
                            .toBodilessEntity()
                            .then();
                })
                .doOnError(e -> log.warn("upsert failed id={}", jobId, e));
    }

    public List<PineconeRecommendDto> getRecommendJob(List<Float> vector) {

        Map<String, Object> body = Map.of(
                "vector", vector,
                "topK", 8,
                "includeMetadata", true
        );

        Map response = pineconeClient.post()
                .uri("/query")
                .header("Api-Key", apiKey)
                .header("X-Pinecone-Api-Version", "2025-04")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).flatMap(msg ->
                                Mono.error(new RuntimeException("Pinecone query failed: " + msg))
                        )
                )
                .bodyToMono(Map.class)
                .block();

        log.info("get recommend job response = {}", response);

        List<Map<String, Object>> matches = (List<Map<String, Object>>) response.get("matches");
        if (matches == null || matches.isEmpty()) {
            throw new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR);
        }

        return matches.stream()
                .map(m -> new PineconeRecommendDto(
                        Long.valueOf(String.valueOf(m.get("id"))),
                        (long) (Double.parseDouble(String.valueOf(m.get("score"))) * 100)
                ))
                .toList();
    }


    @Transactional
    public void saveAllJobVector() {
        List<Job> jobList = jobRepository.findAll();

        for (Job job : jobList) {
            saveJobVector(job.getJobId())
                    .doOnSubscribe(s -> log.info("▶️ Start embedding for jobId={}", job.getJobId()))
                    .doOnSuccess(v -> log.info("✅ Saved embedding for jobId={}", job.getJobId()))
                    .doOnError(e -> log.error("❌ Failed embedding for jobId={}", job.getJobId(), e))
                    .block(); // 호출을 실제로 실행 (동기)
        }

    }
}
