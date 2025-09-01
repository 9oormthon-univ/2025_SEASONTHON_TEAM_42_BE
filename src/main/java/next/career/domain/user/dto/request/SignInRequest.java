package next.career.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SignInRequest(

        @Schema(description = "이메일", example = "ncareer@gmail.com")
        String email,

        @Schema(description = "비밀번호", example = "password123")
        String password
) {
}