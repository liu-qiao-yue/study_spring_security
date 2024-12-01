package com.example.style_spring_security.config;

import com.example.style_spring_security.filter.CaptchaFilter;
import com.example.style_spring_security.filter.CustomAuthorizationManager;
import com.example.style_spring_security.filter.JwtAuthenticationFilter;
import com.example.style_spring_security.filter.SystemSecurityMetadataSource;
import com.example.style_spring_security.handle.*;
import com.example.style_spring_security.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
            "/error"
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


    /**
     * SecurityFilterChain 的 Bean 定义。
     * 配置应用程序的HTTP安全设置。
     *
     * @param http                           要配置的HttpSecurity对象。
     * @param authenticationManager          安全链使用的认证管理器。
     * @param loginSuccessHandler            注入处理登录成功的处理器
     * @param jwtLogoutSuccessHandler        注入处理登出成功的处理器
     * @param loginFailureHandler            注入处理登录失败的处理器
     * @param captchaFilter                  注入处理验证码验证的过滤器
     * @param jwtAuthenticationEntryPoint    注入处理认证异常的入口点
     * @param jwtAccessDeniedHandler         注入处理访问拒绝异常的处理器
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   LoginSuccessHandler loginSuccessHandler,
                                                   JWTLogoutSuccessHandler jwtLogoutSuccessHandler,
                                                   LoginFailureHandler loginFailureHandler,
                                                   CaptchaFilter captchaFilter,
                                                   JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                                   JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                                   JwtUtils jwtUtils,
//                                                   UserDetailServiceImpl userDetailService,
//                                                   UserService sysUserService,
                                                   RedisUtil redisUtil
    ) throws Exception {
        http.cors(Customizer.withDefaults()) // 启用默认的跨域资源共享（CORS）配置
                .csrf(AbstractHttpConfigurer::disable) // 禁用跨站请求伪造（CSRF）
                .formLogin(formLogin -> formLogin // 启用表单登录配置
                        .successHandler(loginSuccessHandler) // 设置登录成功处理器
                        .failureHandler(loginFailureHandler)) // 设置登录失败处理器
                .logout(logout -> logout // 启用登出配置
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)) // 设置登出成功处理器
                .sessionManagement(sessionManagement -> sessionManagement // 启用会话管理配置
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 设置无状态会话策略
                .exceptionHandling(exceptionHandling -> exceptionHandling // 异常处理配置
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 设置认证异常入口点
                        .accessDeniedHandler(jwtAccessDeniedHandler)) // 设置访问拒绝处理器
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class) // 在用户名密码过滤器之前添加验证码过滤器
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtUtils, redisUtil)) // 添加JWT认证过滤器
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URL_WHITELIST).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/**"))
                        .access(new CustomAuthorizationManager(systemSecurityMetadataSource().getResourceMap()))
                );
        return http.build(); // 构建并返回配置好的 SecurityFilterChain
    }

    /**
     * AuthenticationManager 的 Bean 定义。
     * 使用 AuthenticationConfiguration 获取 AuthenticationManager 实例。
     *
     * @param authenticationConfiguration 用于获取 AuthenticationManager 的配置对象。
     * @return 构建好的 AuthenticationManager。
     * @throws Exception 如果在构建过程中发生错误。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // 使用 AuthenticationConfiguration 获取 AuthenticationManager 实例
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SystemSecurityMetadataSource systemSecurityMetadataSource() {
        return new SystemSecurityMetadataSource();
    }
}