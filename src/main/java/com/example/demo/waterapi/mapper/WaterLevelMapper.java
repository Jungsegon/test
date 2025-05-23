package com.example.demo.waterapi.mapper;


import com.example.demo.waterapi.dto.WaterLevelStandardResponse;
import com.example.demo.waterapi.vo.WaterLevelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;


public interface WaterLevelMapper {
    int existsWaterLevel(WaterLevelVo vo);
    void insertWaterLevel(WaterLevelVo vo);
    void updateWaterLevel(WaterLevelVo vo);
    List<WaterLevelVo> findAll();


    // 실시간 수위 조회
    WaterLevelVo findLatestByWlobscd(@Param("wlobscd") String wlobscd);

    // 기간별 수위 조회
    List<WaterLevelVo> findByPeriod(
            @Param("startDt") String startDt,
            @Param("endDt") String endDt,
            @Param("wlobscd") String wlobscd
    );

    @Select("SELECT WLOBSCD, OBSNM, ATTWL, WRNWL, ALMWL, SRSWL, PFH FROM WLOBS_MNT WHERE WLOBSCD = #{wlobscd}")
    WaterLevelStandardResponse findStandardByWlobscd(@Param("wlobscd") String wlobscd);
}