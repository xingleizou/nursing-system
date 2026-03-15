package com.example.nursingsystem.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源标签关联实体类
 */
@Data
@TableName("knowledge_resource_tag")
public class KnowledgeResourceTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源 ID
     */
    private Long resourceId;

    /**
     * 标签 ID
     */
    private Long tagId;
}
