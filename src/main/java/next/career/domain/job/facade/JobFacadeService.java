package next.career.domain.job.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.education.entity.Education;
import next.career.domain.job.entity.Job;
import next.career.domain.job.service.JobBatchService;
import next.career.domain.pinecone.service.PineconeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void getJobDataFromSeoulJobSchedule() {

        List<Job> jobs = jobBatchService.fetchAndSaveJobsSchedule();

        // 2. Pinecone 업서트 (동시성 5개 제한)
        Flux.fromIterable(jobs)
                .flatMap(job -> pineconeService.saveJobVector(job.getJobId()), 5)
                .index()
                .doOnNext(tuple -> {
                    long idx = tuple.getT1(); // 0-based index
                    if ((idx + 1) % 5 == 0) {
                        log.info("{}개 저장 완료", idx + 1);
                    }
                })
                .then()
                .block();

    }
}
