package com.example.nursingsystem.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nursingsystem.knowledge.dto.TagQueryDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeTag;

import java.util.List;

/**
 * 知识标签服务接口
 */
public interface KnowledgeTagService extends IService<KnowledgeTag> {

    /**
     * 分页查询标签
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<KnowledgeTag> pageTags(TagQueryDTO queryDTO);

    /**
     * 获取启用状态的标签列表
     * @return 标签列表
     */
    List<KnowledgeTag> getEnabledTags();

    /**
     * 根据资源ID查询关联的标签
     * @param resourceId 资源ID
     * @return 标签列表
     */
    List<KnowledgeTag> getTagsByResourceId(Long resourceId);

    /**
     * 检查标签是否可以被删除
     * @param tagId 标签ID
     * @return 是否可以删除
     */
    boolean canDeleteTag(Long tagId);
}