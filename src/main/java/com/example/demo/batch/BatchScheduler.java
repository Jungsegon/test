package com.example.demo.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job insertLatestJob;
    private final Job updateDailyJob;
    private final Job refreshCacheJob;

    @Scheduled(cron = "0 * * * * *") // 매 1분
    public void runInsertJob() throws Exception {
        jobLauncher.run(insertLatestJob,
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    }

    @Scheduled(cron = "0 0 * * * *") // 매 1시간
    public void runUpdateJob() throws Exception {
        jobLauncher.run(updateDailyJob,
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    }

    @Scheduled(cron = "0 8,18,28,38,48,58 * * * *") // 캐시 갱신
    public void runCacheRefreshJob() throws Exception {
        jobLauncher.run(refreshCacheJob,
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    }
    // insert -> redis 바로연결
    // if else 죽었을 경우.
}

