package com.example.demo.batch;

import com.example.demo.waterapi.service.WaterLevelCacheService;
import com.example.demo.waterapi.service.WaterLevelSchedulerService;
import com.example.demo.waterapi.service.WaterLevelService;
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
    private final WaterLevelCacheService cacheService;
    private final WaterLevelService waterLevelService;

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
                    try{
                        schedulerService.fetchAndInsertLatest("1018683");
                        schedulerService.fetchAndInsertLatest("1018662");


                    }catch (Exception e){
                        throw new RuntimeException("InsertLatestStep 실패", e);
                    }

                    return RepeatStatus.FINISHED;
                }).build();
    }
}
