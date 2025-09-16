package next.career.domain.UserJobMap.service;

import java.util.List;

public interface BookMarkFinder {
    List<Long> findBookMarkedJobs(Long userId);
}
