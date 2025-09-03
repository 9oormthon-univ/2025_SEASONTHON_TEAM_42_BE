package next.career.domain.job.service.dto;

import lombok.*;
import next.career.domain.job.entity.Job;

import java.time.LocalDate;

public class JobDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AllResponse{

        private String jobId;           // 공고 고유 식별자
        private String companyName;     // 기업명
        private String companyLogo;     // 기업 로고 (URL or 이미지 경로)
        private String jobTitle;        // 직무명
        private String jobCategory;     // 직무 분야 (산업)
        private String workLocation;    // 근무 지역
        private String requiredSkills;  // 필요 기술
        private LocalDate closingDate;  // 공고 마감일
        private Integer jobRecommendScore; // 직업 추천도
        private Boolean isScrap; // 스크랩 했는지

        public static AllResponse of(Job job, Integer jobRecommendScore, Boolean isScrap) {
            return AllResponse.builder()
                    .jobId(job.getJobId())
                    .companyName(job.getCompanyName())
                    .companyLogo(job.getCompanyLogo())
                    .jobTitle(job.getJobTitle())
                    .jobCategory(job.getJobCategory())
                    .workLocation(job.getWorkLocation())
                    .requiredSkills(job.getRequiredSkills())
                    .closingDate(job.getClosingDate())
                    .jobRecommendScore(jobRecommendScore)
                    .isScrap(isScrap)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Response{

        private Long id; // 내부 PK (자동 생성)
        private String jobId;           // 공고 고유 식별자
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
        private LocalDate postingDate;  // 공고 등록일
        private LocalDate closingDate;  // 공고 마감일
        private String applyLink;       // 지원 링크
        private Integer jobRecommendScore; // 직업 추천도
        private Boolean isScrap; // 스크랩 했는지

        public static Response of(Job job, Integer jobRecommendScore, Boolean isScrap) {
            return Response.builder()
                    .id(job.getId())
                    .jobId(job.getJobId())
                    .companyName(job.getCompanyName())
                    .companyLogo(job.getCompanyLogo())
                    .jobTitle(job.getJobTitle())
                    .jobCategory(job.getJobCategory())
                    .workLocation(job.getWorkLocation())
                    .employmentType(job.getEmploymentType())
                    .salary(job.getSalary())
                    .workPeriod(job.getWorkPeriod())
                    .experience(job.getExperience())
                    .requiredSkills(job.getRequiredSkills())
                    .preferredSkills(job.getPreferredSkills())
                    .postingDate(job.getPostingDate())
                    .closingDate(job.getClosingDate())
                    .applyLink(job.getApplyLink())
                    .jobRecommendScore(jobRecommendScore)
                    .isScrap(isScrap)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class RecommendJob{
        private String first;
        private String second;
        private String third;
    }
}
