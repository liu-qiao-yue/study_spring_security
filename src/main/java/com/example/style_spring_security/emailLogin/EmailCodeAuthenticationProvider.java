package com.example.style_spring_security.emailLogin;

import com.example.style_spring_security.service.impl.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-02
 * @Description:
 *      身份验证提供者，用于处理电子邮件验证。仿照DaoAuthenticationProvider-》AbstractUserDetailsAuthenticationProvider
 *          ，实际上是实现 AuthenticationProvider
 * @Version: 1.0
 */
@Slf4j
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userService;
    public EmailCodeAuthenticationProvider(UserDetailServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * 认证处理，
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        log.info("EmailCodeAuthentication authentication request: %s", authentication);
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;
        UserDetails user = userService.loadUserByEmail((String) token.getPrincipal());
        log.info("user email path: %s", token.getPrincipal());

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        log.info("user principal: %s", token.getAuthorities());

        EmailCodeAuthenticationToken res = new EmailCodeAuthenticationToken(user, user.getAuthorities());
        res.setDetails(token.getDetails());

        return res;
    }

    /**
     * 判断该认证提供者是否支持该认证类型，即authentication是否是EmailCodeAuthenticationToken类型
     * @param authentication the authentication request object.
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
