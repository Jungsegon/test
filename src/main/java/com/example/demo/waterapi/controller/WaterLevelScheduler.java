package com.example.demo.waterapi.controller;


import com.example.demo.waterapi.service.WaterLevelSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/flood")
public class WaterLevelScheduler {

    private final WaterLevelSchedulerService waterLevelSchedulerService;
    private static final String HANGANG_CODE = "1018683";
    private static final String CHEONGDAM_CODE = "1018662";

    @Scheduled(cron = "0 * * * * *")  // 매 1분
//    @Scheduled(cron = "*/30 * * * * *")
    public void insertLatest() {
        waterLevelSchedulerService.fetchAndInsertLatest(HANGANG_CODE);
        waterLevelSchedulerService.fetchAndInsertLatest(CHEONGDAM_CODE);
    }

    @Scheduled(cron = "0 0 * * * *")  // 매 1시간
    public void updateIfChanged() {
        waterLevelSchedulerService.fetchAndUpdateFullDay(HANGANG_CODE);
        waterLevelSchedulerService.fetchAndUpdateFullDay(CHEONGDAM_CODE);
    }

    //zzzzzzzzzzzzzzzzzzzzzzzzzz
}