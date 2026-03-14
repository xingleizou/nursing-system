package com.example.nursingsystem.common.constant;

/**
 * 系统常量
 */
public class SystemConstant {

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 用户登录令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户 ID
     */
    public static final String USER_ID = "userId";

    /**
     * 用户名
     */
    public static final String USERNAME = "username";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * Token 有效期（秒）
     */
    public static final long TOKEN_EXPIRE_TIME = 2 * 60 * 60;

    /**
     * Token 秘钥
     */
    public static final String TOKEN_SECRET = "nursing-smart-system-secret-key";
}
