package com.example.springredis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    public boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }
}
