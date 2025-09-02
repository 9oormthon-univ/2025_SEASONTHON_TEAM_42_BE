package next.career.domain.user.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.user.entity.Credential;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import next.career.domain.user.dto.request.AuthRequest;
import next.career.domain.user.dto.request.SignInRequest;
import next.career.domain.user.dto.response.MemberResponse;
import next.career.domain.user.dto.response.TokenResponse;
import next.career.global.config.redis.RedisClient;
import next.career.global.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final CredentialService credentialService;
    private final JwtProvider jwtProvider;
    private final RedisClient redisClient;

    @Value("${jwt.token.refresh-expiration-time}")
    private Long refreshTokenExpirationTime;

    /**
     * 회원가입 메서드
     * @param authRequest
     * @return MemberResponse
     */
    @Transactional
    public MemberResponse signUp(AuthRequest authRequest) {
        Credential credential = credentialService.createCredential(authRequest);
        Member member = userService.createUser(credential, authRequest);

        return MemberResponse.of(member);
    }

    /**
     * 로그인 메서드
     * @param signInRequest
     * @return TokenResponse
     */
    @Transactional
    public TokenResponse signIn(SignInRequest signInRequest) {
        Member member = userService.getUserByEmail(signInRequest.email());
        credentialService.checkPassword(member, signInRequest.password());

        TokenResponse tokenResponse = jwtProvider.createToken(member);
        redisClient.setValue(member.getEmail(), tokenResponse.refreshToken(), refreshTokenExpirationTime);

        return tokenResponse;
    }

    /**
     * 로그아웃 메서드
     * @param refreshToken
     * @param accessToken
     */
    @Transactional
    public void signOut(String refreshToken, String accessToken) {
        if(refreshToken==null || accessToken==null) throw new CoreException(GlobalErrorType.TOKEN_NOT_FOUND);
        if(!jwtProvider.validateToken(accessToken)) {
            throw new CoreException(GlobalErrorType.TOKEN_INVALID);
        }
        jwtProvider.invalidateTokens(refreshToken, accessToken);
    }

    /**
     * 토큰 재발급 메서드
     * @param token
     * @param member
     * @return tokenResponse
     */
    public TokenResponse recreate(String token, Member member) {
        if(token ==null) throw new CoreException(GlobalErrorType.TOKEN_NOT_FOUND);
        if (member == null) throw new CoreException(GlobalErrorType.MEMBER_NOT_FOUND);

        String refreshToken = token.substring(7);
        boolean isValid = jwtProvider.validateToken(refreshToken);

        if (!isValid) throw new CoreException(GlobalErrorType.TOKEN_INVALID);

        String email = jwtProvider.getEmail(refreshToken);
        String redisRefreshToken = redisClient.getValue(email);

        if (refreshToken.isEmpty() || redisRefreshToken.isEmpty() || !redisRefreshToken.equals(refreshToken)) {
            throw new CoreException(GlobalErrorType.TOKEN_NOT_FOUND);
        }

        return jwtProvider.recreate(member, refreshToken);
    }
}

