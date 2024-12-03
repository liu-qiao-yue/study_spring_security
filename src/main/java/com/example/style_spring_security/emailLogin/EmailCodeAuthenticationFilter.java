package com.example.style_spring_security.emailLogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-02
 * @Description: 邮件验证码登录 filter，仿照 UsernamePasswordAuthenticationFilter
 * @Version: 1.0
 */
@Slf4j
public class EmailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 前端传来的 参数名 - 用于request.getParameter 获取 - 邮箱
     */
    public final static String DEFAULT_EMAIL_NAME = "email";
    /**
     * 前端传来的 参数名 - 用于request.getParameter 获取 - 验证码
     */
    public final static String DEFAULT_EMAIL_CODE = "e_code";

    private static final AntPathRequestMatcher EMAIL_CODE_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/email/login", "POST");


    /**
     * 是否 仅仅post方式
     */
    private final boolean postOnly = true;


    /**
     * 通过 传入的 参数 创建 匹配器
     * 即 Filter过滤的url
     */
    public EmailCodeAuthenticationFilter() {
        super(EMAIL_CODE_PATH_REQUEST_MATCHER);
    }


    /**
     * filter 获得 用户名（邮箱） 和 密码（验证码） 装配到 token 上 ，
     * 然后把token 交给 provider 进行授权
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String email = getEmail(request);
        //封装 token
        EmailCodeAuthenticationToken token = new EmailCodeAuthenticationToken(email, new ArrayList<>());
        this.setDetails(request, token);
        //交给 manager 发证
        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * 获取 头部信息 让合适的provider 来验证他
     */
    public void setDetails(HttpServletRequest request, EmailCodeAuthenticationToken token) {
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 获取 传来 的Email信息
     */
    private String getEmail(HttpServletRequest request) {
        return Optional.ofNullable(request.getParameter(DEFAULT_EMAIL_NAME)).orElse("").trim();
    }

}
