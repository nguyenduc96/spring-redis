package com.example.springredis.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final CacheManager cacheManager;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, CacheManager cacheManager) {
        this.redisTemplate = redisTemplate;
        this.cacheManager = cacheManager;
    }

    @Override
    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    @Cacheable(value = "searchRedis", key = "#key")
    public Object find(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean delete(String key) {
       return redisTemplate.delete(key);
    }
}
