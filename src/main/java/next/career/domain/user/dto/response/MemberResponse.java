package next.career.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import next.career.domain.user.entity.Member;
import next.career.domain.user.enumerate.MemberType;

@Builder
public record MemberResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "ncareer@gmail.com")
        String email,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "회원 타입", example = "GENERAL | ADMIN")
        MemberType memberType
) {
    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType())
                .build();
    }
}
