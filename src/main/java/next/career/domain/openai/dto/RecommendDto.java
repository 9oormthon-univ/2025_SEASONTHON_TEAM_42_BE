package next.career.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.roadmap.entity.RoadMap;
import next.career.domain.roadmap.entity.RoadmapInput;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
            private String strength;
            private String score;
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class RoadMapResponse {

        private RoadmapInputResponse roadmapInputResponse;
        private List<RoadMapStep> steps;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class RoadmapInputResponse {
            private Long dDay;
            private String career;
            private String period;
            private String experience;

            public static RoadmapInputResponse from(RoadmapInput input) {
                LocalDate now = LocalDate.now();
                long dDay = ChronoUnit.DAYS.between(input.getCreatedAt(), now);

                return RoadmapInputResponse.builder()
                        .dDay(dDay)
                        .career(input.getCareer())
                        .period(input.getPeriod())
                        .experience(input.getExperience())
                        .build();
            }
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class RoadMapStep {
            private Long roadMapId;
            private String period;
            private String category;
            private Boolean isCompleted;
            private List<ActionDto> actions;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Builder
        public static class ActionDto {
            private Long roadMapActionId;
            private String action;
            private Boolean isCompleted;
        }

        public static RoadMapResponse of(List<RoadMap> roadMaps, RoadmapInput roadmapInput) {
            return RoadMapResponse.builder()
                    .roadmapInputResponse(RoadmapInputResponse.from(roadmapInput))
                    .steps(
                            roadMaps.stream()
                                    .map(roadMap -> RoadMapStep.builder()
                                            .roadMapId(roadMap.getRoadMapId())
                                            .period(roadMap.getPeriod())
                                            .category(roadMap.getCategory())
                                            .isCompleted(roadMap.getIsCompleted())
                                            .actions(
                                                    roadMap.getActionList().stream()
                                                            .map(action -> ActionDto.builder()
                                                                    .roadMapActionId(action.getId())
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
