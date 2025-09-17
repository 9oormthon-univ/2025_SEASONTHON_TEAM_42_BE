package next.career.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.report.service.ReportService;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;


    @PostMapping("/strength")
    @Operation(
            summary = "강점 보고서 생성",
            description = "OpenAI API를 호출하여 사용자의 강점 보고서를 생성하고, 결과를 DB에 저장한 뒤 반환합니다."
    )
    public ApiResponse<GetStrengthReportDto.Response> createStrengthReport(
            @Parameter(hidden = true, description = "인증된 사용자 정보")
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(reportService.createStrengthReport(member));
    }

    @GetMapping("/strength")
    @Operation(
            summary = "강점 보고서 조회",
            description = "저장된 강점 보고서를 조회합니다. 반환되는 reportList에는 strength, experience, keyword, job 정보가 포함됩니다."
    )
    public ApiResponse<GetStrengthReportDto.Response> getStrengthReport(
            @Parameter(hidden = true, description = "인증된 사용자 정보")
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(reportService.getStrengthReport(member));
    }

}
