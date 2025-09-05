package next.career.domain.roadmap.repository;

import next.career.domain.roadmap.entity.RoadMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadMapRepository extends JpaRepository<RoadMap, Long> {
}
