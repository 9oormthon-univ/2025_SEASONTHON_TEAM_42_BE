package next.career.domain.report.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetStrengthReportDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private List<Report> reportList;

        public static Response of(List<Report> reportList){
            return Response.builder()
                    .reportList(reportList)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Report{
        private String strength;
        private String experience;
        private List<String> keyword;
        private List<String> job;

        public static Report of(String strength, String experience, List<String> keyword, List<String> job){
            return Report.builder()
                    .strength(strength)
                    .experience(experience)
                    .keyword(keyword)
                    .job(job)
                    .build();
        }
    }
}
