package next.career.domain.UserEducationMap.service;

import java.util.List;

public interface EducationBookMarkFinder {

    List<Long> findBookMarkedJobs(Long userId);
}
