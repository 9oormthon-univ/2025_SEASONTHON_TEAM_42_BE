package next.career.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import next.career.domain.user.entity.Credential;
import next.career.domain.user.entity.Member;

@Builder
public record AuthRequest(

        @Schema(description = "이메일", example = "ncareer@gmail.com")
        String email,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "비밀번호", example = "password123")
        String password
) {
    public Member toMember(Credential credential) {
        return Member.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .credential(credential)
                .build();
    }
}