package next.career.domain.openai.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.user.entity.MemberDetail;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvertService {

    public String convertMemberDetailToText(MemberDetail memberDetail) {
        StringBuilder sb = new StringBuilder();

        if (memberDetail.getExperience() != null && !memberDetail.getExperience().isBlank()) {
            sb.append("경력/경험: ").append(memberDetail.getExperience()).append("\n");
        }
        if (memberDetail.getCertificateOrSkill() != null && !memberDetail.getCertificateOrSkill().isBlank()) {
            sb.append("자격증/기술: ").append(memberDetail.getCertificateOrSkill()).append("\n");
        }
        if (memberDetail.getPersonalityType() != null && !memberDetail.getPersonalityType().isBlank()) {
            sb.append("성격/강점: ").append(memberDetail.getPersonalityType()).append("\n");
        }
        if (memberDetail.getInterests() != null && !memberDetail.getInterests().isBlank()) {
            sb.append("관심 분야: ").append(memberDetail.getInterests()).append("\n");
        }
        if (memberDetail.getPreferredWorkStyles() != null && !memberDetail.getPreferredWorkStyles().isBlank()) {
            sb.append("희망 근무 스타일: ").append(memberDetail.getPreferredWorkStyles()).append("\n");
        }
        if (memberDetail.getAvoidConditions() != null && !memberDetail.getAvoidConditions().isBlank()) {
            sb.append("피하고 싶은 근무 조건: ").append(memberDetail.getAvoidConditions()).append("\n");
        }
        if (memberDetail.getAvailableWorkingTime() != null && !memberDetail.getAvailableWorkingTime().isBlank()) {
            sb.append("근무 가능 시간: ").append(memberDetail.getAvailableWorkingTime()).append("\n");
        }
        if (memberDetail.getPhysicalCondition() != null && !memberDetail.getPhysicalCondition().isBlank()) {
            sb.append("체력 상태: ").append(memberDetail.getPhysicalCondition()).append("\n");
        }
        if (memberDetail.getEducationAndCareerGoal() != null && !memberDetail.getEducationAndCareerGoal().isBlank()) {
            sb.append("교육 의향 및 취업 목표 기간: ").append(memberDetail.getEducationAndCareerGoal()).append("\n");
        }

        return sb.toString().trim();
    }

}
