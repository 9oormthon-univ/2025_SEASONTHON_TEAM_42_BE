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
        private List<String> roadMapList;
    }
}
