package next.career.domain.roadmap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.user.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roadMapId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String period;
    private String category;
    private Boolean isCompleted;

    @OneToMany(mappedBy = "roadMap", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoadMapAction> actionList = new ArrayList<>();

    public void updateCompleted() {
        boolean allCompleted = !actionList.isEmpty() &&
                actionList.stream().allMatch(RoadMapAction::getIsCompleted);

        this.isCompleted = allCompleted;
    }
}
