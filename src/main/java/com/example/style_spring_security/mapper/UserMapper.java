package com.example.style_spring_security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.style_spring_security.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
