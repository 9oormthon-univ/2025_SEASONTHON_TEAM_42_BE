package next.career.domain.user.dto.response;

import next.career.domain.user.entity.Address;
import next.career.domain.user.entity.Member;
import next.career.domain.user.enumerate.Gender;

import java.time.LocalDate;

public record MemberAdditionalInfo (
        LocalDate birthDate,

        Gender gender,

        Address address
) {
    public static MemberAdditionalInfo from(Member member) {
        return new MemberAdditionalInfo(
                member.getBirthDate(),
                member.getGender(),
                member.getAddress()
        );
    }
}
