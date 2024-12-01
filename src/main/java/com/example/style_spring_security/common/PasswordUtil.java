package com.example.style_spring_security.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 * @author ellie
 */
public class PasswordUtil {

    PasswordUtil(){
        throw new IllegalStateException("Utility class");
    }
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 密码比对
     * @param rawPassword 原始密码
     * @param encodedPassword 加密密码
     * @return
     */
    public static boolean matches(String rawPassword, String encodedPassword){
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    /**
     * 密码加密
     * @param rawPassword 原始密码
     * @return
     */
    public static String encodePassword(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }
}
