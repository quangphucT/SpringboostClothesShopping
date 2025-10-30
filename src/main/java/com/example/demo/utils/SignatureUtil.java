package com.example.demo.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SignatureUtil {
    public static String generateSignature(Map<String, Object> data, String checksumKey) {
        // Bỏ signature nếu có
        Map<String, Object> filteredData = new HashMap<>(data);
        filteredData.remove("signature");

        // 1️⃣ Sắp xếp theo alphabet
        List<String> keys = new ArrayList<>(filteredData.keySet());
        Collections.sort(keys);

        // 2️⃣ Ghép lại
        StringBuilder rawData = new StringBuilder();
        for (String key : keys) {
            Object value = filteredData.get(key);
            if (value != null) {
                if (rawData.length() > 0) rawData.append("&");
                rawData.append(key).append("=").append(value.toString());
            }
        }

        // 3️⃣ HMAC-SHA256
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(checksumKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(rawData.toString().getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error creating signature", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}