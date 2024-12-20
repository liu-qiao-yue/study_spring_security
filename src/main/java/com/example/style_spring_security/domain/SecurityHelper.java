package com.example.style_spring_security.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityHelper {

    private SecurityHelper(){}
    /**
     * 取得用户信息
     *
     * @return 用户信息
     */
    public static AccountUser getUserInfo() {
        AccountUser userInfo = null;
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authInfo = context.getAuthentication();
            if (authInfo != null) {
                userInfo = (AccountUser) authInfo.getPrincipal();
            }
        }
        if (userInfo == null){
            userInfo = UserInfoHolder.getUserInfo();
        }
        return userInfo;
    }
}
