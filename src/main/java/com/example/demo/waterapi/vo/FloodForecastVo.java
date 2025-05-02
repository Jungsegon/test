package com.example.demo.waterapi.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FloodForecastVo {
    private String ancDt;         // 발표일시
    private String ancNm;         // 발표자
    private String fctDt;         // 수위 도달 예상일시
    private String fctHgt;        // 예상 수위표수위
    private String fctSealvl;     // 예상 해발수위
    private String kind;          // 예보 종류 (주의보, 경보 등)
    private String no;            // 예보 번호
    private String obsnm;         // 지점명
    private String prntDt;        // 기존 발령일시
    private String rfrnc2;        // 비고
    private String rvrNm;         // 강명
    private String sttCurCng;     // 변동상황
    private String sttCurDt;      // 현재 일시
    private String sttCurHgt;     // 현재 수위표수위
    private String sttCurSealvl;  // 현재 해발수위
    private String sttFctDt;      // 예상 일시
    private String sttFctHgt;     // 예상 수위표수위
    private String sttFctSealvl;  // 예상 해발수위
    private String sttNm;         // 관측소 코드
    private String wrnAraNm;      // 주의 지역
    private String wrnRvrNm;      // 주의 강명
}

