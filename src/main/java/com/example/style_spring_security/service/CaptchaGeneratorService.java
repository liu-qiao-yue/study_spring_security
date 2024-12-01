package com.example.style_spring_security.service;

import com.example.style_spring_security.entity.Captcha;

public interface CaptchaGeneratorService {
    /**
     * 判断是否支持
     * @param captchaType 验证码类型
     * @return
     */
    boolean support(String captchaType);

    /**
     * 生成验证码
     * @return Captcha
     */
    Captcha generate();
}
