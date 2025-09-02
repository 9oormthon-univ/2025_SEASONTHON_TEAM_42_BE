package next.career.domain.job.controller.dto;

import lombok.*;
import next.career.domain.job.service.dto.JobDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class GetJobDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class AllResponse {
        private Long totalElements;
        private Integer numberOfElements;
        private List<JobDto.AllResponse> jobDtoList;

        public static AllResponse of(Page<JobDto.AllResponse> jobDtoList){
            return AllResponse.builder()
                    .jobDtoList(jobDtoList.getContent())
                    .totalElements(jobDtoList.getTotalElements())
                    .numberOfElements(jobDtoList.getNumberOfElements())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class SearchRequest{
        private String keyword;
        private String workLocation;
        private String employmentType;
        private String jobCategory;
    }
}
