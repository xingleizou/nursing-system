package com.example.nursingsystem.system.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
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
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 性别 (0:男 1:女 2:未知)
     */
    private Integer gender;

    /**
     * 状态 (0:禁用 1:正常)
     */
    private Integer status;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 角色 ID
     */
    private Long roleId;
}
