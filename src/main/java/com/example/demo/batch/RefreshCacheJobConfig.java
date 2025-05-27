package com.example.demo.batch;

import com.example.demo.waterapi.service.WaterLevelCacheService;
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
public class RefreshCacheJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WaterLevelService waterLevelService;
    private final WaterLevelCacheService cacheService;

    @Bean
    public Job refreshCacheJob() {
        return jobBuilderFactory.get("refreshCacheJob")
                .start(refreshStep())
                .build();
    }

    @Bean
    public Step refreshStep() {
        return stepBuilderFactory.get("refreshStep")
                .tasklet((contribution, chunkContext) -> {
                    var h = waterLevelService.getLatestWaterLevel("1018683");
                    var c = waterLevelService.getLatestWaterLevel("1018662");
                    if (h != null) cacheService.saveToCache(h);
                    if (c != null) cacheService.saveToCache(c);
                    return RepeatStatus.FINISHED;
                }).build();
    }
}

