package com.example.nursingsystem.common.exception;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    
    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请重新登录"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "请求地址不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不正确"),
    
    // 服务端错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    
    // 业务错误
    LOGIN_FAIL(1001, "登录失败"),
    LOGIN_PASSWORD_ERROR(1002, "用户名或密码错误"),
    LOGIN_USER_NOT_EXIST(1003, "用户不存在"),
    LOGIN_TOKEN_EXPIRED(1004, "Token 已过期"),
    LOGIN_TOKEN_INVALID(1005, "Token 无效"),
    
    USER_HAS_EXIST(1101, "用户已存在"),
    USER_NOT_EXIST(1102, "用户不存在"),
    USER_PASSWORD_ERROR(1103, "密码错误"),
    USER_STATUS_DISABLED(1104, "用户已被禁用"),
    
    PERMISSION_DENIED(1201, "权限不足"),
    ROLE_NOT_EXIST(1202, "角色不存在"),
    
    DATA_NOT_EXIST(1301, "数据不存在"),
    DATA_ALREADY_EXIST(1302, "数据已存在"),
    DATA_OPERATION_FAILED(1303, "数据操作失败");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
