package com.example.style_spring_security.entity;

import lombok.Data;

@Data
public class SysRoleResource {
    private Long id; // 关联ID
    private Long resourceId;
    private Long roleId;
}
