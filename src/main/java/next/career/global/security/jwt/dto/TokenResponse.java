package next.career.global.security.jwt.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public static next.career.domain.user.dto.response.TokenResponse of(String accessToken, String refreshToken) {
        return next.career.domain.user.dto.response.TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
