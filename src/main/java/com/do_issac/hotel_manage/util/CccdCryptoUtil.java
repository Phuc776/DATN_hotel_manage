package com.do_issac.hotel_manage.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CccdCryptoUtil {

    private static final String ALGO = "AES";

    @Value("${security.cccd.secret}")
    private String secretKey;

    public String encrypt(String raw) {
        try {
            System.out.println("AES key length = " + secretKey.getBytes(StandardCharsets.UTF_8).length);
            System.out.println("Secret key: " + secretKey);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(raw.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Encrypt CCCD failed", e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception e) {
            throw new RuntimeException("Decrypt CCCD failed", e);
        }
    }
}

