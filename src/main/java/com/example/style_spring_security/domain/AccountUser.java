package com.example.style_spring_security.domain;

import com.example.style_spring_security.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

/**
 * 封装登录用户信息
 * @author ellie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUser implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1549501681186501846L;

    private User user;

    private List<GrantedAuthority> authorities;

    /**
     * 权限列表
     * @return 权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    /**
     * 账号是否过期
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否锁定
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 证书是否过期
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否可用
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
