package com.example.demo.waterapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WaterLevelResponse {
    private String wlobscd;
    private String ymdhm;
    private String wl;
    private String fw;
}