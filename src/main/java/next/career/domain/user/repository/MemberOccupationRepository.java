package next.career.domain.user.repository;

import next.career.domain.user.entity.MemberOccupation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberOccupationRepository extends JpaRepository<MemberOccupation, Long> {
}
