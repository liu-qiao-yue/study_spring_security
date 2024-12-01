package com.example.style_spring_security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.style_spring_security.entity.SysResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<String> selectResourcesByUserId(String userId);
}
