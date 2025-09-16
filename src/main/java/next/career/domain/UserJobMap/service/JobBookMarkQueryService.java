package next.career.domain.UserJobMap.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.UserJobMap.repository.MemberJobMapRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobBookMarkQueryService implements JobBookMarkFinder {

    private final MemberJobMapRepository memberJobMapRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Long> findBookMarkedJobs(Long userId) {
        return memberJobMapRepository.findJobIdsByUserId(userId);
    }
}
