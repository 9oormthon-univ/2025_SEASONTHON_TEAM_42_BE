package next.career.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberOccupationId;

    private String occupationName;

    private String occupationDescription;

    private String strength;

    private String score;

    public static MemberOccupation of(String occupationName, String occupationDescription, String strength, String score) {
        return MemberOccupation.builder()
                .occupationName(occupationName)
                .occupationDescription(occupationDescription)
                .strength(strength)
                .score(score)
                .build();
    }
}
