package next.career.domain.user.dto.response;

import next.career.domain.user.entity.Member;

public record MemberDetailResponse (
        Long userId,

        String name,

        String socialProvider,

        String socialId,

        String email,

        String profileImage,

        MemberAdditionalInfo additionalInfo
) {
    public static MemberDetailResponse from(Member member) {
        return new MemberDetailResponse(
                member.getId(),
                member.getName(),
                member.getProvider(),
                member.getProviderId(),
                member.getEmail(),
                member.getProfileImageUrl(),
                MemberAdditionalInfo.from(member)
        );
    }
}
