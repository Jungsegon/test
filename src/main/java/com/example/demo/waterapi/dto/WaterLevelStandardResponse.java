package com.example.demo.waterapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "관측소 기준 수위 응답 DTO")
public class WaterLevelStandardResponse {
    @Schema(description = "관측소 코드 (예: 1018683)", example = "1018683")
    private String wlobscd;   // 관측소 코드
    @Schema(description = "관심 수위", example = "4.00")
    private String attwl;     // 관심 수위
    @Schema(description = "주의보 수위", example = "5.00")
    private String wrnwl;     // 주의보 수위
    @Schema(description = "경보 수위", example = "6.00")
    private String almwl;     // 경보 수위
    @Schema(description = "심각 수위", example = "7.00")
    private String srswl;     // 심각 수위
    @Schema(description = "계획홍수위", example = "8.00")
    private String pfh;       // 계획홍수위
}
