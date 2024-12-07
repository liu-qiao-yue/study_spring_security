package com.example.style_spring_security.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * 验证码工具类
 */
@Configuration
public class KaptchaConfig {

    /**
     * 数字字母验证码生成器
     * @return
     */
    @Bean
    DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        // 验证码文本字符颜色
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // 文本字符间距
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "4");
        //验证码图片高
        properties.put(KAPTCHA_IMAGE_HEIGHT, "40");
        //验证码图片宽
        properties.put(KAPTCHA_IMAGE_WIDTH, "120");
        // 验证码文本字符大小
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean
    DefaultKaptcha producerWithCompute(){
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty(KAPTCHA_BORDER, "no");
        // 文本颜色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // 图片宽度
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "120");
        // 图片高度
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "40");
        // 文本字符大小
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "28");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "KAPTCHA_SESSION_KEY");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.example.style_spring_security.provider.KaptchaTextCreator");
        // 文本字符间距
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 文本字符长度
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 文本字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 干扰颜色
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;

    }
}
