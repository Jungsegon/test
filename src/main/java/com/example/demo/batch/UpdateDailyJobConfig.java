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
public class UpdateDailyJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WaterLevelSchedulerService schedulerService;

    @Bean
    public Job updateDailyJob() {
        return jobBuilderFactory.get("updateDailyJob")
                .start(updateStep())
                .build();
    }

    @Bean
    public Step updateStep() {
        return stepBuilderFactory.get("updateStep")
                .tasklet((contribution, chunkContext) -> {
                    try{
                        schedulerService.fetchAndUpdateFullDay("1018683");
                        schedulerService.fetchAndUpdateFullDay("1018662");
                    }catch (Exception e){
                        throw new RuntimeException("updateStep 실패", e);
                    }


                    return RepeatStatus.FINISHED;
                }).build();
    }
}
