package com.example.style_spring_security.service;

import com.example.style_spring_security.entity.Captcha;
import com.example.style_spring_security.entity.User;

public interface CaptchaService {
    /**
     * 获取验证码
     * @param captchaType 验证码类型，例如点击文字验证码、滑动滑块验证码、旋转验证码等
     * @return Captcha
     */
    Captcha getCaptcha(String captchaType, User user);
}
