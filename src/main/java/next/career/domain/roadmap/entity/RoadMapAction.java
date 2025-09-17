package next.career.domain.roadmap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadMapAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private Boolean isCompleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "road_map_id")
    private RoadMap roadMap;

    public void updateCompleted() {
        this.isCompleted = (this.isCompleted == null) ? Boolean.TRUE : !this.isCompleted;
    }

    public void updateAction(String action){
        this.action = action;
    }

    public void updateNotCompleted() {
        this.isCompleted = false;
    }

    public static RoadMapAction of(String action, RoadMap roadMap) {
        return RoadMapAction.builder()
                .action(action)
                .isCompleted(false)
                .roadMap(roadMap)
                .build();
    }
}
