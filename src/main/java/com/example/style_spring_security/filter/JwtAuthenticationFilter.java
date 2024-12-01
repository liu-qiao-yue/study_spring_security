package com.example.style_spring_security.filter;

import cn.hutool.core.util.StrUtil;
import com.example.style_spring_security.config.JwtUtils;
import com.example.style_spring_security.domain.AccountUser;
import com.example.style_spring_security.domain.UserInfoHolder;
import com.example.style_spring_security.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtUtils;

    private final RedisUtil redisUtil;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtUtils jwtUtils,
                                   RedisUtil redisUtil) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info(">>> jwtAuthenticationFilter doFilterInternal 执行 {}", request.getRequestURI());
        String jwt = request.getHeader(jwtUtils.getHeader());
        // 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
        if (StrUtil.isBlankOrUndefined(jwt)) {
            log.info(">>> jwtAuthenticationFilter doFilterInternal 无jwt，继续往后走");
            chain.doFilter(request, response);
            return;
        }
        Claims claim = jwtUtils.extractAllClaims(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }


        if (Boolean.TRUE.equals(jwtUtils.isTokenExpired(jwt))) {
            throw new JwtException("token 已过期");
        }
        // 获取用户的权限等信息
        AccountUser accountUser = (AccountUser) redisUtil.getCacheObject(jwt);
        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountUser.getUser(), null, accountUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        UserInfoHolder.setUserInfo(accountUser);
        chain.doFilter(request, response);
    }
}
