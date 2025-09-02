package next.career.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.user.dto.response.TokenResponse; // 기존 AuthService에서 쓰는 타입과 동일
import next.career.global.security.AuthDetails;
import next.career.global.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Value("${app.oauth2.success-redirect:/}")
    private String successRedirectBase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException {
        log.info("OAuth2 Success Handler");
        log.info("Request: {}", req);
        log.info("Authentication: {}", auth);
        log.info("Response: {}", res);
        AuthDetails principal = (AuthDetails) auth.getPrincipal();

        TokenResponse tokens = jwtProvider.createToken(principal.getUser());

        String access = tokens.accessToken();
        String refresh = tokens.refreshToken();

        String redirect = successRedirectBase
                + "?access="  + URLEncoder.encode(access, StandardCharsets.UTF_8)
                + "&refresh=" + URLEncoder.encode(refresh, StandardCharsets.UTF_8);

        res.sendRedirect(redirect);
    }
}
