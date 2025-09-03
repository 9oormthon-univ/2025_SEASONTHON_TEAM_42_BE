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
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PineconeService pineconeService;
    private final OpenAiService openAiService;
    private final JobCustomRepository jobCustomRepository;

    public Page<JobDto.AllResponse> getAllJob(GetJobDto.SearchRequest request, Member member, Pageable pageable) {

        return jobCustomRepository.findAll(request, pageable)
                .map(job -> JobDto.AllResponse.of(
                        job,
                        getRecommendScore(job, member),
                        getIsScrap(job, member)
                ));
    }

    public JobDto.Response getJob(Long jobId, Member member ) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

        Integer recommendScore = getRecommendScore(job, member);
        Boolean isScrap = getIsScrap(job, member);

        return JobDto.Response.of(job, recommendScore, isScrap);

    }

    private Boolean getIsScrap(Job job, Member member) {
        return null;
    }

    private Integer getRecommendScore(Job job, Member member) {
        return null;
    }

    public RecommendDto.OccupationResponse recommendOccupation(Member member) {
        return openAiService.getRecommendOccupation(member);
    }

    @Transactional
    public RecommendDto.JobResponse recommendJob(Member member) {
        RecommendDto.JobResponse recommendJob = openAiService.getRecommendJob(member);

        String jobIdStr = recommendJob.getJobId();
        Long jobId = Long.valueOf(jobIdStr);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

        recommendJob.isScrap(getIsScrap(job, member));
        return recommendJob;
    }

    public RecommendDto.RoadMapResponse recommendRoadMap(Member member) {
        return openAiService.getRecommendRoadMap(member);

    }



}
