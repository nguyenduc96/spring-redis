package com.example.springredis.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RSAResponse {
    private Object data;
    private String message = "OK";
    private String errorCode = "0";
    private String encryptKey;
    public RSAResponse(Object data) {
        this.data = data;
    }
}
