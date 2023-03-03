package com.example.springredis.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RSARequest {
    private String encryptKey;

    private Object data;
}
