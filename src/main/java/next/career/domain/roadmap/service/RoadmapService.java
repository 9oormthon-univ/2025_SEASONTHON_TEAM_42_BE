package next.career.domain.roadmap.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.roadmap.controller.dto.RoadmapDto;
import next.career.domain.roadmap.entity.RoadMap;
import next.career.domain.roadmap.entity.RoadMapAction;
import next.career.domain.roadmap.entity.RoadmapInput;
import next.career.domain.roadmap.repository.RoadMapRepository;
import next.career.domain.roadmap.repository.RoadmapActionRepository;
import next.career.domain.user.entity.Member;
import next.career.domain.user.repository.MemberRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final MemberRepository memberRepository;
    private final OpenAiService openAiService;
    private final RoadMapRepository roadMapRepository;
    private final RoadmapActionRepository roadmapActionRepository;

    @Transactional
    public RecommendDto.RoadMapResponse recommendRoadMap(GetRoadMapDto.Request roadmapRequest, Member member) {
        member.getRoadMapList().clear();
        member.updateRoadmapInput(null);
        memberRepository.save(member);

        RoadmapInput roadmapInputSave = RoadmapInput.of(roadmapRequest, member);
        member.updateRoadmapInput(roadmapInputSave);
        memberRepository.save(member);

        RecommendDto.RoadMapResponse response = openAiService.getRecommendRoadMap(roadmapRequest, member);

        for (RecommendDto.RoadMapResponse.RoadMapStep step : response.getSteps()) {
            RoadMap roadMap = RoadMap.builder()
                    .member(member)
                    .period(step.getPeriod())
                    .category(step.getCategory())
                    .isCompleted(false)
                    .build();

            step.getActions().forEach(actionDto -> {
                RoadMapAction actionEntity = RoadMapAction.builder()
                        .action(actionDto.getAction())
                        .isCompleted(false)
                        .roadMap(roadMap)
                        .build();
                roadMap.getActionList().add(actionEntity);
            });

            member.getRoadMapList().add(roadMap);
        }

        memberRepository.save(member);

        return response;
    }

    @Transactional
    public RecommendDto.RoadMapResponse getRoadMap(Member member) {

        List<RoadMap> roadmapList = roadMapRepository.findAllByMember(member);

        RoadmapInput roadmapInput = member.getRoadmapInput();

        return RecommendDto.RoadMapResponse.of(roadmapList, roadmapInput);
    }

    @Transactional
    public void checkRoadMapAction(Long roadMapId, Long roadMapActionId, Member member) {
        RoadMapAction roadMapAction = roadmapActionRepository.findById(roadMapActionId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.ROAD_MAP_ACTION_NOT_FOUND));

        RoadMap roadMap = roadMapRepository.findById(roadMapId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.ROAD_MAP_NOT_FOUND));

        roadMap.updateCompleted();
        roadMapAction.updateCompleted();
    }

    @Transactional
    public void updateRoadmapAction(Long roadMapActionId, RoadmapDto.actionUpdateRequest request) {

        RoadMapAction roadMapAction = roadmapActionRepository.findById(roadMapActionId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.ROAD_MAP_ACTION_NOT_FOUND));

        roadMapAction.updateAction(request.getAction());
        roadMapAction.updateNotCompleted();

    }

    @Transactional
    public void deleteRoadmapAction(Long roadMapActionId) {
        roadmapActionRepository.deleteById(roadMapActionId);
    }
}
