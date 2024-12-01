package com.example.style_spring_security.service.impl;

import com.example.style_spring_security.entity.Captcha;
import com.example.style_spring_security.exception.CaptchaException;
import com.example.style_spring_security.service.CaptchaGeneratorService;
import com.example.style_spring_security.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private List<CaptchaGeneratorService> captchaGeneratorServices;
    @Override
    public Captcha getCaptcha(String captchaType) {
        return captchaGeneratorServices
                .stream()
                .filter(service -> service.support(captchaType))
                .findFirst()
                .orElseThrow(() -> new CaptchaException("验证码类型错误"))
                .generate();
    }
}
