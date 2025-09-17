package next.career.domain.UserJobMap.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.UserJobMap.entity.MemberJobMap;
import next.career.domain.UserJobMap.exception.MemberJobMapErrorType;
import next.career.domain.UserJobMap.exception.MemberJobMapException;
import next.career.domain.UserJobMap.repository.MemberJobMapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final MemberJobMapRepository repository;

    /**
     * 구독 등록
     * @param memberId
     * @param JobId
     * @return List<UserCompanySubscription>
     */
    @Transactional
    public List<MemberJobMap> register(Long memberId, Long JobId) {
        if (repository.findByMemberIdAndJobId(memberId, JobId).isPresent()) {
            throw new MemberJobMapException(MemberJobMapErrorType.BOOKMARK_ALREADY_EXISTS);
        }

        try {
            repository.save(MemberJobMap.create(memberId, JobId));
        } catch (Exception e) {
            throw new MemberJobMapException(MemberJobMapErrorType.BOOKMARK_REGISTER_INTERNAL_ERROR);
        }

        return repository.findByMemberId(memberId);
    }

    /**
     * 구독 해지
     * @param memberId
     * @param JobId
     * @return List<UserCompanySubscription>
     */
    @Transactional
    public List<MemberJobMap> unregister(Long memberId, Long JobId) {
        repository.findByMemberIdAndJobId(memberId, JobId)
                .ifPresent(subscribedJob -> {
                    try {
                        repository.delete(subscribedJob);
                    } catch (Exception e) {
                        throw new MemberJobMapException(MemberJobMapErrorType.BOOKMARK_CANCEL_INTERNAL_ERROR);
                    }
                });

        return repository.findByMemberId(memberId);
    }
}
