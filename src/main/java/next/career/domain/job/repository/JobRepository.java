package next.career.domain.job.repository;

import next.career.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END " +
            "FROM Job j " +
            "WHERE j.jobTitle = :jobTitle AND j.companyName = :companyName")
    boolean findAlreadyExists(String jobTitle, String companyName);
}
