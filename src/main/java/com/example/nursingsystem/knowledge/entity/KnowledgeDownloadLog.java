package com.example.nursingsystem.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 下载日志实体类
 */
@Data
@TableName("knowledge_download_log")
public class KnowledgeDownloadLog implements Serializable {

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
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 下载 IP
     */
    private String downloadIp;

    /**
     * 下载时间
     */
    private LocalDateTime downloadTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
