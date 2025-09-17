package next.career.domain.report.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.user.entity.Member;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrengthReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strengthReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String strength;

    private String experience;

    @ElementCollection
    private List<String> keyword;

    @ElementCollection
    private List<String> job;

    public static StrengthReport of(Member member, String strength, String experience,
                                    List<String> keyword, List<String> job) {
        return StrengthReport.builder()
                .member(member)
                .strength(strength)
                .experience(experience)
                .keyword(keyword)
                .job(job)
                .build();
    }
}
