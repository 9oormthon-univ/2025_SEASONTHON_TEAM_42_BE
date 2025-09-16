package next.career.domain.UserEducationMap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.global.BaseTimeEntity;

@Entity
@Getter
@Table(
        name = "member_education_map",
        uniqueConstraints = @UniqueConstraint(name="uk_member_education", columnNames={"member_id","education_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEducationMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "job_id", nullable = false)
    private Long educationId;

    public MemberEducationMap(Long memberId, Long educationId) {
        this.memberId = memberId;
        this.educationId = educationId;
    }

    public static MemberEducationMap create(Long memberId, Long educationId) {
        return new MemberEducationMap(memberId, educationId);
    }
}
