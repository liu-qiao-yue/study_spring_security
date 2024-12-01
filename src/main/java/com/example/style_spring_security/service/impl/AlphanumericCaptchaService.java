package com.example.style_spring_security.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.UUID;
import com.example.style_spring_security.entity.Captcha;
import com.example.style_spring_security.enums.CaptchaType;
import com.example.style_spring_security.exception.CaptchaException;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 数字字母验证码生成服务
 */
@Slf4j
@Service
public class AlphanumericCaptchaService extends AbstractCaptchaGeneratorServiceImpl{

    private final Producer producer;

    public AlphanumericCaptchaService(Producer producer) {
        this.producer = producer;
    }

    @Override
    public boolean support(String captchaType) {
        return CaptchaType.isMatchingCaptchaType(captchaType, CaptchaType.ALPHANUMERIC);
    }

    @Override
    public Captcha generate() {
        // 生成随机Redis key
        String key = UUID.randomUUID().toString();
        // 生成验证码
        String code = producer.createText();
        // 生成图片
        BufferedImage image = producer.createImage(code);
        // 转换为base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 将图片转为base64
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            throw new CaptchaException("生成验证码异常", e);
        }
        String str = "data:image/jpg;base64,";
        // 将图片转为base64
        String base64Img = str + Base64Encoder.encode(outputStream.toByteArray());

        redisUtil.setCacheObject(key, code, CAPTCHA_EXPIRE_TIME);

        log.info("验证码：{}，key: {}", code, key);
        return Captcha.builder()
                .captchaKey(key)
                .captchaImg(base64Img)
                .build();
    }
}
