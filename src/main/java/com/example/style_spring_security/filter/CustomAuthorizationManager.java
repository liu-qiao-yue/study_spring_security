package com.example.style_spring_security.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 自定义授权管理器，用于处理请求的权限检查。
 */
@Slf4j
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final Map<String, Collection<ConfigAttribute>> resourceMap;

    /**
     * 构造函数，初始化资源映射。
     *
     * @param resourceMap 资源路径与权限配置的映射
     */
    public CustomAuthorizationManager(Map<String, Collection<ConfigAttribute>> resourceMap) {
        this.resourceMap = resourceMap;
    }

    /**
     * 检查请求是否被允许访问。
     *
     * @param authentication 用户认证信息的提供者
     * @param context 请求授权上下文
     * @return 授权决策
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        Authentication auth = authentication.get();
        log.info(">> resourceMap: {}", resourceMap);
        // 添加日志记录
        log.info(">> CustomAuthorizationManager check 执行 {}", request.getRequestURI());
        // 查找匹配的资源路径
        Optional<Map.Entry<String, Collection<ConfigAttribute>>> matchingEntry = resourceMap.entrySet().stream()
                .filter(entry -> new AntPathRequestMatcher(entry.getKey()).matches(request))
                .findFirst();

        if (matchingEntry.isPresent()) {
            // 获取所需的权限配置
            Collection<ConfigAttribute> requiredAttributes = matchingEntry.get().getValue();
            for (ConfigAttribute attribute : requiredAttributes) {
                for (GrantedAuthority authority : auth.getAuthorities()) {
                    if (authority.getAuthority().equals(attribute.getAttribute())) {
                        return new AuthorizationDecision(true); // 允许访问
                    }
                }
            }
            return new AuthorizationDecision(false); // 用户没有所需的权限
        }

        return new AuthorizationDecision(false);
    }
}