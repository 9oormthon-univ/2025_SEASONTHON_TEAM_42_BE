package next.career.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.report.service.ReportService;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
@Tag(name = "Report", description = "강점 리포트 생성 및 조회 관련 API")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/strength")
    @Operation(
            summary = "강점 리포트 생성",
            description = """
                    OpenAI API를 호출하여 사용자의 강점 리포트를 새로 생성합니다.  
                    생성된 리포트는 DB에 저장되며, 응답 본문에는 `strength`, `experience`, `appeal`, `keyword`, `job` 필드가 포함됩니다.
                    """
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
            summary = "강점 리포트 조회",
            description = """
                    사용자의 최신 강점 리포트를 조회합니다.  
                    반환되는 `reportList`에는 `strength`, `experience`, `appeal`, `keyword`, `job` 정보가 포함됩니다.
                    """
    )
    public ApiResponse<GetStrengthReportDto.Response> getStrengthReport(
            @Parameter(hidden = true, description = "인증된 사용자 정보")
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(reportService.getStrengthReport(member));
    }

    @GetMapping("/strength/history")
    @Operation(
            summary = "강점 리포트 히스토리 조회",
            description = """
                    사용자가 생성한 강점 리포트의 전체 히스토리를 조회합니다.  
                    각 버전별(`version`)로 생성된 리포트를 반환하며,  
                    각 리포트에는 `strength`, `experience`, `appeal`, `keyword`, `job` 정보가 포함됩니다.
                    """
    )
    public ApiResponse<GetStrengthReportDto.Response> getStrengthReportHistory(
            @Parameter(hidden = true, description = "인증된 사용자 정보")
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(reportService.getStrengthReportCurrentHistory(member));
    }

    @DeleteMapping("/strength/{strengthReportId}")
    @Operation(
            summary = "강점 리포트 삭제",
            description = """
                    특정 강점 리포트를 삭제 처리합니다.  
                    `strengthReportId`에 해당하는 리포트의 `isDeleted` 필드를 `true`로 변경하여  
                    논리적으로 삭제합니다.
                    """
    )
    public ApiResponse<?> deleteStrengthReport(
            @Parameter(hidden = true, description = "인증된 사용자 정보")
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "삭제할 강점 리포트의 ID", example = "1")
            @PathVariable Long strengthReportId
    ) {
        Member member = authDetails.getUser();
        reportService.deleteStrengthReport(member, strengthReportId);
        return ApiResponse.success();
    }

    @PatchMapping("/strength/{strengthReportId}")
    public ApiResponse<?> updateStrengthReport(
            @Parameter(hidden = true, description = "인증된 사용자 정보")
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "복원할 강점 리포트의 ID", example = "1")
            @PathVariable Long strengthReportId,
            @RequestBody GetStrengthReportDto.UpdateRequest request
    ) {
        Member member = authDetails.getUser();
        reportService.updateStrengthReport(member, strengthReportId, request);
        return ApiResponse.success();
    }
}
