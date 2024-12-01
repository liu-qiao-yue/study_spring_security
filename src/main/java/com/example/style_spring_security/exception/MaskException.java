package com.example.style_spring_security.exception;

import java.io.Serial;

/**
 * 自定义异常用于拦截敏感信息
 * @author ellie
 */
public class MaskException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MaskException(String message) {
        super(message);
    }
}
