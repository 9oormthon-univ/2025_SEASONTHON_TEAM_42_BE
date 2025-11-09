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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobFacadeService {

    private final JobBatchService jobBatchService;
    private final PineconeService pineconeService;

    public void getJobDataFromSeoulJob(int pageNumber, int pageSize) {
        long startTime = System.currentTimeMillis();
        log.info("[START] getJobDataFromSeoulJob 시작 page={}, size={}", pageNumber, pageSize);

        // 1️⃣ DB 저장
        List<Job> jobs = jobBatchService.fetchAndSaveJobs(pageNumber, pageSize);
        long afterDb = System.currentTimeMillis();
        log.info("[TIME] DB 저장 완료: {}개, {}ms", jobs.size(), afterDb - startTime);

        // 2️⃣ Pinecone 업서트
        Flux.fromIterable(jobs)
                .flatMap(job -> pineconeService.saveJobVector(job.getJobId()))
                .then()
                .block(); // 동기 대기
        long afterPinecone = System.currentTimeMillis();

        log.info("[TIME] Pinecone 업서트 완료 ({}개, {}ms)", jobs.size(), afterPinecone - afterDb);
        log.info("[TIME] 전체 완료 (총 {}ms)", afterPinecone - startTime);
    }

    public Mono<Void> getJobDataFromSeoulJobAsync(int pageNumber, int pageSize) {
        long startTime = System.currentTimeMillis();

        return jobBatchService.fetchAndSaveJobsAsync(pageNumber, pageSize)
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .flatMap(jobs -> {
                    long dbElapsed = System.currentTimeMillis() - startTime;
                    log.info("[TIME] 서울시 API + DB 저장 완료: {}개, 소요 {}ms", jobs.size(), dbElapsed);

                    AtomicInteger counter = new AtomicInteger();

                    return Flux.fromIterable(jobs)
                            .flatMap(job ->
                                    pineconeService.saveJobVectorAsync(job.getJobId())
                                            .doOnSuccess(v -> counter.incrementAndGet()), 5)
                            .doOnComplete(() -> {
                                long totalElapsed = System.currentTimeMillis() - startTime;
                                log.info("[TIME] 🧠 Pinecone 업서트 완료: {}개, 총 소요 {}ms", counter.get(), totalElapsed);
                            })
                            .then();
                });
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

    public void getJobDataFromSeoulJobVirtual(int pageNumber, int pageSize) {
        long start = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // 🌐 서울시 API 호출 및 DB 저장
            List<Job> jobs = jobBatchService.fetchAndSaveJobs(pageNumber, pageSize);
            log.info("[V3] Job 데이터 저장 완료: {}개", jobs.size());

            // 📤 Pinecone 업서트 병렬 수행
            List<CompletableFuture<Void>> futures = jobs.stream()
                    .map(job -> CompletableFuture.runAsync(() ->
                            pineconeService.saveJobVectorBlocking(job.getJobId()), executor))
                    .toList();

            futures.forEach(CompletableFuture::join);

        } catch (Exception e) {
            log.error("[V3] Virtual Thread 실행 실패", e);
            throw new RuntimeException("Virtual Thread execution failed", e);
        }

        log.info("[V3] 서울시 API + DB + Pinecone 업서트 완료 ({}ms)", System.currentTimeMillis() - start);
    }

}
