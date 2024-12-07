package com.example.style_spring_security.config;

import com.example.style_spring_security.emailLogin.EmailCodeAuthenticationFilter;
import com.example.style_spring_security.emailLogin.EmailCodeAuthenticationProvider;
import com.example.style_spring_security.filter.CaptchaFilter;
import com.example.style_spring_security.filter.CustomAuthorizationManager;
import com.example.style_spring_security.filter.JwtAuthenticationFilter;
import com.example.style_spring_security.filter.SystemSecurityMetadataSource;
import com.example.style_spring_security.handle.*;
import com.example.style_spring_security.service.impl.UserDetailServiceImpl;
import com.example.style_spring_security.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 配置类。
 * 该类配置了应用程序的安全设置，
 * 包括身份验证、授权、会话管理和自定义过滤器。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {


    /**
     * 白名单URL列表，这些URL不需要身份验证。
     */
    private static final String[] URL_WHITELIST = {
            "/login",       // 登录接口
            "/logout",      // 登出接口
            "/captcha",     // 验证码接口
            "/captcha/**",
            "/error",
            "/getCaptcha"
    };

    /**
     * PasswordEncoder 的 Bean 定义。
     * 提供一个使用BCrypt算法的密码编码器实现。
     *
     * @return BCryptPasswordEncoder 实例。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCryptPasswordEncoder来加密密码
        return new BCryptPasswordEncoder(); // 返回 BCryptPasswordEncoder 实例
    }
    @Bean
    public SystemSecurityMetadataSource systemSecurityMetadataSource() {
        return new SystemSecurityMetadataSource();
    }

    /**
     * username 密码登录的 DaoAuthenticationProvider 的 Bean 定义。
     * @param userDetailService
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * email 密码登录的 DaoAuthenticationProvider 的 Bean 定义。
     * @param userService
     * @return
     */
    @Bean
    public EmailCodeAuthenticationProvider emailCodeAuthenticationProvider(UserDetailServiceImpl userService) {
        return new EmailCodeAuthenticationProvider(userService);
    }

    /**
     * prover 管理器的 Bean 定义。
     * @param emailCodeAuthenticationProvider
     * @param daoAuthenticationProvider
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(
            EmailCodeAuthenticationProvider emailCodeAuthenticationProvider,
            DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(emailCodeAuthenticationProvider, daoAuthenticationProvider);
    }

    /**
     * email 登录的 EmailCodeAuthenticationFilter 的 Bean 定义。
     * @param loginSuccessHandler
     * @param loginFailureHandler
     * @param authenticationManager
     * @return
     */
    @Bean
    public EmailCodeAuthenticationFilter emailCodeAuthenticationFilter(LoginSuccessHandler loginSuccessHandler,
                                                                       LoginFailureHandler loginFailureHandler,
                                                                       AuthenticationManager authenticationManager) {
        EmailCodeAuthenticationFilter emailCodeAuthenticationFilter = new EmailCodeAuthenticationFilter();
        // 设置认证成功和失败处理器 必须设置
        emailCodeAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        emailCodeAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);
        emailCodeAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return emailCodeAuthenticationFilter;
    }

    /**
     * SecurityFilterChain 的 Bean 定义。
     * 配置应用程序的HTTP安全设置。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   JWTLogoutSuccessHandler jwtLogoutSuccessHandler,
                                                   CaptchaFilter captchaFilter,
                                                   JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                                   JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                                   JwtUtils jwtUtils,
                                                   RedisUtil redisUtil,
                                                   EmailCodeAuthenticationFilter emailCodeAuthenticationFilter,
                                                   LoginSuccessHandler loginSuccessHandler,
                                                   LoginFailureHandler loginFailureHandler
    ) throws Exception {

        http.cors(Customizer.withDefaults()) // 启用默认的跨域资源共享（CORS）配置
                .csrf(AbstractHttpConfigurer::disable) // 禁用跨站请求伪造（CSRF）
                .formLogin(formLogin -> formLogin // 启用表单登录配置 必须设置，否则username登陆时会重定向到/
                        .successHandler(loginSuccessHandler) // 设置登录成功处理器
                        .failureHandler(loginFailureHandler)) // 设置登录失败处理器
                .logout(logout -> logout // 启用登出配置
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)) // 设置登出成功处理器
                .sessionManagement(sessionManagement -> sessionManagement // 启用会话管理配置
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 设置无状态会话策略
                .authenticationManager(authenticationManager)
                .exceptionHandling(exceptionHandling -> exceptionHandling // 异常处理配置
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 设置认证异常入口点
                        .accessDeniedHandler(jwtAccessDeniedHandler)) // 设置访问拒绝处理器
                .addFilterBefore(emailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // 确保在 UsernamePasswordAuthenticationFilter 之前执行
                .addFilterBefore(captchaFilter, EmailCodeAuthenticationFilter.class) // 在用户名密码过滤器之前添加验证码过滤器
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class) // 在用户名密码过滤器之前添加验证码过滤器
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtUtils, redisUtil)) // 添加JWT认证过滤器
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URL_WHITELIST).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/**"))
                        .access(new CustomAuthorizationManager(systemSecurityMetadataSource().getResourceMap()))
                );

        return http.build(); // 构建并返回配置好的 SecurityFilterChain
    }
}