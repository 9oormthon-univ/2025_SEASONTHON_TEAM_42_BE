package next.career.domain.job.entity;

import jakarta.persistence.*;
import lombok.Getter;
import next.career.domain.job.entity.embedded.CloseType;
import next.career.domain.job.entity.embedded.Company;
import next.career.domain.job.entity.embedded.Salary;
import next.career.domain.job.entity.embedded.Position;

import java.time.LocalDateTime;

@Entity
@Getter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;

    private String url;
    private boolean active;

    @Embedded
    private Company company;

    @Embedded
    private Position position;

    private String keyword;

    @Embedded
    private Salary salary;

    private LocalDateTime postingDate;
    private LocalDateTime modificationDate;
    private LocalDateTime openingDate;
    private LocalDateTime expirationDate;

    @Embedded
    private CloseType closeType;

    private Long readCount;
    private Long applyCount;

}
