package next.career.domain.UserJobMap.repository;

import next.career.domain.UserJobMap.entity.MemberJobMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberJobMapRepository extends JpaRepository<MemberJobMap, Long> {

    List<MemberJobMap> findByMemberId(Long memberId);

    Optional<MemberJobMap> findByMemberIdAndJobId(Long memberId, Long jobId);

    @Query("select s.jobId from MemberJobMap s where s.memberId = :meberId")
    List<Long> findJobIdsByUserId(Long meberId);

    Boolean existsByMemberIdAndJobId(Long id, Long jobId);
}
