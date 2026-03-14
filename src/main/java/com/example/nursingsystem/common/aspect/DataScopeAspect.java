package com.example.nursingsystem.common.aspect;

import com.example.nursingsystem.common.annotation.DataScope;
import com.example.nursingsystem.system.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据权限切面
 * 用于在 Service 层方法上自动注入数据权限过滤条件
 */
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 切入点：所有带有@DataScope 注解的方法
     */
    @Pointcut("@annotation(com.example.nursingsystem.common.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    /**
     * 前置通知：解析@DataScope 注解，设置数据权限过滤
     */
    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取方法签名
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        
        // 获取方法上的@DataScope 注解
        Method method = methodSignature.getMethod();
        DataScope dataScope = method.getAnnotation(DataScope.class);
        
        if (dataScope != null) {
            // 获取部门表别名和用户表别名
            String deptAlias = dataScope.deptAlias();
            String userAlias = dataScope.userAlias();
            
            // TODO: 从登录上下文获取当前用户信息
            // 这里需要根据实际的用户认证系统来获取当前登录用户
            User currentUser = getCurrentUser();
            
            if (currentUser != null) {
                // 根据角色的 dataScope 字段值，构造不同的 SQL 过滤条件
                // 这个逻辑需要在 MyBatis 拦截器或者 XML 中实现
                // 这里只是示例，具体实现需要配合 MyBatis 拦截器
                
                // 1. 全部数据权限（dataScope = "1"）- 无需过滤
                // 2. 自定义数据权限（dataScope = "2"）- AND dept_id IN (SELECT dept_id FROM sys_role_dept WHERE role_id = ?)
                // 3. 本部门数据权限（dataScope = "3"）- AND dept_id = ?
                // 4. 本部门及以下数据权限（dataScope = "4"）- AND dept_id IN (SELECT id FROM sys_dept WHERE ancestors LIKE '%当前部门%')
                // 5. 仅本人数据权限（dataScope = "5"）- AND create_by = ?
                
                // 注意：具体的 SQL 拼接需要在 MyBatis XML 或者通过拦截器实现
                // 这里只是标记该方法需要数据权限控制
            }
        }
    }

    /**
     * 获取当前登录用户
     * TODO: 需要从 Spring Security 或 JWT Token 中获取当前登录用户
     */
    private User getCurrentUser() {
        // 这里需要从安全上下文中获取当前用户
        // 例如：SecurityContextHolder.getContext().getAuthentication()
        // 或者从 Redis 中解析 JWT Token 获取用户信息
        // 暂时返回 null，后续需要集成认证系统
        return null;
    }
}
