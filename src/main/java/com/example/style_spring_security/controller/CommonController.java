package com.example.style_spring_security.controller;

import com.example.style_spring_security.domain.AccountUser;
import com.example.style_spring_security.entity.Captcha;
import com.example.style_spring_security.enums.CaptchaType;
import com.example.style_spring_security.service.CaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.style_spring_security.domain.SecurityHelper.getUserInfo;

@RestController
public class CommonController {

    private final CaptchaService captchaService;

    public CommonController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/captcha")
    public Captcha hello2() {
        return captchaService.getCaptcha(CaptchaType.ALPHANUMERIC.name());
    }

    @GetMapping("/user/info")
    public AccountUser userInfo() {
        return getUserInfo();
    }


    @GetMapping("/captcha/number")
    public Captcha captchaNumber() {
        return captchaService.getCaptcha(CaptchaType.NUMBER.name());
    }
}
