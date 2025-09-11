package next.career.domain.job.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.job.controller.dto.Work24;
import next.career.domain.job.service.JobService;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.openai.dto.AiChatDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.user.entity.Member;
import next.career.domain.user.entity.MemberDetail;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.domain.job.service.HrdCourseService;
import next.career.global.security.AuthDetails;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
@Tag(name = "Job API", description = "채용 공고 및 맞춤형 추천 관련 API")
public class JobController {

    private final JobService jobService;
    private final HrdCourseService rawClient;

    // 전체 채용 조회
    @GetMapping("/all")
    @Operation(summary = "전체 채용 조회", description = "검색 조건과 페이징을 통해 전체 채용 공고 목록을 조회합니다.")
    public ApiResponse<GetJobDto.SearchAllResponse> getAllJob(
            @ParameterObject GetJobDto.SearchRequest searchRequest,
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();
        Page<JobDto.AllResponse> jobDtoList = jobService.getAllJob(searchRequest, member, pageable);
        return ApiResponse.success(GetJobDto.SearchAllResponse.of(jobDtoList));
    }

    @GetMapping("/all/anonymous")
    @Operation(summary = "전체 채용 조회", description = "검색 조건과 페이징을 통해 전체 채용 공고 목록을 조회합니다.")
    public ApiResponse<GetJobDto.SearchAllResponse> getAllJobAnonymous(
            @ParameterObject GetJobDto.SearchRequest searchRequest,
            @Parameter(hidden = true) Pageable pageable) {
        Page<JobDto.AllResponse> jobDtoList = jobService.getAllJobAnonymous(searchRequest, pageable);
        return ApiResponse.success(GetJobDto.SearchAllResponse.of(jobDtoList));
    }

    // 단건 채용 조회
    @GetMapping("/{jobId}")
    @Operation(summary = "단건 채용 조회", description = "채용 공고 ID로 단일 채용 공고를 조회합니다.")
    public ApiResponse<JobDto.Response> getJob(
            @Parameter(description = "채용 공고 ID", example = "1") @PathVariable Long jobId,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();
        return ApiResponse.success(jobService.getJob(jobId, member));
    }

    // 북마크된 채용 조회
    @GetMapping("/bookmarks")
    @Operation(summary = "북마크된 채용 공고 조회", description = "북마크된 채용 공고를 조회합니다.")
    public ApiResponse<GetJobDto.SearchAllResponse> getBookMarkedJobs(
            @ParameterObject GetJobDto.SearchRequest searchRequest,
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Page<JobDto.AllResponse> jobDtoList = jobService.getBookMarkedJobs(searchRequest, authDetails.getUser(), pageable);
        return ApiResponse.success(GetJobDto.SearchAllResponse.of(jobDtoList));
    }

    // 맞춤형 직업 추천
    @GetMapping("/recommend/occupation")
    @Operation(summary = "맞춤형 직업 추천", description = "사용자의 정보를 기반으로 맞춤형 직업을 추천합니다.")
    public ApiResponse<JobDto.RecommendJob> recommendOccupation(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();

        RecommendDto.OccupationResponse occupationResponse = jobService.recommendOccupation(member);
        List<RecommendDto.OccupationResponse.Occupation> occupationList =
                Optional.ofNullable(occupationResponse.getOccupationList()).orElse(List.of());

        JobDto.RecommendJob recommendJob = JobDto.RecommendJob.builder()
                .first(!occupationList.isEmpty() ? JobDto.RecommendJob.Occupation.of(occupationList.get(0)) : null)
                .second(occupationList.size() > 1 ? JobDto.RecommendJob.Occupation.of(occupationList.get(1)) : null)
                .third(occupationList.size() > 2 ? JobDto.RecommendJob.Occupation.of(occupationList.get(2)) : null)
                .build();

        return ApiResponse.success(recommendJob);
    }

    // 맞춤형 일자리 추천
    @GetMapping("/recommend/job")
    @Operation(summary = "맞춤형 일자리 추천", description = "사용자의 정보를 기반으로 맞춤형 일자리를 추천합니다.")
    public ApiResponse<GetJobDto.SearchAllResponse> recommendJob(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(hidden = true) Pageable pageable) {
        Member member = authDetails.getUser();
        Page<JobDto.AllResponse> jobDtoList = jobService.recommendJob(member, pageable);
        return ApiResponse.success(GetJobDto.SearchAllResponse.of(jobDtoList));
    }

    // 맞춤형 로드맵 추천
    @PostMapping("/recommend/roadmap")
    @Operation(summary = "맞춤형 로드맵 추천", description = "사용자의 이력 및 관심 직무를 기반으로 커리어 로드맵을 추천합니다.")
    public ApiResponse<RecommendDto.RoadMapResponse> recommendRoadMap(
            @RequestBody GetRoadMapDto.Request roadmapRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();

        return ApiResponse.success(jobService.recommendRoadMap(roadmapRequest, member));
    }

    // 로드맵 조회
    @GetMapping("/recommend/roadmap")
    @Operation(
            summary = "로드맵 조회",
            description = "로그인한 사용자의 맞춤형 로드맵을 조회합니다."
    )
    public ApiResponse<RecommendDto.RoadMapResponse> getRoadMap(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(jobService.getRoadMap(member));
    }

    // 로드맵 액션 체크
    @PostMapping("/roadmap/{roadMapId}/{roadMapActionId}")
    @Operation(
            summary = "로드맵 액션 완료/미완료 토글",
            description = "특정 로드맵 내의 액션을 완료/미완료 상태로 토글합니다."
    )
    public ApiResponse<?> checkRoadMapAction(
            @Parameter(description = "로드맵 ID") @PathVariable Long roadMapId,
            @Parameter(description = "로드맵 액션 ID") @PathVariable Long roadMapActionId,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        jobService.checkRoadMapAction(roadMapId, roadMapActionId, member);
        return ApiResponse.success();
    }

    // AI 채팅 답변 저장
    @PostMapping("/chat/{sequence}")
    @Operation(summary = "AI 채팅 답변 저장", description = "AI가 제공하는 질문 시퀀스에 대해 사용자의 답변을 저장합니다.")
    public ApiResponse<?> answerAIChat(
            @Parameter(description = "질문 시퀀스 번호 (1~9)", example = "1")
            @PathVariable Integer sequence,
            @Parameter(description = "사용자가 입력한 답변", example = "꼼꼼함 / 친절함 - 이런식으로 단어를 구분할 수 있도록 하는 게 좋습니다('띄어쓰기', ',', '/'))")
            @RequestParam String answer,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {

        Member member = authDetails.getUser();
        jobService.answerAIChat(sequence, answer, member);
        return ApiResponse.success();
    }

    // AI 채팅 옵션 조회
    @GetMapping("/chat/{sequence}")
    @Operation(summary = "AI 채팅 옵션 조회", description = "AI 채팅 질문 번호에 대한 보기 조회.")
    public ApiResponse<AiChatDto.OptionResponse> getAIChat(
            @Parameter(description = "질문 번호 (1~9)", example = "2")
            @PathVariable Integer sequence,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();
        return ApiResponse.success(jobService.getAIChat(sequence, member));
    }

    @GetMapping("/chat/history")
    @Operation(summary = "AI 채팅 답변 히스토리 조회", description = "사용자가 AI 채팅에서 입력한 답변 히스토리를 조회합니다.")
    public ApiResponse<AiChatDto.HistoryResponse> getAIChatHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Member member = authDetails.getUser();

        MemberDetail memberDetail = jobService.getAiChatHistory(member);
        AiChatDto.HistoryResponse memberDetailResponse = AiChatDto.HistoryResponse.of(memberDetail);

        return ApiResponse.success(memberDetailResponse);
    }

    @GetMapping("/hrd-course")
    public ApiResponse<Work24.CardCoursePage> raw(@RequestParam(defaultValue = "") String keyword,
                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "20250101") String startYmd,
                                                  @RequestParam(defaultValue = "20251231") String endYmd) {
        return ApiResponse.success(rawClient.callRaw(keyword, pageNo, pageSize, startYmd, endYmd));
    }

}
