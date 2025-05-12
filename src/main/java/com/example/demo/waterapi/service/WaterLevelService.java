package com.example.demo.waterapi.service;


import com.example.demo.waterapi.mapper.WaterLevelMapper;
import com.example.demo.waterapi.vo.WaterLevelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaterLevelService {

    private final WaterLevelMapper waterLevelMapper;
    private final RestTemplate restTemplate;

    private static final String SERVICE_KEY = "866442D5-9096-4C0F-AC8E-CB087115B9DF";


    // ✅ 1분마다 호출 - INSERT 전용
    @Transactional
    public void fetchAndInsertLatest(String wlobscd) {
        try {
            String url = String.format("http://api.hrfco.go.kr/%s/waterlevel/list/10M/%s.json", SERVICE_KEY, wlobscd);

            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null || response.get("content") == null) return;

            List<Map<String, Object>> list = (List<Map<String, Object>>) response.get("content");
            if (list.isEmpty()) return;

            Map<String, Object> item = list.get(0); // 최신 1건
            String ymdhm = (String) item.get("ymdhm");
            String wl = cleanStr(item.get("wl"));
            String fw = cleanStr(item.get("fw"));

            if (wl == null && fw == null) return;

            WaterLevelVo vo = WaterLevelVo.builder()
                    .wlobscd(wlobscd)
                    .ymdhm(ymdhm)
                    .wl(wl)
                    .fw(fw)
                    .build();

            if (waterLevelMapper.existsWaterLevel(vo) == 0) {
                waterLevelMapper.insertWaterLevel(vo);
                log.info("---------------------------------------------수위 insert 완료---------");
            }


        } catch (Exception e) {
            log.error("1분 수위 INSERT 실패: {}", e.getMessage(), e);
        }
    }

    // ✅ 1시간마다 호출 - UPDATE 전용
    @Transactional
    public void
    fetchAndUpdateFullDay(String wlobscd) {
        try {
            String fromDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0000";
            String toDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "2359";

            String url = String.format("http://api.hrfco.go.kr/%s/waterlevel/list/10M/%s/%s/%s.json",
                    SERVICE_KEY, wlobscd, fromDate, toDate);

            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null || response.get("content") == null) return;

            List<Map<String, Object>> list = (List<Map<String, Object>>) response.get("content");
            List<WaterLevelVo> existing = waterLevelMapper.findAll();

            Map<String, WaterLevelVo> existingMap = existing.stream()
                    .collect(Collectors.toMap(e -> e.getWlobscd() + "_" + e.getYmdhm(), Function.identity()));

            for (Map<String, Object> item : list) {
                String ymdhm = (String) item.get("ymdhm");
                String wl = cleanStr(item.get("wl"));
                String fw = cleanStr(item.get("fw"));

                if (wl == null && fw == null) continue;

                String key = wlobscd + "_" + ymdhm;
                WaterLevelVo old = existingMap.get(key);

                if (old != null && (!Objects.equals(wl, old.getWl()) || !Objects.equals(fw, old.getFw()))) {
                    WaterLevelVo updateVo = WaterLevelVo.builder()
                            .wlobscd(wlobscd)
                            .ymdhm(ymdhm)
                            .wl(wl)
                            .fw(fw)
                            .build();
                    waterLevelMapper.updateWaterLevel(updateVo);
                    log.info("====================================수위 업데이트 완료 !!!!!!!!!!!!!!!!!!!");
                }
            }

        } catch (Exception e) {
            log.error("하루 전체 수위 UPDATE 실패: {}", e.getMessage(), e);
        }
    }


    private String cleanStr(Object obj) {
        if (obj == null) return null;
        String str = obj.toString().trim();
        return (str.isEmpty() || str.equals("-") || str.equals("NaN")) ? null : str;
    }
}
