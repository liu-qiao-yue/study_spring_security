package com.example.style_spring_security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.style_spring_security.entity.User;
import com.example.style_spring_security.mapper.UserMapper;
import com.example.style_spring_security.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{


    @Override
    public User getByUsername(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
