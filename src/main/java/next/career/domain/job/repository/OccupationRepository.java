package next.career.domain.job.repository;

import next.career.domain.job.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    @Query("""
    SELECT COALESCE(
        (SELECT o.imageUrl FROM Occupation o WHERE o.occupationName = :occupationName),
        (SELECT o.imageUrl FROM Occupation o WHERE o.occupationName = '기본 직업')
    )
    """)
    String findImageUrlByOccupationName(String occupationName);
}
