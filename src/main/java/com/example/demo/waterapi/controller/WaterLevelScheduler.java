//package com.example.demo.waterapi.controller;
//
//
//import com.example.demo.waterapi.dto.WaterLevelResponse;
//import com.example.demo.waterapi.service.WaterLevelCacheService;
//import com.example.demo.waterapi.service.WaterLevelSchedulerService;
//import com.example.demo.waterapi.service.WaterLevelService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/flood")
//public class WaterLevelScheduler {
//
//    private final WaterLevelSchedulerService waterLevelSchedulerService;
//    private final WaterLevelService waterLevelService;
//    private final WaterLevelCacheService waterLevelCacheService;
//    private static final String HANGANG_CODE = "1018683";
//    private static final String CHEONGDAM_CODE = "1018662";
//
//    @Scheduled(cron = "0 * * * * *")  // 매 1분
////    @Scheduled(cron = "*/30 * * * * *")
//    public void insertLatest() {
//        waterLevelSchedulerService.fetchAndInsertLatest(HANGANG_CODE);
//        waterLevelSchedulerService.fetchAndInsertLatest(CHEONGDAM_CODE);
//    }
//
//    @Scheduled(cron = "0 0 * * * *")  // 매 1시간
//    public void updateIfChanged() {
//        waterLevelSchedulerService.fetchAndUpdateFullDay(HANGANG_CODE);
//        waterLevelSchedulerService.fetchAndUpdateFullDay(CHEONGDAM_CODE);
//    }
//
//    @Scheduled(cron = "0 8,18,28,38,48,58 * * * *") // 8분, 18분, 28분, 38분, 48분, 58분
//    public void refreshCache() {
//        log.info("[스케줄러] 한강 캐시 갱신 시작");
//        WaterLevelResponse hangang = waterLevelService.getLatestWaterLevel(HANGANG_CODE);
//        if (hangang != null) {
//            waterLevelCacheService.saveToCache(hangang);
//            log.info("[스케줄러] 한강 캐시 저장 완료: {}", hangang);
//        } else {
//            log.warn("[스케줄러] 한강 DB 조회 결과 없음");
//        }
//
//        log.info("[스케줄러] 청담 캐시 갱신 시작");
//        WaterLevelResponse cheongdam = waterLevelService.getLatestWaterLevel(CHEONGDAM_CODE);
//        if (cheongdam != null) {
//            waterLevelCacheService.saveToCache(cheongdam);
//            log.info("[스케줄러] 청담 캐시 저장 완료: {}", cheongdam);
//        } else {
//            log.warn("[스케줄러] 청담 DB 조회 결과 없음");
//        }
//
//        log.info("[스케줄러] 캐시 갱신 완료");
//    }
//}
