package com.example.nursingsystem.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审核日志实体类
 */
@Data
@TableName("knowledge_audit_log")
public class KnowledgeAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志 ID
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /**
     * 资源 ID
     */
    private Long resourceId;

    /**
     * 操作 (1:通过 2:驳回)
     */
    private String auditAction;

    /**
     * 审核意见/驳回原因
     */
    private String auditRemark;

    /**
     * 审核人 ID
     */
    private Long auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
