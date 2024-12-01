package com.example.style_spring_security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {
    /**
     * 验证码key
     */
    private String captchaKey;
    /**
     * 验证码图片 base64
     */
    private String captchaImg;
}
