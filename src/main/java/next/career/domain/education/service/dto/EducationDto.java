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
                    .keyword1(education.getCertificate() != null && education.getCertificate().length() <= 10 ? education.getCertificate() : null)
                    .keyword2(education.getTrainTarget() != null && education.getTrainTarget().length() <= 10 ? education.getTrainTarget() : null)
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .imageUrl(getRandomImageUrl())
                    .title(education.getTitle())
                    .isBookmark(isBookmark)
                    .build();
        }

        public static EducationDto.AllResponse ofAnonymous(Education education) {
            return AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .traEndDate(education.getTraEndDate())
                    .keyword1(education.getCertificate() != null && education.getCertificate().length() <= 10 ? education.getCertificate() : null)
                    .keyword2(education.getTrainTarget() != null && education.getTrainTarget().length() <= 10 ? education.getTrainTarget() : null)
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .imageUrl(education.getImageUrl())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .imageUrl(getRandomImageUrl())
                    .title(education.getTitle())
                    .build();
        }

        public static EducationDto.AllResponse ofRecommend(Education education, Boolean isBookmark, Long score) {
            return AllResponse.builder()
                    .traStartDate(education.getTraStartDate())
                    .educationId(education.getEducationId())
                    .traEndDate(education.getTraEndDate())
                    .keyword1(education.getCertificate() != null && education.getCertificate().length() <= 10 ? education.getCertificate() : null)
                    .keyword2(education.getTrainTarget() != null && education.getTrainTarget().length() <= 10 ? education.getTrainTarget() : null)
                    .courseMan(education.getCourseMan())
                    .subTitle(education.getSubTitle())
                    .trprDegr(education.getTrprDegr())
                    .address(education.getAddress())
                    .imageUrl(getRandomImageUrl())
                    .title(education.getTitle())
                    .isBookmark(isBookmark)
                    .score(score)
                    .build();
        }
    }

    public static AllResponse fromWork24EducationDto(SaveWork24EducationDto.Work24EducationDto dto) {
        return AllResponse.builder()
                .traStartDate(dto.traStartDate())
                .imageUrl(getRandomImageUrl())
                .traEndDate(dto.traEndDate())
                .keyword1(dto.certificate() != null && dto.certificate().length() <= 10 ? dto.certificate() : null)
                .keyword2(dto.trainTarget() != null && dto.trainTarget().length() <= 10 ? dto.trainTarget() : null)
                .courseMan(dto.courseMan())
                .subTitle(dto.subTitle())
                .trprDegr(dto.trprDegr())
                .address(dto.address())
                .title(dto.title())
                .isBookmark(false)
                .build();
    }

    private static String getRandomImageUrl() {
        List<String> imageUrls = List.of(
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/star1.png",
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/star2.png",
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/star3.png",
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/star4.png"
        );
        int randomIndex = (int) (Math.random() * imageUrls.size());
        return imageUrls.get(randomIndex);
    }
}
