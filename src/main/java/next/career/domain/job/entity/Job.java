package next.career.domain.job.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId; // 내부 PK (자동 생성)

    private String companyName;     // 기업명
    private String companyLogo;     // 기업 로고 (URL or 이미지 경로)
    private String jobTitle;        // 직무명
    private String jobCategory;     // 직무 분야 (산업)
    private String workLocation;    // 근무 지역
    private String employmentType;  // 고용 형태 (정규직, 계약직 등)
    private String salary;          // 급여
    private String workPeriod;      // 근무 기간
    private String experience;      // 경력 요구사항
    private String requiredSkills;  // 필요 기술
    private String preferredSkills; // 우대 사항
    private String postingDate;  // 공고 등록일
    private String closingDate;  // 공고 마감일
    private String applyLink;       // 지원 링크
    private String imageUrl;

}
