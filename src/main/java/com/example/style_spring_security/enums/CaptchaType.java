package com.example.style_spring_security.enums;

import com.example.style_spring_security.exception.CaptchaException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 枚举类，定义了不同类型的验证码。
 * 每个枚举常量代表一种验证码类型，并且有一个对应的唯一标识码。
 */
@Getter
public enum CaptchaType {
    /**
     * 数字字母验证码。done
     * 通常用于需要用户输入数字和字母组合的场景。
     */
    ALPHANUMERIC("01", "code"),

    /**
     * 数字验证码。done
     * 邮件等场景。
     */
    EMAIL("02",  "e_code"),

    /**
     * 数字验证码。
     * 短信等场景。
     */
    SMS("06",  "s_code"),

    /**
     * X、Y验证码。
     * 滑动验证码场景。
     */
    SLIDE("03", "s_code"),

    /**
     * 旋转验证码。旋转多少度
     * 用户需要通过旋转操作来完成验证，通常用于增加验证码的复杂度。
     */
    ROTATION("04", "r_code"),

    /**
     * X、Y验证码
     * 单次点击验证码，用户需要点击特定的图片或元素来完成验证，通常用于增加验证码的互动性。
     * 可以是点击几次或多次
     */
    COORDINATE("05", "c_code"),
    /**
     * 数字计算验证码 done
     */
    COMPUTE("07", "c_code");

    private final String typeCode;

    private final String captchaKey;

    CaptchaType(String typeCode, String captchaKey){
        this.typeCode = typeCode;
        this.captchaKey = captchaKey;
    }

    public static boolean isMatchingCaptchaType(String value, CaptchaType captchaType){
        return StringUtils.equals(value, captchaType.getTypeCode())
                || StringUtils.equals(value, captchaType.name());
    }


    public static CaptchaType getCaptchaTypeByTypeCode(String typeCode){
        return Arrays.stream(values())
                .filter(item -> StringUtils.equals(item.getTypeCode(), typeCode) || StringUtils.equals(item.name(), typeCode))
                .findFirst()
                .orElseThrow(()-> new CaptchaException("验证码类型错误"));
    }

}
