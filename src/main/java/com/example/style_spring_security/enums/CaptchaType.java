package com.example.style_spring_security.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 枚举类，定义了不同类型的验证码。
 * 每个枚举常量代表一种验证码类型，并且有一个对应的唯一标识码。
 */
@Getter
public enum CaptchaType {
    /**
     * 数字字母验证码。
     * 通常用于需要用户输入数字和字母组合的场景。
     */
    ALPHANUMERIC("01"),

    /**
     * 短信验证码。
     * 通过短信发送给用户的验证码，用户需要在指定时间内输入以验证身份。
     */
    SMS("02"),

    /**
     * 邮件验证码。
     * 通过电子邮件发送给用户的验证码，用户需要在指定时间内输入以验证身份。
     */
    EMAIL("03"),

    /**
     * 滑动验证码。
     * 用户需要通过滑动操作来完成验证，通常用于防止自动化脚本攻击。
     */
    SLIDER("04"),

    /**
     * 旋转验证码。
     * 用户需要通过旋转操作来完成验证，通常用于增加验证码的复杂度。
     */
    ROTATION("05"),

    /**
     * 点击验证码。
     * 用户需要点击特定的图片或元素来完成验证，通常用于增加验证码的互动性。
     */
    CLICK("06");

    private final String typeCode;

    CaptchaType(String typeCode) {
        this.typeCode = typeCode;
    }

    public static boolean isMatchingCaptchaType(String value, CaptchaType captchaType){
        return StringUtils.equals(value, captchaType.getTypeCode())
                || StringUtils.equals(value, captchaType.name());
    }

}
