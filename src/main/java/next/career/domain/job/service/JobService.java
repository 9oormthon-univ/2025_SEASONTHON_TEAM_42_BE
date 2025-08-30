package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.service.dto.JobDto;
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

    public Page<JobDto.AllResponse> getAllJob(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return jobRepository.findAll(pageable)
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

    public void recommendOccupation(Long userId) {

    }

    public void recommendJob(Long id) {

    }

    public void recommendRoadMap() {

    }



}
