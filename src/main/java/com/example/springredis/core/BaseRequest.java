package com.example.springredis.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseRequest {
    private String payload;
    private String encryptKey;
}
