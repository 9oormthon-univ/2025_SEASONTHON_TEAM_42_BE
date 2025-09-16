package next.career.domain.UserEducationMap.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.UserEducationMap.entity.MemberEducationMap;
import next.career.domain.UserEducationMap.exception.MemberEducationMapErrorType;
import next.career.domain.UserEducationMap.exception.MemberEducationMapException;
import next.career.domain.UserEducationMap.repository.MemberEducationMapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationBookMarkService {

    private final MemberEducationMapRepository repository;

    /**
     * 구독 등록
     * @param memberId
     * @param educationId
     * @return List<UserCompanySubscription>
     */
    @Transactional
    public List<MemberEducationMap> register(Long memberId, Long educationId) {
        if (repository.findByMemberIdAndEducationId(memberId, educationId).isPresent()) {
            throw new MemberEducationMapException(MemberEducationMapErrorType.BOOKMARK_ALREADY_EXISTS);
        }
        try {
            repository.save(MemberEducationMap.create(memberId, educationId));
        } catch (Exception e) {
            throw new MemberEducationMapException(MemberEducationMapErrorType.BOOKMARK_REGISTER_INTERNAL_ERROR);
        }

        return repository.findByMemberId(memberId);
    }

    /**
     * 구독 해지
     * @param memberId
     * @param educationId
     * @return List<UserCompanySubscription>
     */
    @Transactional
    public List<MemberEducationMap> unregister(Long memberId, Long educationId) {
        repository.findByMemberIdAndEducationId(memberId, educationId)
                .ifPresent(subscribedEducation -> {
                    try {
                        repository.delete(subscribedEducation);
                    } catch (Exception e) {
                        throw new MemberEducationMapException(MemberEducationMapErrorType.BOOKMARK_CANCEL_INTERNAL_ERROR);
                    }
                });

        return repository.findByMemberId(memberId);
    }
}
