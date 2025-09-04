package next.career.domain.user.dto.request;

import next.career.domain.user.entity.Address;
import next.career.domain.user.enumerate.Gender;

import java.time.LocalDate;

public record MemberProfileUpdateRequest(
        String name,

        LocalDate birthDate,

        Gender gender,

        String city,

        String street
) {
    public Address toAddress() {
        return new Address(city, street);
    }
}
