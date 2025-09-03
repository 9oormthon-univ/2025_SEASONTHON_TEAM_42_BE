package next.career.domain.embedding.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.user.entity.Member;
import next.career.domain.user.repository.UserRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {


    private final WebClient openAiClient;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE;

    public Mono<List<Float>> getEmbeddingJob(Long jobId) {
        return Mono.fromCallable(() ->
                        jobRepository.findById(jobId)
                                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR))
                )
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::toEmbeddingJobText)
                .flatMap(this::getEmbeddingMono);
    }

    public Mono<List<Float>> getEmbeddingMember(Member member) {
        return Mono.just(member)
                .map(this::toEmbeddingMemberText)
                .flatMap(this::getEmbeddingMono);
    }


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

    private String toEmbeddingMemberText(Member member) {
        return """
            나이: %s
            """.formatted(
                calculateAge(member.getBirthDate())
        );
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(
                birthDate,
                LocalDate.now()
        ).getYears();
    }

    private String toEmbeddingJobText(Job job) {
        return """
            직무명: %s
            직무 분야: %s
            기업명: %s
            근무 지역: %s
            고용 형태: %s
            경력: %s
            필수 기술: %s
            우대 사항: %s
            급여: %s
            근무 기간: %s
            공고 등록일: %s
            공고 마감일: %s
            """.formatted(
                nz(job.getJobTitle()),
                nz(job.getJobCategory()),
                nz(job.getCompanyName()),
                nz(job.getWorkLocation()),
                nz(job.getEmploymentType()),
                nz(job.getExperience()),
                nz(job.getRequiredSkills()),
                nz(job.getPreferredSkills()),
                nz(job.getSalary()),
                nz(job.getWorkPeriod()),
                fmt(job.getPostingDate()),
                fmt(job.getClosingDate())
        );
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    private static String fmt(LocalDate d) {
        return d == null ? "" : d.format(F);
    }
}
