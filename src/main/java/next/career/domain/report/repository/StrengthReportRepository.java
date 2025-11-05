package next.career.domain.report.repository;

import next.career.domain.report.entity.StrengthReport;
import next.career.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StrengthReportRepository extends JpaRepository<StrengthReport, Long> {

    void deleteAllByMember(Member member);

    List<StrengthReport> findAllByMemberAndIsDeletedFalse(Member member);

    @Query("SELECT MAX(sr.version) FROM StrengthReport sr WHERE sr.member = :member")
    Optional<Integer> findMaxVersionByMember(@Param("member") Member member);
}
