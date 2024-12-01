package com.example.style_spring_security.filter;

import com.beust.jcommander.internal.Maps;
import com.example.style_spring_security.entity.SysResource;
import com.example.style_spring_security.mapper.SysResourceMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class SystemSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    @Autowired
    private SysResourceMapper sysResourceMapper;
    private static final Map<String, Collection<ConfigAttribute>> resourceMap = Maps.newHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载所有资源与权限的关系
        if (resourceMap .isEmpty()) {
            List<SysResource> resources = sysResourceMapper.selectList(null);

            if (resources == null || resources.isEmpty()) {
                throw new IllegalArgumentException("No resources found in the database.");
            }

            for (SysResource resource : resources) {
                Collection<ConfigAttribute> configAttributes = new ArrayList<>();
                ConfigAttribute configAttribute = new SecurityConfig(resource.getResourceName());
                configAttributes.add(configAttribute);
                configAttribute = new SecurityConfig(resource.getResourceDesc());
                configAttributes.add(configAttribute);
                resourceMap.put(resource.getApiPath(), configAttributes);
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Iterator<String> it = resourceMap.keySet().iterator();
        RequestMatcher pathMatcher = null;
        while (it.hasNext()) {
            String resURL = it.next();
            pathMatcher = new AntPathRequestMatcher(resURL);
            if (pathMatcher.matches(((FilterInvocation) object).getRequest())) {
                return resourceMap.get(resURL);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    public Map<String, Collection<ConfigAttribute>> getResourceMap() {
        return resourceMap;
    }
}
