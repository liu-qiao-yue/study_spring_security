package com.example.style_spring_security.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysResource {
    private Long id; // 关联ID
    private String resourceName; // 资源名称, 以 RES_ 为前缀
    private String resourceDesc; // 描述
    private String apiPath; // API路径
    private String createBy; // 创建者
    private String updateBy; // 最后更新者
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 最后更新时间
}
