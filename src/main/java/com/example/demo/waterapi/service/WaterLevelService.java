package com.example.demo.waterapi.service;

import com.example.demo.waterapi.dto.WaterLevelResponse;
import com.example.demo.waterapi.dto.WaterLevelStageResponse;
import com.example.demo.waterapi.dto.WaterLevelStandardResponse;
import com.example.demo.waterapi.mapper.WaterLevelMapper;
import com.example.demo.waterapi.vo.WaterLevelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaterLevelService {

    private final WaterLevelMapper waterLevelMapper;
    private final WaterLevelCacheService waterLevelCacheService;

    public WaterLevelResponse getLatestWaterLevel(String wlobscd) {
        if (wlobscd == null || wlobscd.trim().isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터 'wlobscd'가 없습니다.");
        }

        WaterLevelVo vo = waterLevelMapper.findLatestByWlobscd(wlobscd);
        if (vo == null) return null;

        return WaterLevelResponse.builder()
                .wlobscd(vo.getWlobscd())
                .ymdhm(vo.getYmdhm())
                .wl(vo.getWl())
                .fw(vo.getFw())
                .build();
    }

    public List<WaterLevelResponse> getWaterLevelsByPeriod(String startDt, String endDt, String wlobscd) {
        if (startDt == null || startDt.trim().isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터 'startDt'가 없습니다.");
        }
        if (endDt == null || endDt.trim().isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터 'endDt'가 없습니다.");
        }

        List<WaterLevelVo> list = waterLevelMapper.findByPeriod(startDt, endDt, wlobscd);

        if (list == null || list.isEmpty()) {
            throw new NoSuchElementException("조회된 수위 데이터가 없습니다.");
        }

        return list.stream()
                .map(vo -> WaterLevelResponse.builder()
                        .wlobscd(vo.getWlobscd())
                        .ymdhm(vo.getYmdhm())
                        .wl(vo.getWl())
                        .fw(vo.getFw())
                        .build())
                .collect(Collectors.toList());
    }

    public WaterLevelStandardResponse getStandardByWlobscd(String wlobscd) {
        if (wlobscd == null || wlobscd.trim().isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터 'wlobscd'가 없습니다.");
        }

        return waterLevelMapper.findStandardByWlobscd(wlobscd);
    }

    public WaterLevelStageResponse getCurrentStage(String wlobscd) {
        if (wlobscd == null || wlobscd.trim().isEmpty()) {
            throw new IllegalArgumentException("필수 파라미터 'wlobscd'가 없습니다.");
        }

        // redis에서 현재 수위 조회
        WaterLevelResponse cached = waterLevelCacheService.getFromCache(wlobscd);
        if (cached == null) {
            throw new RuntimeException("캐시에서 현재 수위 데이터를 찾을 수 없음.");
        }

        // 기준 수위 조회
        WaterLevelStandardResponse standard = waterLevelMapper.findStandardByWlobscd(wlobscd);
        if(standard == null) {
            throw new RuntimeException("DB 기준 수위 데이터가 없음.");
        }

        // 단계 비교
        String stage = compareStage(cached.getWl(), standard);

        return WaterLevelStageResponse.builder()
                .wlobscd(wlobscd)
                .currentWl(cached.getWl())
                .stage(stage)
                .attwl(standard.getAttwl())
                .wrnwl(standard.getWrnwl())
                .almwl(standard.getAlmwl())
                .srswl(standard.getSrswl())
                .pfh(standard.getPfh())
                .build();
    }

//    단계 비교 로직
    private String compareStage(String currentWl, WaterLevelStandardResponse standard) {
        double wl = Double.parseDouble(currentWl);

        if(standard.getPfh() != null && wl>= Double.parseDouble(standard.getPfh())) return "계획홍수 초과";
        if(standard.getSrswl() != null && wl>= Double.parseDouble(standard.getSrswl())) return "심각";
        if(standard.getAlmwl() != null && wl>= Double.parseDouble(standard.getAlmwl())) return "경보";
        if(standard.getWrnwl() != null && wl>= Double.parseDouble(standard.getWrnwl())) return "주의보";
        if(standard.getAttwl() != null && wl>= Double.parseDouble(standard.getAttwl())) return "관심";
        return "정상";

    }

}