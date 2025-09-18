package next.career.domain.education.controller.dto;

import lombok.*;
import next.career.domain.education.entity.Education;
import next.career.domain.education.service.dto.EducationDto;
import next.career.domain.education.service.dto.SaveWork24EducationDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class GetEducationDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchAllResponse {
        private Long totalElements;
        private Integer numberOfElements;
        private List<EducationDto.AllResponse> educationDtoList;

        public static GetEducationDto.SearchAllResponse of(Page<EducationDto.AllResponse> educationDtoList){
            return GetEducationDto.SearchAllResponse.builder()
                    .educationDtoList(educationDtoList.getContent())
                    .totalElements(educationDtoList.getTotalElements())
                    .numberOfElements(educationDtoList.getNumberOfElements())
                    .build();
        }

        public static GetEducationDto.SearchAllResponse of(Page<Education> educationPage, List<EducationDto.AllResponse> educationList) {
            return GetEducationDto.SearchAllResponse.builder()
                    .educationDtoList(educationList)
                    .totalElements(educationPage.getTotalElements())
                    .numberOfElements(educationPage.getNumberOfElements())
                    .build();
        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest{
        private String keyword;
        private String workLocation;
    }
}
