package next.career.domain.job.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 DB PK

    private String externalId; // API에서 제공하는 id (예: 27614114)

    private String url;
    private boolean active;

    // 회사 정보
    @Embedded
    private Company company;

    // 포지션 정보
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
