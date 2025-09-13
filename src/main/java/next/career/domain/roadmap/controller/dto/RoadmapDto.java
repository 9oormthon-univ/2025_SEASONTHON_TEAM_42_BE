package next.career.domain.roadmap.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoadmapDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class actionUpdateRequest{
        private String action;
    }
}
