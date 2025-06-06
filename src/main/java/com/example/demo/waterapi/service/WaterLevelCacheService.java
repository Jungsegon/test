package com.example.demo.waterapi.service;

import com.example.demo.waterapi.dto.WaterLevelResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaterLevelCacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String WATER_LEVEL_KEY = "WATER_LEVEL:%s";

    public void saveToCache(WaterLevelResponse response) {
        String key = String.format(WATER_LEVEL_KEY, response.getWlobscd());
        log.info("캐시 저장 시 사용 Key: {}", key);
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 저장 실패", e);
        }
    }

    public WaterLevelResponse getFromCache(String wlobscd) {
        String key = String.format(WATER_LEVEL_KEY, wlobscd);
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;

        try {
            return objectMapper.readValue(json, WaterLevelResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 조회 실패", e);
        }
    }
}
