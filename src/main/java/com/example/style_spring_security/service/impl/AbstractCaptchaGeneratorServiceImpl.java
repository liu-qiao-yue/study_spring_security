package com.example.style_spring_security.service.impl;

import com.example.style_spring_security.service.CaptchaGeneratorService;
import com.example.style_spring_security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractCaptchaGeneratorServiceImpl implements CaptchaGeneratorService {

    protected final static Long CAPTCHA_EXPIRE_TIME = 60 * 60 * 1000L;


    @Autowired
    RedisUtil redisUtil;
}
