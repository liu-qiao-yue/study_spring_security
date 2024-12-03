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
            throw new UsernameNotFoundException("用户名或密码错误");
        }


        // 5.2 把对应的用户信息包括权限信息封装成UserDetails对象
        return new AccountUser(user, getUserAuthorities(user.getId()));
    }

    public List<GrantedAuthority> getUserAuthorities(Long userId) {
        List<String> resources = sysResourceMapper.selectResourcesByUserId(String.valueOf(userId));
        List<GrantedAuthority> authorities = Lists.newArrayList();
        resources.forEach(resource -> authorities.add(new CustomGrantedAuthority(resource)));
        return authorities;
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        List<User> users = userMapper.selectList(queryWrapper);

        if (users.size() != 1){
            throw new UsernameNotFoundException("用户信息异常");
        }

        User user = users.getFirst();

        return new AccountUser(user, getUserAuthorities(user.getId()));
    }

}
