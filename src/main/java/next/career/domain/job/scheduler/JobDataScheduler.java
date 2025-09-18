package next.career.domain.job.scheduler;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.facade.JobFacadeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobDataScheduler {

    private final JobFacadeService jobFacadeService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void fetchSeoulJobData() {
        jobFacadeService.getJobDataFromSeoulJobSchedule();
    }
}
