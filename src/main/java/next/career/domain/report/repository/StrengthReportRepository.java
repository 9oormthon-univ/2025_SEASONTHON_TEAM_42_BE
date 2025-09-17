package next.career.domain.report.repository;

import next.career.domain.report.entity.StrengthReport;
import next.career.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StrengthReportRepository extends JpaRepository<StrengthReport, Long> {

    void deleteAllByMember(Member member);

    List<StrengthReport> findAllByMember(Member member);
}
