package next.career.global.security;

import next.career.domain.user.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

public record AuthDetails(Member member, Map<String, Object> attributes, String nameAttributeKey) implements UserDetails, OAuth2User {

    // 폼 로그인 호환용 보조 생성자
    public AuthDetails(Member member) {
        this(member, Collections.emptyMap(), "id");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add(member.getMemberType().toString());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Member getUser() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //OAuth2User
    @Override public Map<String, Object> getAttributes() {
        return attributes == null ? Map.of() : Collections.unmodifiableMap(attributes);
    }

    @Override public String getName() {
        if (attributes != null && nameAttributeKey != null && attributes.containsKey(nameAttributeKey)) {
            return String.valueOf(attributes.get(nameAttributeKey));
        }
        return member.getId() != null ? String.valueOf(member.getId()) : member.getEmail();
    }

    public static AuthDetails of(Member member, Map<String,Object> attrs, String nameKey) {
        return new AuthDetails(member, attrs, nameKey);
    }
}

