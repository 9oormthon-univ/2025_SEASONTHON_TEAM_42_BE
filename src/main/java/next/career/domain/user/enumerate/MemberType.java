package next.career.domain.user.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import next.career.domain.user.entity.Member;

@Getter
@RequiredArgsConstructor
public enum MemberType {
    GENERAL("일반 사용자"),
    ADMIN("관리자");

    private final String role;
}