package next.career.domain.education.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.education.entity.Education;

public class EducationDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AllResponse {

        private Long educationId;        //교육ID
        private String certificate;      //키워드
        private String title;            //제목
        private String subTitle;         //교육기관
        private String traStartDate;     //훈련시작일
        private String traEndDate;       //훈련마감일
        private String address;          //주소
        private String courseMan;        //가격
        private String trainTarget;      //키워드
        private String trprDegr;         //수강횟수
        private Boolean isBookmark;      // 북마크 여부
        private Long score;              // 추천 점수 (optional)

        public static EducationDto.AllResponse of(Education education, Boolean isBookmark) {
            return AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .certificate(education.getCertificate())
                    .trainTarget(education.getTrainTarget())
                    .traEndDate(education.getTraEndDate())
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .title(education.getTitle())
                    .isBookmark(isBookmark)
                    .build();
        }

        public static EducationDto.AllResponse ofAnonymous(Education education) {
            return EducationDto.AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .certificate(education.getCertificate())
                    .trainTarget(education.getTrainTarget())
                    .traEndDate(education.getTraEndDate())
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .title(education.getTitle())
                    .build();
        }

        public static EducationDto.AllResponse ofRecommend(Education education, Boolean isBookmark, Long score) {
            return EducationDto.AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .certificate(education.getCertificate())
                    .trainTarget(education.getTrainTarget())
                    .traEndDate(education.getTraEndDate())
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .title(education.getTitle())
                    .isBookmark(isBookmark)
                    .score(score)
                    .build();
        }
    }
}
