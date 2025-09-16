package next.career.domain.UserEducationMap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.UserEducationMap.entity.MemberEducationMap;
import next.career.domain.UserEducationMap.service.EducationBookMarkService;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "BookMark API")
@RequestMapping("/v1/bookmark/edu")
@RequiredArgsConstructor
public class EducationBookMarkController {

    private final EducationBookMarkService bookMarkService;

    @PostMapping
    @Operation(summary = "교육 북마크 등록", description = "마음에 드는 교육에 대해서 북마크를 등록합니다.")
    public ApiResponse<List<MemberEducationMap>> bookmarkJob(@AuthenticationPrincipal AuthDetails authDetails,
                                                             @RequestParam Long jobId) {
        return ApiResponse.success(bookMarkService.register(authDetails.getMemberId(), jobId));
    }

    @DeleteMapping
    @Operation(summary = "교육 북마크 취소", description = "마음에 들지 않는 교육에 대해서 북마크를 취소합니다.")
    public ApiResponse<List<MemberEducationMap>> cancelBookmark(@AuthenticationPrincipal AuthDetails authDetails,
                                                                @RequestParam Long jobId) {
        return ApiResponse.success(bookMarkService.unregister(authDetails.getMemberId(), jobId));
    }
}
