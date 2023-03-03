package com.example.springredis.service;

public interface RedisService {
    void save(String key, Object value);
    Object find(String key);
    Boolean delete(String key);
}
