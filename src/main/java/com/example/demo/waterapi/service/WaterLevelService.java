package com.example.demo.waterapi.service;

import com.example.demo.waterapi.dto.WaterLevelResponse;
import com.example.demo.waterapi.mapper.WaterLevelMapper;
import com.example.demo.waterapi.vo.WaterLevelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaterLevelService {

    private final WaterLevelMapper waterLevelMapper;

    public WaterLevelResponse getLatestWaterLevel(String wlobscd) {
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
        List<WaterLevelVo> list = waterLevelMapper.findByPeriod(startDt, endDt, wlobscd);

        return list.stream()
                .map(vo -> WaterLevelResponse.builder()
                        .wlobscd(vo.getWlobscd())
                        .ymdhm(vo.getYmdhm())
                        .wl(vo.getWl())
                        .fw(vo.getFw())
                        .build())
                .collect(Collectors.toList());
    }
}