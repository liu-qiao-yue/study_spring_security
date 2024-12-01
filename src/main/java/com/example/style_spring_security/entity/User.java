package com.example.style_spring_security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -3562234712331392438L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;
    private String nickName;
    private String password;
    private String status;
    private String email;
    private String sex;
    private String avatar;
    private Integer userType;
    private Date createTime;
    private Date updateTime;
    private Long createBy;
    private Long updateBy;
    private Integer delFlag;

}
