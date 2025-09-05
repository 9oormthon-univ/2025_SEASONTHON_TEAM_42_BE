package next.career.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.user.entity.MemberDetail;

import java.util.List;

public class AiChatDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OptionResponse{
        private List<String> optionList;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class MemberDetailResponse{

        private String experience;

        private String certificateOrSkill;

        private String personalityType;

        private String interests;

        private String preferredWorkStyles;

        private String avoidConditions;

        private String availableWorkingTime;

        private String physicalCondition;

        private String educationAndCareerGoal;

        public static MemberDetailResponse of(MemberDetail memberDetail) {
            return MemberDetailResponse.builder()
                    .experience(memberDetail.getExperience())
                    .certificateOrSkill(memberDetail.getCertificateOrSkill())
                    .personalityType(memberDetail.getPersonalityType())
                    .interests(memberDetail.getInterests())
                    .preferredWorkStyles(memberDetail.getPreferredWorkStyles())
                    .avoidConditions(memberDetail.getAvoidConditions())
                    .availableWorkingTime(memberDetail.getAvailableWorkingTime())
                    .physicalCondition(memberDetail.getPhysicalCondition())
                    .educationAndCareerGoal(memberDetail.getEducationAndCareerGoal())
                    .build();
        }

    }
}
