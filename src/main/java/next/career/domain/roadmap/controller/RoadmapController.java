package next.career.domain.roadmap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.job.service.JobService;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.roadmap.controller.dto.RoadmapDto;
import next.career.domain.roadmap.service.RoadmapService;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Roadmap API", description = "커리어 로드맵 관련 API")
@RequestMapping("/roadmap")
public class RoadmapController {

    private final JobService jobService;
    private final RoadmapService roadmapService;

    // 맞춤형 로드맵 추천
    @PostMapping("/recommend")
    @Operation(summary = "맞춤형 로드맵 추천", description = "사용자의 이력 및 관심 직무를 기반으로 커리어 로드맵을 추천합니다.")
    public ApiResponse<RecommendDto.RoadMapResponse> recommendRoadMap(
            @RequestBody GetRoadMapDto.Request roadmapRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();

        return ApiResponse.success(roadmapService.recommendRoadMap(roadmapRequest, member));
    }

    // 로드맵 조회
    @GetMapping("/recommend")
    @Operation(
            summary = "로드맵 조회",
            description = "로그인한 사용자의 맞춤형 로드맵을 조회합니다."
    )
    public ApiResponse<RecommendDto.RoadMapResponse> getRoadMap(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(roadmapService.getRoadMap(member));
    }

    // 로드맵 액션 체크
    @PostMapping("/{roadMapActionId}")
    @Operation(
            summary = "로드맵 액션 완료/미완료 토글",
            description = "특정 로드맵 내의 액션을 완료/미완료 상태로 토글합니다."
    )
    public ApiResponse<?> checkRoadMapAction(
            @Parameter(description = "로드맵 액션 ID") @PathVariable Long roadMapActionId,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        roadmapService.checkRoadMapAction(roadMapActionId, member);
        return ApiResponse.success();
    }

    @PostMapping("{roadmapId}/roadmapAction")
    @Operation(
            summary = "로드맵 액션 추가",
            description = "특정 로드맵 내에 새로운 액션을 추가합니다."
    )
    public ApiResponse<?> addRoadmapAction(
            @Parameter(
                    description = "로드맵 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long roadmapId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로드맵 액션 추가 요청 DTO",
                    required = true
            )
            @RequestBody RoadmapDto.ActionAddRequest request
    ) {
        roadmapService.addRoadmapAction(roadmapId, request);
        return ApiResponse.success();
    }



    @PutMapping("/{roadMapActionId}")
    @Operation(
            summary = "로드맵 액션 수정",
            description = "특정 로드맵 내의 액션 내용을 수정합니다."
    )
    public ApiResponse<?> updateRoadmapAction(
            @Parameter(description = "로드맵 액션 ID", required = true)
            @PathVariable Long roadMapActionId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로드맵 액션 수정 요청 DTO",
                    required = true
            )
            @RequestBody RoadmapDto.ActionUpdateRequest request
    ) {
        roadmapService.updateRoadmapAction(roadMapActionId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{roadMapActionId}")
    @Operation(
            summary = "로드맵 액션 삭제",
            description = "특정 로드맵 내의 액션을 삭제합니다."
    )
    public ApiResponse<?> deleteRoadmapAction(
            @Parameter(description = "로드맵 액션 ID", required = true)
            @PathVariable Long roadMapActionId
    ) {
        roadmapService.deleteRoadmapAction(roadMapActionId);
        return ApiResponse.success();
    }

    @GetMapping("/roadmapAction/recommend/category")
    @Operation(
            summary = "로드맵 액션 추천 (카테고리 기반)",
            description = "카테고리를 기반으로 로드맵 내에서 수행할 수 있는 액션을 추천합니다."
    )
    public ApiResponse<RoadmapDto.RoadmapActionRecommendResponse> recommendRoadmapAction(
            @Parameter(
                    description = "추천을 받고 싶은 카테고리 (예: 교육, 현장경험 등)",
                    required = true,
                    example = "교육"
            )
            @RequestParam String category
    ) {
        List<String> recommendRoadmapActionList = roadmapService.recommendRoadmapAction(category);
        RoadmapDto.RoadmapActionRecommendResponse response = RoadmapDto.RoadmapActionRecommendResponse.of(recommendRoadmapActionList);
        return ApiResponse.success(response);
    }

    @GetMapping("/roadmapAction/recommend/ai")
    @Operation(
            summary = "로드맵 액션 추천 (AI 기반)",
            description = "AI 모델을 활용해 카테고리를 기반으로 로드맵 내에서 수행할 수 있는 액션을 추천합니다."
    )
    public ApiResponse<RoadmapDto.RoadmapActionRecommendResponse> recommendRoadmapActionAI(
            @Parameter(
                    description = "추천을 받고 싶은 카테고리 (예: 교육, 현장경험 등)",
                    required = true,
                    example = "교육"
            )
            @RequestParam String category
    ) {
        List<String> recommendRoadmapActionList = roadmapService.recommendRoadmapActionAI(category);
        RoadmapDto.RoadmapActionRecommendResponse response =
                RoadmapDto.RoadmapActionRecommendResponse.of(recommendRoadmapActionList);
        return ApiResponse.success(response);
    }



}
