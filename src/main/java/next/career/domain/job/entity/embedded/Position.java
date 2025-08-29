package next.career.domain.job.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
class Position {
    private String title;
    private String industryCode;
    private String industryName;
    private String locationCode;
    private String locationName;
    private String jobTypeCode;
    private String jobTypeName;
    private String jobMidCode;
    private String jobMidName;
    private String jobCode;
    private String jobName;
    private String experienceName;
    private Integer experienceMin;
    private Integer experienceMax;
    private String educationCode;
    private String educationName;
}
