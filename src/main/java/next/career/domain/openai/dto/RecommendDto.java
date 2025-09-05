package next.career.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RecommendDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OccupationResponse{
        private List<String> occupationList;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class RoadMapResponse{

        private List<RoadMapStep> steps;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class RoadMapStep {
            private String period;          // 기간 (예: [1개월 이내])
            private String category;        // 카테고리 (예: 준비하기, 성장하기, 도전하기)
            private List<String> actions;   // 세부 활동 리스트
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class JobResponse {
        private String jobId;
        private String companyName;
        private String keyword;
        private String jobRecommendScore;
        private String closingDate;
        private String workLocation;
        private Boolean isScrap;

        public void isScrap(Boolean isScrap) {
            this.isScrap = isScrap;
        }
    }
}
