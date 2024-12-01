package com.example.style_spring_security.domain;

public abstract class UserInfoHolder {

    private UserInfoHolder() {
    }

    private static final ThreadLocal<AccountUser> context = new ThreadLocal<>();

    static AccountUser getUserInfo() {
        return context.get();
    }

    public static void setUserInfo(AccountUser accountUser) {
        context.set(accountUser);
    }

    /**
     * 清除上下文
     */
    public static void clearContext() {
        context.remove();
    }

}
