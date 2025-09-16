package next.career.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDetailId;

    @OneToOne(mappedBy = "memberDetail", fetch = FetchType.EAGER)
    private Member member;

    private String job;

    private String experience;

    private String certificateOrSkill;

    private String workingStyle;

    private String avoidConditions;

    private String personalityType;

    private String interests;

    private String preferredWorkStyles;

    private String physicalCondition;

    private String educationAndCareerGoal;

    public static MemberDetail createOf(Member member) {
        return MemberDetail.builder()
                .member(member)
                .build();
    }

    public void updateExperience(String experience) {
        this.experience = experience;
    }

    public void updateCertificateOrSkill(String certificateOrSkill) {
        this.certificateOrSkill = certificateOrSkill;
    }

    public void updatePersonalityType(String personalityType) {
        this.personalityType = personalityType;
    }

    public void updateInterests(String interests) {
        this.interests = interests;
    }

    public void updatePreferredWorkStyles(String preferredWorkStyles) {
        this.preferredWorkStyles = preferredWorkStyles;
    }

    public void updateAvoidConditions(String avoidConditions) {
        this.avoidConditions = avoidConditions;
    }

    public void updateAvailableWorkingTime(String availableWorkingTime) {
        this.availableWorkingTime = availableWorkingTime;
    }

    public void updatePhysicalCondition(String physicalCondition) {
        this.physicalCondition = physicalCondition;
    }

    public void updateEducationAndCareerGoal(String educationAndCareerGoal) {
        this.educationAndCareerGoal = educationAndCareerGoal;
    }

    public void updateWorkingStyle(String workingStyle) {
        this.workingStyle = workingStyle;
    }
}
