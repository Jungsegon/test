package com.example.demo.waterapi.mapper;


import com.example.demo.waterapi.vo.WaterLevelVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;


public interface WaterLevelMapper {
    int existsWaterLevel(WaterLevelVo vo);
    void insertWaterLevel(WaterLevelVo vo);
    void updateWaterLevel(WaterLevelVo vo);
    List<WaterLevelVo> findAll();
    void updateSeq();
    long getLastSeq();


//    // 10분 단위
//    void insertOrUpdateWaterhangang(WaterLevelVo vo);
//    void insertOrUpdateWatercheongdam(WaterLevelVo vo);

//    // 1시간 단위
//    void insertOrUpdateWaterHangangHour(WaterLevelVo vo);
//    void insertOrUpdateWaterCheongdamHour(WaterLevelVo vo);
//
//    // 일 단위
//    void insertOrUpdateWaterHangangDay(WaterLevelVo vo);
//    void insertOrUpdateWaterCheongdamDay(WaterLevelVo vo);

}