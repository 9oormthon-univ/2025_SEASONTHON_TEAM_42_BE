package next.career.domain.report.controller;

import lombok.RequiredArgsConstructor;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.report.service.ReportService;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;


    @GetMapping("/strength")
    public ApiResponse<GetStrengthReportDto.Response> getStrengthReport(
            @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();

        return ApiResponse.success(reportService.createStrengthReport(member));
    }

}
