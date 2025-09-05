package next.career.domain.UserJobMap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.user.entity.Member;
import next.career.global.BaseTimeEntity;

@Entity
@Getter
@Table(
        name = "member_job_map",
        uniqueConstraints = @UniqueConstraint(name="uk_member_job", columnNames={"member_id","job_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberJobMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    public MemberJobMap(Long memberId, Long JobId) {
        this.memberId = memberId;
        this.jobId = JobId;
    }

    public static MemberJobMap create(Long memberId, Long JobId) {
        return new MemberJobMap(memberId, JobId);
    }
}
