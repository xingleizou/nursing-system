package com.example.nursingsystem.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户实体类
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 (0:男 1:女 2:未知)
     */
    private Integer gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态 (0:禁用 1:正常)
     */
    private Integer status;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志 (0:存在 1:删除)
     */
    @TableLogic
    private Integer deleted;
}
