package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.pinecone.service.PineconeService;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PineconeService pineconeService;

    public void getAllJob() {

    }

    public JobDto.Response getJob(Long jobId) {

    }

    public void recommendOccupation(Long userId) {

    }

    public void recommendJob(Long id) {

    }

    public void recommendRoadMap() {

    }



}
