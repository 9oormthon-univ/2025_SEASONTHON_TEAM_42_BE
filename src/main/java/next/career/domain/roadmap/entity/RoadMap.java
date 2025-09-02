package next.career.domain.roadmap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roadMapId;

    //TODO : user랑 다대일 연관관계
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false) // FK
//    private User user;

    private String content;

    @Builder.Default()
    private Boolean isCompleted = false;

    public RoadMap of(String content) {
        return RoadMap.builder()
                .content(content)
                .build();
    }
}
