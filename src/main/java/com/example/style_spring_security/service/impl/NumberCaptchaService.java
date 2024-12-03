package com.example.style_spring_security.service.impl;

import com.example.style_spring_security.entity.Captcha;
import com.example.style_spring_security.enums.CaptchaType;
import com.example.style_spring_security.utils.NumberCaptchaGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-02
 * @Description: 数字验证码服务器，用于验证码生成、验证。用于短信、邮件等场景
 * @Version: 1.0
 */
@Slf4j
@Service
public class NumberCaptchaService extends AbstractCaptchaGeneratorServiceImpl {
    @Override
    public boolean support(String captchaType) {
        return  CaptchaType.isMatchingCaptchaType(captchaType, CaptchaType.NUMBER);
    }

    @Override
    public Captcha generate() {
        // 生成随机Redis key
        String key = UUID.randomUUID().toString();
        Integer code = NumberCaptchaGenerateUtil.generateValidateCode(4);

        redisUtil.setCacheObject(key, code, CAPTCHA_EXPIRE_TIME);
        log.info("验证码：{}，key: {}", code, key);

        //todo 发送验证码
        return Captcha.builder()
                .captchaKey(key)
                .build();
    }
}
