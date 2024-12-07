package com.example.style_spring_security.utils;

import com.example.style_spring_security.domain.AuthenticatorDomain;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-06
 * @Description: 邮件工具类
 * @Version: 1.0
 */
public class MailUtils {

    private MailUtils(){}
    /**
     * 发送邮件的主机
     */
    private static final String EMAIL_HOST = "smtp.aliyun.com";
    /**
     * 发件人邮箱
     */
    private static final String FORM_ADDRESS = "qiao-yue.liu@aliyun.com";
    /**
     * 发件人邮箱授权码
     */
    private static final String AUTH_CODE = "87939705Lqy.";

    private static final String PORT = "465";

    /**
     * 发送邮件
     * @param subject 邮件主题
     * @param content 邮件正文
     * @param to       收件人，多个邮箱地址以半角逗号分隔
     */
    public static void sendEmail(String subject, String content, String to, String cc){

        validateEmail(subject, content, to);

        // 创建Session实例
        Session session = getSession();

        try {
            // 创建邮件实例
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FORM_ADDRESS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject,"utf-8"); // 邮件主题
            message.setText(content); // 邮件正文

            if (StringUtils.isNotBlank(cc)){
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }

            // 保存并生成最终的邮件内容
            message.saveChanges();

            // 发送邮件
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Session实例
     * @return Session
     */
    private static Session getSession(){
        // 邮件服务器配置

        Properties props = new Properties();
        props.put("mail.smtp.host", EMAIL_HOST); // SMTP服务器地址 smtp.aliyun.com
        props.put("mail.smtp.port", PORT); // SMTP服务器端口 465
        props.put("mail.smtp.auth", "true"); // 是否需要身份验证
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        props.put("mail.smtp.socketFactory.port", PORT); // 465
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 创建密码验证器
        AuthenticatorDomain auth = new AuthenticatorDomain(FORM_ADDRESS, AUTH_CODE);

        // 创建Session实例
        return Session.getInstance(props, auth);
    }


    private static void validateEmail(String subject, String content, String to){
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(content) || StringUtils.isBlank(to))
            throw new IllegalArgumentException("发送邮件时缺少必穿参数");
    }

//    public static void main(String[] args) {
//        sendEmail("测试邮件主题","测试邮件正文","qiao-yue.liu@outlook.com","");
//    }
}
