package next.career.domain.UserEducationMap.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.UserEducationMap.repository.MemberEducationMapRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EducationBookMarkQueryService implements EducationBookMarkFinder {

    private final MemberEducationMapRepository memberJobMapRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Long> findBookMarkedJobs(Long userId) {
        return memberJobMapRepository.findJobIdsByUserId(userId);
    }
}
