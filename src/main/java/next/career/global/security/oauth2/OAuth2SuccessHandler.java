package next.career.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import next.career.domain.user.dto.response.TokenResponse; // 기존 AuthService에서 쓰는 타입과 동일
import next.career.global.security.AuthDetails;
import next.career.global.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    // 성공 후 토큰을 전달할 프론트 콜백 URL (예: http://localhost:3000/oauth2/success)
    @Value("${app.oauth2.success-redirect:/}")
    private String successRedirectBase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException {

        AuthDetails principal = (AuthDetails) auth.getPrincipal();

        // 기존 규격 그대로: JwtProvider.createToken(member) -> TokenResponse(AT/RT)
        TokenResponse tokens = jwtProvider.createToken(principal.getUser());

        String access = invokeGetter(tokens, "getAccessToken");
        String refresh = invokeGetter(tokens, "getRefreshToken");

        String redirect = successRedirectBase
                + "?access="  + URLEncoder.encode(access, StandardCharsets.UTF_8)
                + "&refresh=" + URLEncoder.encode(refresh, StandardCharsets.UTF_8);

        res.sendRedirect(redirect);
    }

    private String invokeGetter(Object obj, String method) {
        try { return (String) obj.getClass().getMethod(method).invoke(obj); }
        catch (Exception e) { return ""; }
    }
}
