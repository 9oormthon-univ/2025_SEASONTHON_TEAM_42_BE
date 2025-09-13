package next.career.domain.roadmap.repository;

import next.career.domain.roadmap.entity.RoadMap;
import next.career.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadMapRepository extends JpaRepository<RoadMap, Long> {
    List<RoadMap> findAllByMember(Member member);

    List<RoadMap> findAllByCategory(String category);
}
