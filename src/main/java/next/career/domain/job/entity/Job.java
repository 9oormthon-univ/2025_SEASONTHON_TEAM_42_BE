package next.career.domain.job.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId; // 내부 PK (자동 생성)

    @Column(columnDefinition = "TEXT")
    private String companyName;     // 기업명

    @Column(columnDefinition = "TEXT")
    private String jobCodeName;     // 직종명

    private Integer recruitNumber;  // 모집 인원 (숫자라 그대로 둠)

    @Column(columnDefinition = "TEXT")
    private String employmentType;  // 고용 형태 (정규직, 계약직 등)

    @Column(columnDefinition = "TEXT")
    private String workLocation;    // 근무 지역

    @Column(columnDefinition = "TEXT")
    private String description;     // 직무 내용

    @Column(columnDefinition = "TEXT")
    private String wage;            // 임금

    @Column(columnDefinition = "TEXT")
    private String insurance;       // 4대 보험

    @Column(columnDefinition = "TEXT")
    private String workTime;        // 근무 시간

    @Column(columnDefinition = "TEXT")
    private String managerPhone;    // 담당자 연락처

    @Column(columnDefinition = "TEXT")
    private String jobTitle;        // 직무명

    @Column(columnDefinition = "TEXT")
    private String screeningMethod; // 전형 방법

    @Column(columnDefinition = "TEXT")
    private String receptionMethod; // 접수 방법

    @Column(columnDefinition = "TEXT")
    private String requiredDocuments; // 제출 서류

    @Column(columnDefinition = "TEXT")
    private String jobCategory;     // 직무 분야 (산업)

    @Column(columnDefinition = "TEXT")
    private String postingDate;     // 공고 등록일

    @Column(columnDefinition = "TEXT")
    private String closingDate;     // 공고 마감일
    private String imageUrl;

    public static Job ofSeoulJob(
            String companyName,
            String jobCodeName,
            Integer recruitNumber,
            String employmentType,
            String workLocation,
            String description,
            String wage,
            String insurance,
            String workTime,
            String managerPhone,
            String jobTitle,
            String screeningMethod,
            String receptionMethod,
            String requiredDocuments,
            String jobCategory,
            String postingDate,
            String closingDate
    ) {
        return Job.builder()
                .companyName(companyName)
                .jobCodeName(jobCodeName)
                .recruitNumber(recruitNumber)
                .employmentType(employmentType)
                .workLocation(workLocation)
                .description(description)
                .wage(wage)
                .insurance(insurance)
                .workTime(workTime)
                .managerPhone(managerPhone)
                .jobTitle(jobTitle)
                .screeningMethod(screeningMethod)
                .receptionMethod(receptionMethod)
                .requiredDocuments(requiredDocuments)
                .jobCategory(jobCategory)
                .postingDate(postingDate)
                .closingDate(extractDate(closingDate))
                .imageUrl(null)
                .build();
    }

    private static String extractDate(String closingDate) {
        if (closingDate == null || closingDate.isEmpty()) {
            return null;
        }
        String[] parts = closingDate.split(" ");
        return parts[0];
    }
}
