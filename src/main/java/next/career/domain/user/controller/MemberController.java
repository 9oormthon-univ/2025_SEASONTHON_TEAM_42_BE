package next.career.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.user.dto.response.MemberDetailResponse;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User")
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping
    public ApiResponse<MemberDetailResponse> getUserDetail(@AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.success(MemberDetailResponse.from(authDetails.getUser()));
    }
}
