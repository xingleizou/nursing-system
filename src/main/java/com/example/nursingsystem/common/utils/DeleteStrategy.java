package com.example.nursingsystem.common.utils;

/**
 * 删除策略枚举
 * 用于区分不同业务场景的删除方式
 */
public enum DeleteStrategy {
    
    /**
     * 逻辑删除 - 适用于核心业务数据
     * 场景：用户、病人、护理记录等需要追溯的数据
     */
    LOGIC("logic", "逻辑删除 (保留历史记录)"),
    
    /**
     * 物理删除 - 适用于临时数据或 GDPR 合规
     * 场景：临时日志、过期数据、用户注销等
     */
    PHYSICAL("physical", "物理删除 (彻底删除)");
    
    private final String code;
    private final String description;
    
    DeleteStrategy(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}
