package next.career.domain.UserEducationMap.repository;

import next.career.domain.UserEducationMap.entity.MemberEducationMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberEducationMapRepository extends JpaRepository<MemberEducationMap, Long> {

    List<MemberEducationMap> findByMemberId(Long memberId);

    Optional<MemberEducationMap> findByMemberIdAndEducationId(Long memberId, Long educationId);

    @Query("select s.educationId from MemberEducationMap s where s.memberId = :memberId")
    List<Long> findJobIdsByUserId(Long memberId);

    Boolean existsByMemberIdAndJobId(Long memberId, Long jobId);
}
