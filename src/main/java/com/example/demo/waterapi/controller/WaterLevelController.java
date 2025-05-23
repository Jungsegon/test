package com.example.demo.waterapi.controller;


import com.example.demo.waterapi.dto.WaterLevelResponse;
import com.example.demo.waterapi.dto.WaterLevelStageResponse;
import com.example.demo.waterapi.dto.WaterLevelStandardResponse;
import com.example.demo.waterapi.service.WaterLevelCacheService;
import com.example.demo.waterapi.service.WaterLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "실시간, 기간별 수위 조회")
public class WaterLevelController {

    private final WaterLevelService waterLevelService;
    private final WaterLevelCacheService waterLevelCacheService;

    @ApiOperation(value ="실시간 수위 조회", notes = "wlobscd (대교 코드)를 입력하면 수위 유량 실시간조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청(필수 파라미터 누락 등)"),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping(value = "/getLatest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WaterLevelResponse> getLatestWaterLevel(
            @Parameter(name = "wlobscd", description = "관측소 코드 (예: 1018683)", required = true)
            @RequestParam String wlobscd) {

        WaterLevelResponse latest = waterLevelService.getLatestWaterLevel(wlobscd);

        return ResponseEntity.ok(latest);
    }


    @ApiOperation(value ="기간별 수위 조회", notes = "startDt ~endDt 수위 유량 실시간조회 + 대교 코드안넣으면 한강대교,청담대교 둘다 불러짐")
    @GetMapping(value = "/getByPeriod", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청(필수 파라미터 누락 등)"),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<List<WaterLevelResponse>> getWaterLevelsByPeriod(
            @Parameter(name = "startDt", description = "검색시작시간 (예: 202505120000)", required = true)
            @RequestParam String startDt,
            @Parameter(name = "endDt", description = "검색종료시간 (예: 202505130000)", required = true)
            @RequestParam String endDt,
            @Parameter(name = "wlobscd", description = "관측소 코드 (예: 1018683)", required = false)
            @RequestParam(required = false) String wlobscd
    ) {

        List<WaterLevelResponse> results = waterLevelService.getWaterLevelsByPeriod(startDt, endDt, wlobscd);


        return ResponseEntity.ok(results);

    }


    @ApiOperation(value = "실시간 수위 캐시 수동 저장", notes = "필수파라미터 - wlobscd")
    @PostMapping(value = "/saveToCacheFromDb", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청(필수 파라미터 누락 등)"),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<?> saveToCacheFromDb(
            @Parameter(name = "wlobscd", description = "관측소 코드 (예: 1018683)", required = true)
            @RequestParam String wlobscd) {
        // DB 조회
        WaterLevelResponse latest = waterLevelService.getLatestWaterLevel(wlobscd);
        // 캐시 저장
        waterLevelCacheService.saveToCache(latest);

        return ResponseEntity.ok("DB에서 조회한 최신 수위 캐시 저장 완료");
    }


    @ApiOperation(value = "redis 캐시로 실시간 수위 불러옴", notes = "필수파라미터 - wlobscd")
    @GetMapping(value = "/getFromCache", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청(필수 파라미터 누락 등)"),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<WaterLevelResponse> getFromCache(
            @Parameter(name = "wlobscd", description = "관측소 코드 (예: 1018683)", required = true)
            @RequestParam String wlobscd) {
        WaterLevelResponse cached = waterLevelCacheService.getFromCache(wlobscd);
        return ResponseEntity.ok(cached);
    }


    @ApiOperation(value = "대교의 기준수위를 불러옴", notes = "관측소 코드(`wlobscd`)로 대교의 기준 수위를 조회합니다.\n\n" +
            "- **attwl**: 관심 수위 (주의 필요 단계)\n" +
            "- **wrnwl**: 주의보 수위 (위험 가능성 증가)\n" +
            "- **almwl**: 경보 수위 (위험 심각)\n" +
            "- **srswl**: 심각 수위 (매우 심각, 즉시 대응 필요)\n" +
            "- **pfh**: 계획홍수위 (설계 기준, 이론상 최대 수위)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청(필수 파라미터 누락 등)"),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping(value = "/getStandard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WaterLevelStandardResponse> getStandard(@RequestParam String wlobscd) {
        WaterLevelStandardResponse standard = waterLevelService.getStandardByWlobscd(wlobscd);

        return ResponseEntity.ok(standard);
    }



    @ApiOperation(value = "수위 정보 경고 알림", notes = "실시간 수위를 조회해서 단계가 어느정도인지 알림.\n\n"+
            "- **attwl**: 관심 수위 (주의 필요 단계)\n" +
            "- **wrnwl**: 주의보 수위 (위험 가능성 증가)\n" +
            "- **almwl**: 경보 수위 (위험 심각)\n" +
            "- **srswl**: 심각 수위 (매우 심각, 즉시 대응 필요)\n" +
            "- **pfh**: 계획홍수위 (설계 기준, 이론상 최대 수위)"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청(필수 파라미터 누락 등)"),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping(value = "/getCurrentStage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WaterLevelStageResponse> getCurrentStage(@RequestParam String wlobscd) {
        WaterLevelStageResponse response = waterLevelService.getCurrentStage(wlobscd);
        return ResponseEntity.ok(response);

    }
}