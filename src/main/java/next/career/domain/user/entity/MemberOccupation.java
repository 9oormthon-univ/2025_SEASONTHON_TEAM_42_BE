package next.career.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberOccupationId;

    private String occupationName;

    private String occupationDescription;

    private String strength;

    private String score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberOccupation of(String occupationName, String occupationDescription, String strength, String score, Member member) {
        return MemberOccupation.builder()
                .occupationName(occupationName)
                .occupationDescription(occupationDescription)
                .strength(strength)
                .score(score)
                .member(member)
                .build();
    }
}
