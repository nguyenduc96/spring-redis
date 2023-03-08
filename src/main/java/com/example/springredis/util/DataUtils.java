package com.example.springredis.util;

import com.alibaba.fastjson2.JSON;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class DataUtils {
    public <T> T parseObject(String jsonStr, Class<T> tClass) {
        try {
            return JSON.parseObject(jsonStr,tClass);
        } catch (Exception ex) {
            return null;
        }
    }

    public Map<String, String> parseObject(String jsonStr) {
        return parseObject(jsonStr, Map.class);
    }

    public String toJsonString(Object var1) {
        return JSON.toJSONString(var1);
    }
}
