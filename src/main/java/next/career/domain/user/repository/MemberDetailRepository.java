package next.career.domain.user.repository;

import next.career.domain.user.entity.Member;
import next.career.domain.user.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

}
