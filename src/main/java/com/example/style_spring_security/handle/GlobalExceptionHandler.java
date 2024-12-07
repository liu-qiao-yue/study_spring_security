package com.example.style_spring_security.handle;

import com.example.style_spring_security.common.R;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-06
 * @Description: 异常处理器
 * @Version: 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<R<String>> handleJwtException(JwtException jwtException){
        return new ResponseEntity<>(R.error(jwtException.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
