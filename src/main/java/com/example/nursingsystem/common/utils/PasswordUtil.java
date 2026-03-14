package com.example.nursingsystem.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密码加密工具类
 * 用于生成和验证 BCrypt 加密密码
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 主方法 - 用于生成 BCrypt 密码
     * 用法：将需要加密的密码作为参数传入，或直接修改下面的示例密码
     */
    public static void main(String[] args) {
        // 示例：生成 admin123 的 BCrypt 密码
        String rawPassword = "admin123";
        String encodedPassword = encode(rawPassword);
        
        System.out.println("原始密码：" + rawPassword);
        System.out.println("BCrypt 加密后：" + encodedPassword);
        System.out.println("验证结果：" + matches(rawPassword, encodedPassword));
        
        // 可以添加更多密码进行加密
        System.out.println("\n--- 常用密码加密示例 ---");
        String[] passwords = {"123456", "password", "admin", "nurse123"};
        for (String pwd : passwords) {
            System.out.println(pwd + " -> " + encode(pwd));
        }
    }
}
