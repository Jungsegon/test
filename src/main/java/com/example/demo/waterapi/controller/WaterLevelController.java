package com.example.demo.waterapi.controller;


import com.example.demo.waterapi.dto.WaterLevelResponse;
import com.example.demo.waterapi.service.WaterLevelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WaterLevelController {

    private final WaterLevelService waterLevelService;

    @GetMapping("/getLatest")
    public ResponseEntity<?> getLatestWaterLevel(@RequestParam String wlobscd) {
        if (wlobscd == null || wlobscd.isEmpty()) {
            return ResponseEntity.badRequest().body("관측소 ID(wlobscd)는 필수입니다.");
        }

        WaterLevelResponse latest = waterLevelService.getLatestWaterLevel(wlobscd);

        if (latest == null) {
            return ResponseEntity.ok().body("데이터 없음");
        }

        return ResponseEntity.ok(latest);
    }

    @GetMapping("/getByPeriod")
    public ResponseEntity<?> getWaterLevelsByPeriod(
            @RequestParam String startDt,
            @RequestParam String endDt,
            @RequestParam(required = false) String wlobscd
    ) {
        if (startDt.isEmpty() || endDt.isEmpty()) {
            return ResponseEntity.badRequest().body("startDt, endDt는 필수입니다.");
        }

        List<WaterLevelResponse> results = waterLevelService.getWaterLevelsByPeriod(startDt, endDt, wlobscd);

        if (results.isEmpty()) {
            return ResponseEntity.ok("데이터 없음");
        }

        return ResponseEntity.ok(results);

    }
}