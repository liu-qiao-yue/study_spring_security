package com.example.style_spring_security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beust.jcommander.internal.Lists;
import com.example.style_spring_security.domain.AccountUser;
import com.example.style_spring_security.domain.CustomGrantedAuthority;
import com.example.style_spring_security.entity.User;
import com.example.style_spring_security.mapper.SysResourceMapper;
import com.example.style_spring_security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/***
 * Spring Boot Security 通过数据库查询 user 信息
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * security 根据用户名查询用户信息
     * 5. 调用 loadUserByUsername 方法查看用户
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 5.1 查询用户信息和权限信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }


        // 查询用户的权限信息
        List<GrantedAuthority> authorities = getUserAuthorities(user.getId());

        // 5.2 把对应的用户信息包括权限信息封装成UserDetails对象
        return new AccountUser(user, authorities);
    }

    public List<GrantedAuthority> getUserAuthorities(Long userId) {
        List<String> resources = sysResourceMapper.selectResourcesByUserId(String.valueOf(userId));
        List<GrantedAuthority> authorities = Lists.newArrayList();
        resources.forEach(resource -> authorities.add(new CustomGrantedAuthority(resource)));
        return authorities;
    }
}
