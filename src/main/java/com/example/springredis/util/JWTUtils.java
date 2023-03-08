package com.example.springredis.util;


import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Map;

@UtilityClass
public class JWTUtils {

    public String sign(Object payload) {
        try {
            return Jwts.builder()
                    .setSubject(JSON.toJSONString(payload))
                    .signWith(SignatureAlgorithm.RS256, RSAUtils.getPrivateKey())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 60000 * 60))
                    .compact();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return "";
        }
    }

    public boolean verify(String payload, String encryptKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(RSAUtils.getPublicKey(encryptKey))
                    .isSigned(payload);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
    }

    @SuppressWarnings("java:S5659")
    public Object verifyPayload(String payload, String encryptKey) {
        try {
            if (verify(payload, encryptKey)) {
                Map<String, String> map = DataUtils.parseObject(DataUtils.toJsonString(Jwts.parser()
                        .setSigningKey(RSAUtils.getPublicKey(encryptKey))
                        .parse(payload).getBody()));
                return DataUtils.parseObject(map.get("sub"));
            }
            return null;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }
}
