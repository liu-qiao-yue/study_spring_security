package com.example.style_spring_security.exception;

import lombok.Data;

import java.io.Serial;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-06
 * @Description: 异常顶层类
 * @Version: 1.0
 */
@Data
public class CustomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1112100008832563844L;

    private String code;

    private String msg;

    public CustomException(String message) {
        super(message);
        this.msg = message;
    }

    public CustomException(String message, String code) {
        super(message);
        this.msg = message;
        this.code = code;
    }
}
