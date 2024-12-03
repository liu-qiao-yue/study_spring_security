package com.example.style_spring_security.filter;

import com.example.style_spring_security.enums.CaptchaType;
import com.example.style_spring_security.exception.CaptchaException;
import com.example.style_spring_security.handle.LoginFailureHandler;
import com.example.style_spring_security.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证码过滤器
 * 在验证码过滤器中，需要先判断请求是否是登录请求，
 * 若是登录请求，则进行验证码校验，从redis中通过userKey查找对应的验证码，看是否与前端所传验证码参数一致，
 * 当校验成功时，因为验证码是一次性使用的，一个验证码对应一个用户的一次登录过程，所以需用hdel将存储的HASH删除。
 * 当校验失败时，则交给登录认证失败处理器LoginFailureHandler进行处理
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    // 定义一个正则表达式来匹配 /login 或 /**/login
    private static final Pattern LOGIN_PATH_PATTERN = Pattern.compile("^/.*/login$|^/login$");

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        if (isLoginUrl(url) && httpServletRequest.getMethod().equals("POST")) {
            // 校验验证码
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 校验验证码逻辑
     * @param httpServletRequest
     */
    private void validate(HttpServletRequest httpServletRequest) {
        // 获取验证码类型
        String validateTypeCode = httpServletRequest.getParameter("validateType");

        CaptchaType captchaType = CaptchaType.getCaptchaTypeByTypeCode(validateTypeCode);

        String code = httpServletRequest.getParameter(captchaType.getCaptchaKey());
        String key = httpServletRequest.getParameter("userKey");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }
        if (!code.equals(redisUtil.getCacheObject(key))) {
            throw new CaptchaException("验证码错误");
        }
        // 若验证码正确，执行以下语句
        // 一次性使用
        redisUtil.deleteObject(key);
    }

    /**
     * 匹配登录url: /login /email/login等
     * @param url
     * @return
     */
    private static boolean isLoginUrl(String url) {
        Matcher matcher = LOGIN_PATH_PATTERN.matcher(url);
        return matcher.matches();
    }

}
