package com.example.demo.waterapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "관측소 수위 단계 응답 DTO")
public class WaterLevelStageResponse {
    @Schema(description = "관측소 코드 (예: 1018683)", example = "1018683")
    private String wlobscd;

    @Schema(description = "현재 수위 (m)", example = "3.52")
    private String currentWl;
    @Schema(description = "단계 (예: 정상, 관심, 주의보, 경보, 심각)", example = "관심")
    private String stage;
    @Schema(description = "관심 수위", example = "4.00")
    private String attwl;
    @Schema(description = "주의보 수위", example = "5")
    private String wrnwl;
    @Schema(description = "경보 수위", example = "6.00")
    private String almwl;
    @Schema(description = "심각 수위", example = "7.00")
    private String srswl;
    @Schema(description = "계획홍수위", example = "8.00")
    private String pfh;
}
