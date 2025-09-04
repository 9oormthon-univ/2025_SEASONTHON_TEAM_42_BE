package next.career.domain.user.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.user.entity.Credential;
import next.career.domain.user.entity.Member;
import next.career.domain.user.repository.MemberRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import next.career.domain.user.dto.request.AuthRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Credential을 생성하고 저장하는 메서드
     * @param credential
     * @param authRequest
     * @return Member
     */
    public Member createUser(Credential credential, AuthRequest authRequest) {
        memberRepository.findByEmail(authRequest.email()).ifPresent(existingUser -> {
            throw new CoreException(GlobalErrorType.MEMBER_ALREADY_EXISTS);
        });
        Member member = authRequest.toMember(credential);
        return memberRepository.save(member);
    }

    /**
     * 이메일로 사용자를 조회하는 메서드
     * @param email
     * @return Member
     */
    public Member getUserByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CoreException(GlobalErrorType.MEMBER_NOT_FOUND));
    }
}

