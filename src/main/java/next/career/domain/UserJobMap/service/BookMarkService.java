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
     * @param companyId
     * @return List<UserCompanySubscription>
     */
    @Transactional
    public List<MemberJobMap> register(Long memberId, Long companyId) {
        if (repository.findByMemberIdAndJobId(memberId, companyId).isPresent()) {
            throw new MemberJobMapException(MemberJobMapErrorType.BOOKMARK_ALREADY_EXISTS);
        }

        try {
            repository.save(MemberJobMap.create(memberId, companyId));
        } catch (Exception e) {
            throw new MemberJobMapException(MemberJobMapErrorType.BOOKMARK_REGISTER_INTERNAL_ERROR);
        }

        return repository.findByUserId(memberId);
    }

    /**
     * 구독 해지
     * @param memberId
     * @param companyId
     * @return List<UserCompanySubscription>
     */
    @Transactional
    public List<MemberJobMap> unregister(Long memberId, Long companyId) {
        repository.findByMemberIdAndJobId(memberId, companyId)
                .ifPresent(subscribedCompany -> {
                    try {
                        repository.delete(subscribedCompany);
                    } catch (Exception e) {
                        throw new MemberJobMapException(MemberJobMapErrorType.BOOKMARK_CANCEL_INTERNAL_ERROR);
                    }
                });

        return repository.findByUserId(memberId);
    }
}
