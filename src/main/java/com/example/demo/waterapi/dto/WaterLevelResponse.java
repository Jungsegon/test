package com.example.demo.waterapi.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "관측소 실시간 수위 응답 DTO")
public class WaterLevelResponse {
    @Schema(description = "관측소 코드 (예: 1018683)", example = "1018683")
    private String wlobscd;
    @Schema(description = "관측 일시 (yyyyMMddHHmm)", example = "202505121530")
    private String ymdhm;
    @Schema(description = "수위 (m)", example = "2.1")
    private String wl;
    @Schema(description = "유량 자료", example = "387.55")
    private String fw;
}

