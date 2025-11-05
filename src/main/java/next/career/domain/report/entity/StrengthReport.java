package next.career.domain.report.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.user.entity.Member;
import org.hibernate.annotations.Type;

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

    @Column(columnDefinition = "TEXT")
    private String appeal;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> keyword;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> job;

    private int version;

    public static StrengthReport of(Member member, String strength, String experience,
                                    List<String> keyword, List<String> job, String appeal, int version) {
        return StrengthReport.builder()
                .member(member)
                .strength(strength)
                .experience(experience)
                .keyword(keyword)
                .job(job)
                .appeal(appeal)
                .version(version)
                .build();
    }
}
