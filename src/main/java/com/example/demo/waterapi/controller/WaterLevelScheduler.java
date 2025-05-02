package com.example.demo.waterapi.controller;


import com.example.demo.waterapi.service.WaterLevelService;
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

    private final WaterLevelService waterLevelService;
    private static final String HANGANG_CODE = "1018683";
    private static final String CHEONGDAM_CODE = "1018662";

//    @Scheduled(cron = "0 * * * * *")  // 매 1분
    @Scheduled(cron = "*/30 * * * * *")
    public void insertLatest() {
        waterLevelService.fetchAndInsertLatest(HANGANG_CODE, "한강대교");
        waterLevelService.fetchAndInsertLatest(CHEONGDAM_CODE, "청담대교");
    }

    @Scheduled(cron = "0 * *  * * *")  // 매 1시간
    public void updateIfChanged() {
        waterLevelService.fetchAndUpdateFullDay(HANGANG_CODE, "한강대교");
        waterLevelService.fetchAndUpdateFullDay(CHEONGDAM_CODE, "청담대교");
    }

    //zzzzzzzzzzzzzzzzzzzzzzzzzz
}