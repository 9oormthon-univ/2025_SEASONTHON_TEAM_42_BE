package next.career.domain.roadmap.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RoadmapDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionUpdateRequest{
        private String action;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoadmapActionRecommendResponse{
        private List<String> recommendRoadmapActionList;

        public static RoadmapActionRecommendResponse of(List<String> recommendRoadmapActionList) {
            return RoadmapActionRecommendResponse.builder()
                    .recommendRoadmapActionList(recommendRoadmapActionList)
                    .build();
        }
    }
}
