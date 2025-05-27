package com.example.demo.batch;

import com.example.demo.waterapi.service.WaterLevelSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InsertLatestJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WaterLevelSchedulerService schedulerService;

    @Bean
    public Job insertLatestJob() {
        return jobBuilderFactory.get("insertLatestJob")
                .start(insertLatestStep())
                .build();
    }

    @Bean
    public Step insertLatestStep() {
        return stepBuilderFactory.get("insertLatestStep")
                .tasklet((contribution, chunkContext) -> {
                    schedulerService.fetchAndInsertLatest("1018683");
                    schedulerService.fetchAndInsertLatest("1018662");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
