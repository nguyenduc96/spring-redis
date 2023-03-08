package com.example.springredis.controller;

import com.alibaba.fastjson2.JSON;
import com.example.springredis.core.RSARequest;
import com.example.springredis.core.RSAResponse;
import com.example.springredis.service.RedisService;
import com.example.springredis.util.RSAUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    private final RedisService redisService;

    public TestController(RedisService redisService) {
        this.redisService = redisService;
    }


    @PostMapping("/save")
    public String save(@RequestBody Object obj) {
        String key = UUID.randomUUID().toString();
        redisService.save(key, obj);
        return key;
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody Object obj) {
        return RSAUtils.encryptData(JSON.toJSONString(obj));
    }

    @PostMapping("/encrypt/v1")
    public RSAResponse encrypt(@RequestBody RSARequest request) {
        return new RSAResponse(RSAUtils.encryptData(request));
    }

     @PostMapping("/decrypt")
    public Object encrypt(@RequestBody String str) {
        return RSAUtils.decryptData(str, Object.class);
    }


     @PostMapping("/delete/{key}")
    public Boolean save(@PathVariable String key) {
         return redisService.delete(key);


    }

    @GetMapping("/find/{key}")
    public Object get(@PathVariable String key) {
        return redisService.find(key);
    }


}
