package com.example.style_spring_security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @Description: (这里用一句话描述这个类的作用)
 * @author: xiazhengwei
 * @date:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -779622177004774348L;

    private String role;

    public CustomGrantedAuthority() {}

    public CustomGrantedAuthority(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof SimpleGrantedAuthority) {
            return role.equals(((CustomGrantedAuthority) obj).role);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
