package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.pinecone.service.PineconeService;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PineconeService pineconeService;

    public void getAllJob(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Job> all = jobRepository.findAll(pageable);
    }

    public void getJob(Long jobId) {
        jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

    }

    public void recommendOccupation(Long userId) {

    }

    public void recommendJob(Long id) {

    }

    public void recommendRoadMap() {

    }



}
