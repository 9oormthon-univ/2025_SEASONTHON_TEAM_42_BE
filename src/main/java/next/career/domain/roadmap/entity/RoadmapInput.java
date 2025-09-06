package next.career.domain.roadmap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.user.entity.Member;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roadmapRequestId;

    @OneToOne(mappedBy = "roadmapInput", fetch = FetchType.EAGER)
    private Member member;

    private String career;

    private String period;

    private String experience;

    private LocalDate createdAt;

    public static RoadmapInput of(GetRoadMapDto.Request request, Member member) {
        return RoadmapInput.builder()
                .career(request.getCareer())
                .period(request.getPeriod())
                .experience(request.getExperience())
                .createdAt(LocalDate.now())
                .member(member)
                .build();
    }

}
