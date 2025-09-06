package next.career.domain.job.service.dto;

import lombok.*;
import next.career.domain.job.entity.Job;
import next.career.domain.openai.dto.RecommendDto;

import java.time.LocalDate;

public class JobDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AllResponse{

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
        private Boolean isBookmark; // 북마크

        public static AllResponse of(Job job, Boolean isBookmark) {
            return AllResponse.builder()
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
                    .postingDate(job.getPostingDate() != null ? job.getPostingDate().toString() : null)
                    .closingDate(job.getClosingDate() != null ? job.getClosingDate().toString() : null)
                    .applyLink(job.getApplyLink())
                    .imageUrl(job.getImageUrl())
                    .isBookmark(isBookmark)
                    .build();
        }

        public static AllResponse ofAnonymous(Job job) {
            return AllResponse.builder()
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
                    .postingDate(job.getPostingDate() != null ? job.getPostingDate().toString() : null)
                    .closingDate(job.getClosingDate() != null ? job.getClosingDate().toString() : null)
                    .applyLink(job.getApplyLink())
                    .imageUrl(job.getImageUrl())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Response{

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
        private Boolean isScrap; // 스크랩 했는지

        public static Response of(Job job, Boolean isScrap) {
            return Response.builder()
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
                    .isScrap(isScrap)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class RecommendJob {
        private Occupation first;
        private Occupation second;
        private Occupation third;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Builder
        public static class Occupation {
            private String imageUrl;
            private String occupationName;
            private String description;
            private String strength;
            private String workCondition;
            private String wish;
            private String score;

            public static Occupation of(RecommendDto.OccupationResponse.Occupation o) {
                return Occupation.builder()
                        .imageUrl(o.getImageUrl())
                        .occupationName(o.getOccupationName())
                        .description(o.getDescription())
                        .score(o.getScore())
                        .build();
            }
        }
    }
}
