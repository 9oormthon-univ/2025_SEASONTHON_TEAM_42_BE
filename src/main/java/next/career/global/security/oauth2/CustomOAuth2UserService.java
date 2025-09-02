package next.career.global.security.oauth2;

import lombok.RequiredArgsConstructor;
import next.career.domain.user.entity.Member;
import next.career.domain.user.enumerate.MemberType;
import next.career.domain.user.repository.UserRepository;
import next.career.global.security.AuthDetails;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(req);
        var p = OAuth2Attributes.parse(req, oAuth2User);

        // provider+providerId 우선, 없으면 email로 조회
        Optional<Member> found = userRepository.findByProviderAndProviderId(p.provider(), p.providerId());
        if (found.isEmpty() && p.email() != null) {
            found = userRepository.findByEmail(p.email());
        }

        Member member = found.orElseGet(() ->
                userRepository.save(Member.newSocial(p.email(), p.provider(), p.providerId(), null, MemberType.GENERAL))
        );

        // 필요 시 프로필 동기화 로직 추가 가능 (이름/이미지 등)
        return AuthDetails.of(member, oAuth2User.getAttributes(), p.nameAttributeKey());
    }
}
