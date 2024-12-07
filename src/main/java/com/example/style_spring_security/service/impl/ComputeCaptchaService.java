package com.example.style_spring_security.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import com.example.style_spring_security.entity.Captcha;
import com.example.style_spring_security.entity.User;
import com.example.style_spring_security.enums.CaptchaType;
import com.example.style_spring_security.exception.CaptchaException;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-07
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
public class ComputeCaptchaService extends AbstractCaptchaGeneratorServiceImpl{

    private final Producer producerWithCompute;

    public ComputeCaptchaService(Producer producerWithCompute) {
        this.producerWithCompute = producerWithCompute;
    }
    @Override
    public boolean support(String captchaType) {
        return CaptchaType.isMatchingCaptchaType(captchaType, CaptchaType.COMPUTE);
    }

    @Override
    public Captcha generate(User user) {
        String uuid = UUID.randomUUID().toString();

        // 生成验证码
        String capText = producerWithCompute.createText();
        String capStr = capText.substring(0, capText.lastIndexOf("@"));
        String code = capText.substring(capText.lastIndexOf("@") + 1);

        // 生成图片
        BufferedImage image = producerWithCompute.createImage(capStr);

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

        redisUtil.setCacheObject(uuid, code, CAPTCHA_EXPIRE_TIME);

        log.info("验证码：{}，key: {}", code, uuid);
        return Captcha.builder()
                .captchaKey(uuid)
                .captchaImg(base64Img)
                .build();
    }
}
