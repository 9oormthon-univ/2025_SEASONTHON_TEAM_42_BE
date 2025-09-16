package next.career.domain.user.repository;

import next.career.domain.user.entity.Member;
import next.career.domain.user.entity.MemberOccupation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberOccupationRepository extends JpaRepository<MemberOccupation, Long> {
    List<MemberOccupation> findByMember(Member member);
}
