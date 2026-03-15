package com.example.nursingsystem.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识资源实体类
 */
@Data
@TableName("knowledge_resource")
public class KnowledgeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源 ID
     */
    @TableId(value = "resource_id", type = IdType.AUTO)
    private Long resourceId;

    /**
     * 标题
     */
    private String title;

    /**
     * 资源简介
     */
    private String description;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件存储路径/OSS Key
     */
    private String filePath;

    /**
     * 文件大小 (字节)
     */
    private Long fileSize;

    /**
     * 文件类型 (pdf, docx, mp4 等)
     */
    private String fileType;

    /**
     * 文件扩展名
     */
    private String fileExtension;

    /**
     * 封面图路径 (视频/文档缩略图)
     */
    private String coverImage;

    /**
     * 全文检索内容 (提取后的纯文本)
     */
    private String contentText;

    /**
     * 文件 MD5 值
     */
    private String md5Code;

    /**
     * 累计浏览次数
     */
    private Integer viewCount;

    /**
     * 累计下载次数
     */
    private Integer downloadCount;

    /**
     * 状态 (0:待审核 1:已发布 2:已驳回 3:已下架)
     */
    private String status;

    /**
     * 是否置顶 (0:否 1:是)
     */
    private String isTop;

    /**
     * 审核意见/驳回原因
     */
    private String auditRemark;

    /**
     * 上传者 ID
     */
    private Long uploadUserId;

    /**
     * 上传者姓名
     */
    private String uploadUserName;

    /**
     * 所属部门 ID
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
