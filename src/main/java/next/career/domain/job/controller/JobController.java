package next.career.domain.job.controller;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.service.JobService;
import next.career.global.apiPayload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // 전체 채용 조회
    @GetMapping()
    public ApiResponse<?> getAllJob(){
        jobService.getAllJob();
        return ApiResponse.success();
    }

    // 단건 채용 조회
    @GetMapping()
    public ApiResponse<?> getJob(@PathVariable Long jobId){
        jobService.getJob(jobId);
        return ApiResponse.success();

    }

    // 맞춤형 직업 추천
    @GetMapping()
    public ApiResponse<?> recommendOccupation(Long userId){
        jobService.recommendOccupation(userId);
        return ApiResponse.success();

    }

    // 맞춤형 일자리 추천
    @GetMapping()
    public ApiResponse<?> recommendJob(Long userId) {
        jobService.recommendJob(userId);
        return ApiResponse.success();
    }

    // 맞춤형 로드맵 추천
    @GetMapping()
    public ApiResponse<?> recommendRoadMap(Long userId){
        jobService.recommendRoadMap();
        return ApiResponse.success();

    }

}
