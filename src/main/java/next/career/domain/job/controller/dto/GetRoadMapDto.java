package next.career.domain.job.controller.dto;

import lombok.*;

public class GetRoadMapDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Request{
        private String career;
        private String experience;
        private String period;
    }
}
