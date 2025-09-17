package next.career.domain.education.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.education.entity.Education;

import java.util.List;

public class EducationDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class AllResponse {

        private Long educationId;        //교육ID
        private String title;            //제목
        private String subTitle;         //교육기관
        private String traStartDate;     //훈련시작일
        private String traEndDate;       //훈련마감일
        private String address;          //주소
        private String courseMan;        //가격
        private String keyword1;         //키워드
        private String keyword2;         //키워드
        private String trprDegr;         //수강횟수
        private String imageUrl;         // 이미지 URL
        private Boolean isBookmark;      // 북마크 여부
        private Long score;              // 추천 점수 (optional)

        public static EducationDto.AllResponse of(Education education, Boolean isBookmark) {
            return AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .traEndDate(education.getTraEndDate())
                    .keyword1(education.getCertificate())
                    .keyword2(education.getTrainTarget())
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .imageUrl(education.getImageUrl())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .title(education.getTitle())
                    .isBookmark(isBookmark)
                    .build();
        }

        public static EducationDto.AllResponse ofAnonymous(Education education) {
            return AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .traEndDate(education.getTraEndDate())
                    .keyword1(education.getCertificate())
                    .keyword2(education.getTrainTarget())
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .imageUrl(education.getImageUrl())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .title(education.getTitle())
                    .build();
        }

        public static EducationDto.AllResponse ofRecommend(Education education, Boolean isBookmark, Long score) {
            return AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .traEndDate(education.getTraEndDate())
                    .keyword1(education.getCertificate())
                    .keyword2(education.getTrainTarget())
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .imageUrl(education.getImageUrl())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .title(education.getTitle())
                    .isBookmark(isBookmark)
                    .score(score)
                    .build();
        }
    }

    public static AllResponse fromWork24EducationDto(SaveWork24EducationDto.Work24EducationDto dto) {
        return AllResponse.builder()
                .title(dto.title())
                .subTitle(dto.subTitle())
                .traStartDate(dto.traStartDate())
                .traEndDate(dto.traEndDate())
                .address(dto.address())
                .courseMan(dto.courseMan())
                .keyword1(dto.certificate())
                .keyword2(dto.trainTarget())
                .trprDegr(dto.trprDegr())
                // 필요한 필드는 매핑하고, 나머지는 null 또는 적절한 기본값 설정
                .build();
    }
}
