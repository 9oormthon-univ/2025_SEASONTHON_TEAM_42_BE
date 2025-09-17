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
    public static class AllResponse {

        private Long jobId;
        private String companyName;
        private String jobCodeName;
        private Integer recruitNumber;
        private String employmentType;
        private String workLocation;
        private String description;
        private String wage;
        private String insurance;
        private String workTime;
        private String managerPhone;
        private String jobTitle;
        private String screeningMethod;
        private String receptionMethod;
        private String requiredDocuments;
        private String jobCategory;
        private String postingDate;
        private String closingDate;
        private String imageUrl;
        private Boolean isBookmark;
        private Long score; // 추천 점수 (optional)

        public static AllResponse of(Job job, Boolean isBookmark) {
            return AllResponse.builder()
                    .jobId(job.getJobId())
                    .companyName(job.getCompanyName())
                    .jobCodeName(job.getJobCodeName())
                    .recruitNumber(job.getRecruitNumber())
                    .employmentType(job.getEmploymentType())
                    .workLocation(job.getWorkLocation())
                    .description(job.getDescription())
                    .wage(job.getWage())
                    .insurance(job.getInsurance())
                    .workTime(job.getWorkTime())
                    .managerPhone(job.getManagerPhone())
                    .jobTitle(job.getJobTitle())
                    .screeningMethod(job.getScreeningMethod())
                    .receptionMethod(job.getReceptionMethod())
                    .requiredDocuments(job.getRequiredDocuments())
                    .jobCategory(job.getJobCategory())
                    .postingDate(job.getPostingDate())
                    .closingDate(job.getClosingDate())
                    .imageUrl(job.getImageUrl())
                    .isBookmark(isBookmark)
                    .build();
        }

        public static AllResponse ofAnonymous(Job job) {
            return AllResponse.builder()
                    .jobId(job.getJobId())
                    .companyName(job.getCompanyName())
                    .jobCodeName(job.getJobCodeName())
                    .recruitNumber(job.getRecruitNumber())
                    .employmentType(job.getEmploymentType())
                    .workLocation(job.getWorkLocation())
                    .description(job.getDescription())
                    .wage(job.getWage())
                    .insurance(job.getInsurance())
                    .workTime(job.getWorkTime())
                    .managerPhone(job.getManagerPhone())
                    .jobTitle(job.getJobTitle())
                    .screeningMethod(job.getScreeningMethod())
                    .receptionMethod(job.getReceptionMethod())
                    .requiredDocuments(job.getRequiredDocuments())
                    .jobCategory(job.getJobCategory())
                    .postingDate(job.getPostingDate())
                    .closingDate(job.getClosingDate())
                    .imageUrl(job.getImageUrl())
                    .build();
        }

        public static AllResponse ofRecommend(Job job, Boolean isBookmark, Long score) {
            return AllResponse.builder()
                    .jobId(job.getJobId())
                    .companyName(job.getCompanyName())
                    .jobCodeName(job.getJobCodeName())
                    .recruitNumber(job.getRecruitNumber())
                    .employmentType(job.getEmploymentType())
                    .workLocation(job.getWorkLocation())
                    .description(job.getDescription())
                    .wage(job.getWage())
                    .insurance(job.getInsurance())
                    .workTime(job.getWorkTime())
                    .managerPhone(job.getManagerPhone())
                    .jobTitle(job.getJobTitle())
                    .screeningMethod(job.getScreeningMethod())
                    .receptionMethod(job.getReceptionMethod())
                    .requiredDocuments(job.getRequiredDocuments())
                    .jobCategory(job.getJobCategory())
                    .postingDate(job.getPostingDate())
                    .closingDate(job.getClosingDate())
                    .imageUrl(job.getImageUrl())
                    .isBookmark(isBookmark)
                    .score(score)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Response {

        private Long jobId;
        private String companyName;
        private String jobCodeName;
        private Integer recruitNumber;
        private String employmentType;
        private String workLocation;
        private String description;
        private String wage;
        private String insurance;
        private String workTime;
        private String managerPhone;
        private String jobTitle;
        private String screeningMethod;
        private String receptionMethod;
        private String requiredDocuments;
        private String jobCategory;
        private String postingDate;
        private String closingDate;
        private String imageUrl;
        private Boolean isScrap;

        public static Response of(Job job, Boolean isScrap) {
            return Response.builder()
                    .jobId(job.getJobId())
                    .companyName(job.getCompanyName())
                    .jobCodeName(job.getJobCodeName())
                    .recruitNumber(job.getRecruitNumber())
                    .employmentType(job.getEmploymentType())
                    .workLocation(job.getWorkLocation())
                    .description(job.getDescription())
                    .wage(job.getWage())
                    .insurance(job.getInsurance())
                    .workTime(job.getWorkTime())
                    .managerPhone(job.getManagerPhone())
                    .jobTitle(job.getJobTitle())
                    .screeningMethod(job.getScreeningMethod())
                    .receptionMethod(job.getReceptionMethod())
                    .requiredDocuments(job.getRequiredDocuments())
                    .jobCategory(job.getJobCategory())
                    .postingDate(job.getPostingDate())
                    .closingDate(job.getClosingDate())
                    .imageUrl(job.getImageUrl())
                    .isScrap(isScrap)
                    .build();
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class RecommendOccupationResponse {
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
            private String score;
            private Long memberOccupationId;
            private Boolean isBookmark;

            public static Occupation of(RecommendDto.OccupationResponse.Occupation o) {
                return Occupation.builder()
                        .imageUrl(o.getImageUrl())
                        .occupationName(o.getOccupationName())
                        .description(o.getDescription())
                        .strength(o.getStrength())
                        .score(o.getScore())
                        .memberOccupationId(o.getMemberOccupationId())
                        .isBookmark(o.getIsBookmark())
                        .build();
            }
        }
    }
}
