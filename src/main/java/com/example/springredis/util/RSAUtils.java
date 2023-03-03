package com.example.springredis.util;

import com.alibaba.fastjson2.JSON;
import com.example.springredis.core.RSARequest;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

@UtilityClass
@Log4j2
@SuppressWarnings("all")
public class RSAUtils {

    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    public String encryptData(String data) {
        try {
            return new String(Base64.getEncoder().encode(encrypt(data)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeySpecException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return "";
    }


     public String encryptData(RSARequest request) {
        try {
            return new String(Base64.getEncoder().encode(encrypt(JSON.toJSONString(request.getData()), request.getEncryptKey())));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeySpecException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return "";
    }



    public <T extends Object> T decryptData(String encrypt, Class<T> type) {
        try {
            String decryptedData = decrypt(encrypt);
            return JSON.parseObject(decryptedData, type);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String readFile(String path) {
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            String content = scanner.next();
            scanner.close();
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPrivateKeyString() {
        String privateKey = readFile("private_key.pem");
        if (StringUtils.isBlank(privateKey)) return privateKey;
        else return privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----","")
                .replace("\r", "").replace("\n","");
    }

    private String getPublicKeyString() {
        String privateKey = readFile("public_key.pem");
        if (StringUtils.isBlank(privateKey)) return privateKey;
        else return privateKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----","")
                .replace("\r", "").replace("\n","");
    }

    private Key getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyBytes = Base64.getDecoder().decode(getPublicKeyString());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    private Key getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = Base64.getDecoder().decode(getPrivateKeyString());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }


    private byte[] encrypt(String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(getPublicKeyString()));
        return cipher.doFinal(data.getBytes());
    }

    private byte[] encrypt(String data, String encryptKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(encryptKey));
        return cipher.doFinal(data.getBytes());
    }

    private String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    private String decrypt(String data) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), (PrivateKey) getPrivateKey());
    }
}
