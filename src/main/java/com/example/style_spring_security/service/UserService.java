package com.example.style_spring_security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.style_spring_security.entity.User;

public interface UserService extends IService<User> {

    User getByUsername(String userName);
}
