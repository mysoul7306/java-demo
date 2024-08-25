/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtil implements PasswordEncoder {

    @Override
    public String encode(CharSequence raw) {
        if (StringUtils.isBlank(raw.toString())) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        return this.getSecurePw(raw.toString());
    }

    @Override
    public boolean matches(CharSequence raw, String enc) {
        return StringUtils.equals(this.encode(raw), enc);
    }

    private String getSecurePw(String raw) {
        final String ALGO = "SHA-512";
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(ALGO);
            md.update(Utf8.encode(BCrypt.gensalt()));
            for (byte b : md.digest(raw.getBytes())) {
                result.append(Integer.toHexString(0xFF & b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return result.toString();
    }

}
