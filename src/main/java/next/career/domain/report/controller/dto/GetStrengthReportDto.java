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
        private String occupation;

        public static Response of(List<Report> reportList, String occupation){
            return Response.builder()
                    .occupation(occupation)
                    .reportList(reportList)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Report{
        private Long strengthReportId;
        private String strength;
        private String experience;
        private List<String> keyword;
        private List<String> job;
        private String appeal;

        public static Report of(Long reportId, String strength, String experience, List<String> keyword, List<String> job, String appeal){
            return Report.builder()
                    .strengthReportId(reportId)
                    .strength(strength)
                    .experience(experience)
                    .keyword(keyword)
                    .job(job)
                    .appeal(appeal)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest{
        private String strength;
        private String experience;
        private List<String> keyword;
        private String appeal;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportRequest {
        private String occupation;
    }
}
