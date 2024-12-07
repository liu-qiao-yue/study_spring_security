package com.example.style_spring_security.domain;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-06
 * @Description: Authenticator Domain
 * @Version: 1.0
 */
public class AuthenticatorDomain extends Authenticator {

    private String userName;

    private String password;

    public AuthenticatorDomain(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public AuthenticatorDomain(){
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
