package next.career.domain.job.controller;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.service.JobService;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    // 전체 채용 조회
    @GetMapping("/all")
    public ApiResponse<GetJobDto.AllResponse> getAllJob(
            GetJobDto.SearchRequest searchRequest,
            Pageable pageable,
            @AuthenticationPrincipal AuthDetails authDetails){
        Member member = authDetails.getUser();
        Page<JobDto.AllResponse> jobDtoList = jobService.getAllJob(searchRequest, member, pageable);
        return ApiResponse.success(GetJobDto.AllResponse.of(jobDtoList));
    }

    // 단건 채용 조회
    @GetMapping("/{jobId}")
    public ApiResponse<JobDto.Response> getJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal AuthDetails authDetails){
        Member member = authDetails.getUser();
        return ApiResponse.success(jobService.getJob(jobId, member));

    }

    // 맞춤형 직업 추천
    @GetMapping("/recommend/occupation")
    public ApiResponse<RecommendDto.OccupationResponse> recommendOccupation(
            @AuthenticationPrincipal AuthDetails authDetails){
        Member member = authDetails.getUser();
        return ApiResponse.success(jobService.recommendOccupation(member));
    }

    // 맞춤형 일자리 추천
    @GetMapping("/recommend/job")
    public ApiResponse<RecommendDto.JobResponse> recommendJob(
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Member member = authDetails.getUser();
        return ApiResponse.success(jobService.recommendJob(member));
    }

    // 맞춤형 로드맵 추천
    @GetMapping("recommend/roadmap")
    public ApiResponse<?> recommendRoadMap(
            @AuthenticationPrincipal AuthDetails authDetails){
        Member member = authDetails.getUser();
        jobService.recommendRoadMap(member);
        return ApiResponse.success();
    }

}
