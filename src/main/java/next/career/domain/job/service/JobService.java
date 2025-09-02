package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobCustomRepository;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.openai.OpenAiService;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.pinecone.service.PineconeService;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PineconeService pineconeService;
    private final OpenAiService openAiService;
    private final JobCustomRepository jobCustomRepository;

    public Page<JobDto.AllResponse> getAllJob(GetJobDto.SearchRequest request, Long userId, Pageable pageable) {

        return jobCustomRepository.findAll(request, pageable)
                .map(job -> JobDto.AllResponse.of(
                        job,
                        getRecommendScore(job, userId),
                        getIsScrap(job, userId)
                ));
    }

    public JobDto.Response getJob(Long userId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

        Integer recommendScore = getRecommendScore(job, userId);
        Boolean isScrap = getIsScrap(job, userId);

        return JobDto.Response.of(job, recommendScore, isScrap);

    }

    private Boolean getIsScrap(Job job, Long id) {
        return null;
    }

    private Integer getRecommendScore(Job job, Long id) {
        return null;
    }

    public RecommendDto.OccupationResponse recommendOccupation(Long userId) {
        return openAiService.getRecommendOccupation(userId);
    }

    public void recommendJob(Long id) {

    }

    public RecommendDto.RoadMapResponse recommendRoadMap(Long userId) {
        return openAiService.getRecommendRoadMap(userId);

    }



}
