package next.career.domain.embedding.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.user.entity.Member;
import next.career.domain.user.entity.MemberDetail;
import next.career.domain.user.repository.MemberRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {


    private final WebClient openAiClient;
    private final JobRepository jobRepository;
    private final MemberRepository memberRepository;

    private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE;

    public Mono<List<Float>> getEmbeddingJob(Long jobId) {
        return Mono.fromCallable(() ->
                        jobRepository.findById(jobId)
                                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR))
                )
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::toEmbeddingJobTextV2)
                .flatMap(this::getEmbeddingMono);
    }

    public Mono<List<Float>> getEmbeddingMember(Member member) {
        return Mono.just(member)
                .map(this::toEmbeddingMemberTextV2)
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
        MemberDetail memberDetail = member.getMemberDetail();
        return """
        이름: %s
        나이: %s
        성별: %s
        주소: %s
        경력: %s
        자격증/기술: %s
        성격 유형: %s
        관심사: %s
        선호 근무 스타일: %s
        피하고 싶은 조건: %s
        가능 근무 시간: %s
        신체 조건: %s
        교육 및 커리어 목표: %s
        """.formatted(
                member.getName(),
                calculateAge(member.getBirthDate()),
                member.getGender() != null ? member.getGender().name() : "",
                member.getAddress() != null ? member.getAddress().toString() : "",
                memberDetail != null ? memberDetail.getExperience() : "",
                memberDetail != null ? memberDetail.getCertificateOrSkill() : "",
                memberDetail != null ? memberDetail.getPersonalityType() : "",
                memberDetail != null ? memberDetail.getInterests() : "",
                memberDetail != null ? memberDetail.getPreferredWorkStyles() : "",
                memberDetail != null ? memberDetail.getAvoidConditions() : "",
                memberDetail != null ? memberDetail.getJob() : "",
                memberDetail != null ? memberDetail.getPhysicalCondition() : "",
                memberDetail != null ? memberDetail.getEducationAndCareerGoal() : ""
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
                nz(job.getPostingDate()),
                nz(job.getClosingDate())
        );
    }

    //TODO: 추후에 수정
    private String toEmbeddingJobTextV2(Job job) {
        return """
        이 채용 공고는 %s 분야의 %s 직무입니다.
        기업명은 %s이며, 근무 지역은 %s입니다.
        고용 형태는 %s이고 요구 경력은 %s입니다.
        필수 기술은 %s이며, 우대 사항으로 %s가 있습니다.
        급여는 %s이고, 근무 기간은 %s입니다.
        공고는 %s에 등록되었고, %s에 마감됩니다.
        """.formatted(
                nz(job.getJobCategory()),
                nz(job.getJobTitle()),
                nz(job.getCompanyName()),
                nz(job.getWorkLocation()),
                nz(job.getEmploymentType()),
                nz(job.getPostingDate()),
                nz(job.getClosingDate())
        );
    }

    //TODO: 추후에 수정
    private String toEmbeddingMemberTextV2(Member member) {
        MemberDetail d = member.getMemberDetail();
        return """
        %s은(는) %d세 %s입니다. 
        현재 거주지는 %s이며, 경력은 %s이고 보유 기술/자격증은 %s입니다. 
        성격 유형은 %s이며 관심사는 %s입니다. 
        선호하는 근무 스타일은 %s이고, 피하고 싶은 조건은 %s입니다. 
        가능 근무 시간은 %s이고, 신체 조건은 %s입니다. 
        교육 및 커리어 목표는 %s입니다.
        """.formatted(
                member.getName(),
                calculateAge(member.getBirthDate()),
                member.getGender() != null ? member.getGender().name() : "",
                member.getAddress() != null ? member.getAddress().toString() : "미입력",
                nz(d != null ? d.getExperience() : ""),
                nz(d != null ? d.getCertificateOrSkill() : ""),
                nz(d != null ? d.getPersonalityType() : ""),
                nz(d != null ? d.getInterests() : ""),
                nz(d != null ? d.getPreferredWorkStyles() : ""),
                nz(d != null ? d.getAvoidConditions() : ""),
                nz(d != null ? d.getJob() : ""),
                nz(d != null ? d.getPhysicalCondition() : ""),
                nz(d != null ? d.getEducationAndCareerGoal() : "")
        );
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    private static String fmt(LocalDate d) {
        return d == null ? "" : d.format(F);
    }
}
