package next.career.domain.job.controller;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.service.JobService;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.global.apiPayload.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // 전체 채용 조회
//    @GetMapping()
//    public ApiResponse<GetJobDto.AllResponse> getAllJob(@RequestParam int page, @RequestParam int size){
//        Page<JobDto.AllResponse> jobDtoList = jobService.getAllJob(page, size);
//        return ApiResponse.success(GetJobDto.AllResponse.of(jobDtoList));
//    }
//
//    // 단건 채용 조회
//    @GetMapping()
//    public ApiResponse<JobDto.Response> getJob(@PathVariable Long jobId){
//        return ApiResponse.success(jobService.getJob(jobId));
//
//    }

    // 맞춤형 직업 추천
    @GetMapping("/recommend/occupation")
    public ApiResponse<RecommendDto.OccupationResponse> recommendOccupation(Long userId){
        return ApiResponse.success(jobService.recommendOccupation(userId));
    }

    // 맞춤형 일자리 추천
    @GetMapping("/recommend/job")
    public ApiResponse<?> recommendJob(Long userId) {
        jobService.recommendJob(userId);
        return ApiResponse.success();
    }

    // 맞춤형 로드맵 추천
    @GetMapping("recommend/roadmap")
    public ApiResponse<?> recommendRoadMap(Long userId){
        jobService.recommendRoadMap();
        return ApiResponse.success();

    }

}
