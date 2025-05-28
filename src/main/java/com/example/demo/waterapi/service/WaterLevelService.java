package com.example.demo.waterapi.service;

import com.example.demo.waterapi.dto.WaterLevelResponse;

import com.example.demo.waterapi.mapper.WaterLevelMapper;
import com.example.demo.waterapi.vo.WaterLevelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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


}