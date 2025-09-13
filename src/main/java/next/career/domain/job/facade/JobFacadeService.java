package next.career.domain.job.facade;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.domain.job.service.JobBatchService;
import next.career.domain.pinecone.service.PineconeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobFacadeService {

    private final JobBatchService jobBatchService;
    private final PineconeService pineconeService;

    public void getJobDataFromSeoulJob(int pageNumber, int pageSize) {
        List<Job> jobs = jobBatchService.fetchAndSaveJobs(pageNumber, pageSize);

        Flux.fromIterable(jobs)
                .flatMap(job -> pineconeService.saveJobVector(job.getJobId()))
                .then()
                .block();
    }
}
