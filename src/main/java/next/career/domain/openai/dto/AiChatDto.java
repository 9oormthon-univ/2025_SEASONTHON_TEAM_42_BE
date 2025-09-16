package next.career.domain.openai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    public static class HistoryResponse{

        private String job;

        private String experience;

        private String certificateOrSkill;

        private String personalityType;

        private String interests;

        private String preferredWorkStyles;

        private String avoidConditions;

        private String physicalCondition;

        private String educationAndCareerGoal;

        public static HistoryResponse of(MemberDetail memberDetail) {
            return HistoryResponse.builder()
                    .job(memberDetail.getJob())
                    .experience(memberDetail.getExperience())
                    .certificateOrSkill(memberDetail.getCertificateOrSkill())
                    .personalityType(memberDetail.getPersonalityType())
                    .interests(memberDetail.getInterests())
                    .preferredWorkStyles(memberDetail.getPreferredWorkStyles())
                    .avoidConditions(memberDetail.getAvoidConditions())
                    .physicalCondition(memberDetail.getPhysicalCondition())
                    .educationAndCareerGoal(memberDetail.getEducationAndCareerGoal())
                    .build();
        }

    }
}
