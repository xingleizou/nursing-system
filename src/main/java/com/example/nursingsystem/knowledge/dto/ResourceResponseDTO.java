package com.example.nursingsystem.knowledge.dto;

import lombok.Data;

/**
 * 资源响应 DTO
 */
@Data
public class ResourceResponseDTO {

    /**
     * 资源 ID
     */
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
     * 分类名称
     */
    private String categoryName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小 (字节)
     */
    private Long fileSize;

    /**
     * 文件大小格式化显示
     */
    private String fileSizeFormatted;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件扩展名
     */
    private String fileExtension;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * MD5 值
     */
    private String md5Code;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 是否置顶
     */
    private String isTop;

    /**
     * 审核意见
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
     * 部门 ID
     */
    private Long deptId;

    /**
     * 标签列表
     */
    private java.util.List<TagDTO> tags;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    @Data
    public static class TagDTO {
        private Long tagId;
        private String tagName;
        private String color;
    }
}
