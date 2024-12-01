package com.example.style_spring_security.annotation;

import com.example.style_spring_security.enums.MaskType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要掩码处理的字段。
 * @author ellie
 */
@SuppressWarnings("ALL")
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskInfo {

    MaskType type() default MaskType.FULL;
}