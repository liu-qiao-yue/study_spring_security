package com.example.style_spring_security.emailLogin;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-02
 * @Description: 登录认证令牌，仿照 UsernamePasswordAuthenticationToken
 * @Version: 1.0
 */
public class EmailCodeAuthenticationToken extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = -8530245044378613589L;

    /**
     * 这里的 principal 指的是 email 地址（未认证的时候）
     */
    private final Object principal;

    /**
     没经过身份验证时，初始化权限为空，setAuthenticated(false)设置为不可信令牌
     */
    public EmailCodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    /**
     经过身份验证后，将权限放进去，setAuthenticated(true)设置为可信令牌
     */
    public EmailCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }
}
