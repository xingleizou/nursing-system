package com.example.nursingsystem.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.nursingsystem.common.constant.SystemConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
public class JwtUtil {

    /**
     * 创建 Token
     */
    public static String createToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return JWT.create()
            .withClaim("claims", claims)
            .withExpiresAt(new Date(System.currentTimeMillis() + SystemConstant.TOKEN_EXPIRE_TIME * 1000))
            .sign(Algorithm.HMAC256(SystemConstant.TOKEN_SECRET));
    }

    /**
     * 验证 Token
     */
    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SystemConstant.TOKEN_SECRET)).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析 Token
     */
    public static Map<String, Object> parseToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SystemConstant.TOKEN_SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("claims").asMap();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取用户 ID
     */
    public static String getUserIdFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        if (claims != null && claims.containsKey("userId")) {
            return claims.get("userId").toString();
        }
        return null;
    }

    /**
     * 从 Token 中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        if (claims != null && claims.containsKey("username")) {
            return claims.get("username").toString();
        }
        return null;
    }
}
