package com.example.style_spring_security.handle;

import cn.hutool.json.JSONUtil;
import com.example.style_spring_security.common.R;
import com.example.style_spring_security.config.JwtUtils;
import com.example.style_spring_security.utils.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        // 生成JWT，并放置到请求头中
        String jwt = jwtUtils.createJwt(authentication.getName());
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        redisUtil.setCacheObject(jwt, authentication.getPrincipal(), 60 * 60 * 1000L);// 和jwt保持一致

        outputStream.write(JSONUtil.toJsonStr(R.success("SuccessLogin")).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}