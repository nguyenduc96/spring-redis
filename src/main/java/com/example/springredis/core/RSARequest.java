package com.example.springredis.core;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RSARequest {
    @JSONField(name = "encrypt_key")
    private String encryptKey;

    @JSONField(name = "data")
    private Object data;
}
