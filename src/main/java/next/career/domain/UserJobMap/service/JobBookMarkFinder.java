package next.career.domain.UserJobMap.service;

import java.util.List;

public interface JobBookMarkFinder {
    List<Long> findBookMarkedJobs(Long userId);
}
