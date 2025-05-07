package com.example.demo.waterapi.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WaterLevelVo {
    private String wlobscd;         // 관측소 코드 (예: 1018640)
    private String obsnm;           // 관측소 이름 (예: 한강대교)
    private String ymdhm;    // 관측 일시
    private String  wl;          // 수위 (m)
    private String  fw;          // 유량자료
}
