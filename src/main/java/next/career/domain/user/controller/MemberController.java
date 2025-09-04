package next.career.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.user.dto.request.MemberProfileUpdateRequest;
import next.career.domain.user.dto.response.MemberDetailResponse;
import next.career.domain.user.entity.Member;
import next.career.domain.user.service.MemberService;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User")
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 정보 조회", description = "회원 정보 조회(AT 필요함)")
    public ApiResponse<MemberDetailResponse> getUserDetail(@AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.success(MemberDetailResponse.from(authDetails.getUser()));
    }

    @PostMapping("/profile")
    @Operation(summary = "소셜 로그인 후 추가정보 입력", description = "카카오 회원가입한 기반으로, 나머지 정보 업데이트(AT 필요함)")
    public ApiResponse<MemberDetailResponse> updateProfile(@AuthenticationPrincipal AuthDetails authDetails,
                                                           @RequestBody MemberProfileUpdateRequest request) {
        Member member = memberService.updateUser(authDetails.getUser(), request);
        return ApiResponse.success(MemberDetailResponse.from(member));
    }
}
