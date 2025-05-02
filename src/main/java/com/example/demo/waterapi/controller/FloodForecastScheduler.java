package com.example.demo.waterapi.controller;



import com.example.demo.waterapi.service.FloodForecastService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FloodForecastScheduler {

    private final FloodForecastService floodForecastService;

    /**
     * 매 시간마다 홍수예보 데이터를 수집함
     */
//    @Scheduled(cron = "*/5 * * * * *") // 매 5초 간격
//    @Scheduled(cron = "0 */5 * * * *") // 매 시각의 0초에, 5분 단위로 실행
//    @Scheduled(cron = "0 */10 * * * *") // 매 10분마다 0초에 실행
    @Scheduled(cron = "0 * * * * * ") // 매 분 0초에 실행
    public void fetchForecast() {
        log.info("[홍수예보] 데이터 수집 시작");
        floodForecastService.fetchForecastData();
        log.info("[홍수예보] 수집 완료");
    }
}