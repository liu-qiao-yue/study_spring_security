package com.example.style_spring_security.utils;

import com.example.style_spring_security.exception.CaptchaException;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 随机生成验证码工具类
 */
@Slf4j
public class NumberCaptchaGenerateUtil {

    private NumberCaptchaGenerateUtil(){
        throw new IllegalStateException("Utility class");
    }

    private static final Random RANDOM = new Random();
    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static String generateValidateCode(int length){
        if (length < 4) {
            throw new CaptchaException("验证码长度至少4位");
        }

        int code = RANDOM.nextInt(maxRandom(length));
        int minRandom = minRandom(length);
        if (code < minRandom){
            code = code + minRandom;
        }
        return String.valueOf(code);
    }


    private static int maxRandom(int length) {
        return (int) Math.pow(10, length) - 1;
    }

    private static int minRandom(int length) {
        return (int) Math.pow(10, length - 1);
    }
}
