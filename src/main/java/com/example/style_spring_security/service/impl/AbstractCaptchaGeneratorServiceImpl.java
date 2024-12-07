package com.example.style_spring_security.service.impl;

import com.example.style_spring_security.service.CaptchaGeneratorService;
import com.example.style_spring_security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractCaptchaGeneratorServiceImpl implements CaptchaGeneratorService {

    protected final static Long CAPTCHA_EXPIRE_TIME = 2 * 60 * 1000L;

    protected final static String SMS_SUBJECT = "【瑞吉外卖】验证码通知";

    protected final static String SMS_CONTENT = "尊敬的用户，\n" +
            "\n" +
            "您好！感谢您使用 [YourAppName]。\n" +
            "\n" +
            "为了验证您的邮箱地址，请在2分钟内输入以下验证码完成验证：\n" +
            "----------------------------------------\n" +
            "|            验证码: %s           |\n" +
            "----------------------------------------\n" +
            "\n" +
            "请注意，此验证码仅用于验证邮箱，并且只能使用一次。如果它已过期或您未请求此验证码，请忽略此邮件。\n" +
            "\n" +
            "祝您生活愉快！\n" +
            "\n" +
            "提醒：请勿回复本邮件，该邮箱无人值守。";


    @Autowired
    RedisUtil redisUtil;
}
