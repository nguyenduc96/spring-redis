package com.example.springredis.controller;

import com.alibaba.fastjson2.JSON;
import com.example.springredis.core.BaseRequest;
import com.example.springredis.core.RSARequest;
import com.example.springredis.core.RSAResponse;
import com.example.springredis.service.RedisService;
import com.example.springredis.util.JWTUtils;
import com.example.springredis.util.RSAUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
        return new RSAResponse(RSAUtils.encryptData(request), RSAUtils.getPublicKeyString());
    }

     @PostMapping("/decrypt")
    public Object decrypt(@RequestBody String str) {
        return RSAUtils.decryptData(str, Object.class);
    }

    @PostMapping("/sign")
    public BaseRequest sign(@RequestBody Object obj) {
        return new BaseRequest(JWTUtils.sign(obj), RSAUtils.getPublicKeyString());
    }

    @PostMapping("/verify")
    public Boolean verify(@RequestBody BaseRequest baseRequest) {
        return JWTUtils.verify(baseRequest.getPayload(), baseRequest.getEncryptKey());
    }
    @PostMapping("/verify-payload")
    public Object verifyPayload(@RequestBody BaseRequest baseRequest) {
        return JWTUtils.verifyPayload(baseRequest.getPayload(), baseRequest.getEncryptKey());
    }


     @PostMapping("/delete/{key}")
    public Boolean save(@PathVariable String key) {
         return redisService.delete(key);


    }

    @GetMapping("/find/{key}")
    public Object get(@PathVariable String key) {
        return redisService.find(key);
    }

    @GetMapping("/find")
    public Object get() {
        Map<String, String> map = new HashMap<>();
        map.put("data", "Hello");
        map.put("encrypt_key", "key123");
        RSARequest request = JSON.parseObject(JSON.toJSONString(map), RSARequest.class);
        return request;
    }


}
