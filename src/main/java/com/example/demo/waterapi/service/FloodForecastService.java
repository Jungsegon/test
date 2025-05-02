package com.example.demo.waterapi.service;


import com.example.demo.waterapi.mapper.FloodForecastMapper;
import com.example.demo.waterapi.vo.FloodForecastVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FloodForecastService {

    private final FloodForecastMapper floodForecastMapper;
    private final RestTemplate restTemplate;

    private static final String SERVICE_KEY = "866442D5-9096-4C0F-AC8E-CB087115B9DF";

    public void fetchForecastData() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String url = String.format(
                "http://api.hrfco.go.kr/%s/fldfct/list.json?Edt=%s&HydroType=fldfct&DataType=list&DocumentType=json",
                SERVICE_KEY, date
        );

        try {
            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                log.warn("API 응답 없음");
                return;
            }

            // ✅ API 코드 체크 (예: 990 = 자료 없음)
            if (response.containsKey("code") && !"00".equals(response.get("code"))) {
                log.info("홍수예보 없음: code={}, message={}", response.get("code"), response.get("message"));
                return;
            }

            if (response.get("content") == null) {
                log.info("content 항목 없음 → 예보 데이터 없음");
                return;
            }

            List<Map<String, Object>> contentList = (List<Map<String, Object>>) response.get("content");

            if (contentList.isEmpty()) {
                log.info("예보 발령 데이터 0건 (평시)");
                return;
            }

            for (Map<String, Object> item : contentList) {
                FloodForecastVo vo = FloodForecastVo.builder()
                        .ancDt((String) item.get("ANCDT"))
                        .ancNm((String) item.get("ANCNM"))
                        .fctDt((String) item.get("FCTDT"))
                        .fctHgt((String) item.get("FCTHGT"))
                        .fctSealvl((String) item.get("FCTSEALVL"))
                        .kind((String) item.get("KIND"))
                        .no((String) item.get("NO"))
                        .obsnm((String) item.get("OBSNM"))
                        .prntDt((String) item.get("PRNTDT"))
                        .rfrnc2((String) item.get("RFRNC2"))
                        .rvrNm((String) item.get("RVRNM"))
                        .sttCurCng((String) item.get("STTCURCNG"))
                        .sttCurDt((String) item.get("STTCURDT"))
                        .sttCurHgt((String) item.get("STTCURHGT"))
                        .sttCurSealvl((String) item.get("STTCURSEALVL"))
                        .sttFctDt((String) item.get("STTFCTDT"))
                        .sttFctHgt((String) item.get("STTFCTHGT"))
                        .sttFctSealvl((String) item.get("STTFCTSEALVL"))
                        .sttNm((String) item.get("STTNM"))
                        .wrnAraNm((String) item.get("WRNARANM"))
                        .wrnRvrNm((String) item.get("WRNRVRNM"))
                        .build();

                floodForecastMapper.insertFloodForecast(vo);
            }

            log.info("홍수예보 {}건 수집 및 저장 완료", contentList.size());

        } catch (Exception e) {
            log.error("홍수예보 수집 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}

