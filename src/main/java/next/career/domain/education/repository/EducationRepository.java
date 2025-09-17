package next.career.domain.education.repository;

import next.career.domain.education.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EducationRepository extends JpaRepository<Education, Long> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Education e " +
            "WHERE e.title = :edcationTitle AND e.subTitle = :companyName")
    boolean findAlreadyExists(String edcationTitle, String companyName);
}
