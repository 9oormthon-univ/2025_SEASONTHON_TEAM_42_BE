package next.career.domain.roadmap.repository;

import next.career.domain.roadmap.entity.RoadmapInput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapInputRepository extends JpaRepository<RoadmapInput, Long> {
}
