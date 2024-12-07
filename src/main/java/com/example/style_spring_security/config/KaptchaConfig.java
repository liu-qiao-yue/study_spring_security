package com.example.style_spring_security.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

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
        properties.put("kaptcha.textproducer.font.color", "black");
        // 验证码文本字符大小
        properties.put("kaptcha.textproducer.char.space", "4");
        //验证码图片高
        properties.put("kaptcha.image.height", "40");
        //验证码图片宽
        properties.put("kaptcha.image.width", "120");
        // 验证码文本字符大小
        properties.put("kaptcha.textproducer.font.size", "30");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
