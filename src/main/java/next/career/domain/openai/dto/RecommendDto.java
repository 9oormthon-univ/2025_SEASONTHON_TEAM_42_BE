package next.career.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.roadmap.entity.RoadMap;

import java.util.List;

public class RecommendDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OccupationResponse{

        private List<Occupation> occupationList;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class Occupation{
            private String imageUrl;
            private String occupationName;
            private String description;
            private String score;
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class RoadMapResponse {

        private List<RoadMapStep> steps;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class RoadMapStep {
            private String period;
            private String category;
            private Boolean isCompleted; // 단계 완료 여부
            private List<ActionDto> actions; // 문자열 대신 객체 리스트
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class ActionDto {
            private String action;
            private Boolean isCompleted;
        }

        public static RoadMapResponse of(List<RoadMap> roadMaps) {
            return RoadMapResponse.builder()
                    .steps(
                            roadMaps.stream()
                                    .map(roadMap -> RoadMapStep.builder()
                                            .period(roadMap.getPeriod())
                                            .category(roadMap.getCategory())
                                            .isCompleted(roadMap.getIsCompleted())
                                            .actions(
                                                    roadMap.getActionList().stream()
                                                            .map(action -> ActionDto.builder()
                                                                    .action(action.getAction())
                                                                    .isCompleted(action.getIsCompleted())
                                                                    .build()
                                                            )
                                                            .toList()
                                            )
                                            .build()
                                    )
                                    .toList()
                    )
                    .build();
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
        private Boolean isBookmark;

        public void isBookmark(Boolean isBookmark) {
            this.isBookmark = isBookmark;
        }
    }
}
