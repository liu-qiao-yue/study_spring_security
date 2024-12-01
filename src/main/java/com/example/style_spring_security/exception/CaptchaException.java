package com.example.style_spring_security.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }

    public CaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}