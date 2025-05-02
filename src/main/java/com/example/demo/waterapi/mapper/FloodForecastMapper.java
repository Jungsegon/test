package com.example.demo.waterapi.mapper;


import com.example.demo.waterapi.vo.FloodForecastVo;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;


public interface FloodForecastMapper {
    void insertFloodForecast(FloodForecastVo vo);
}
