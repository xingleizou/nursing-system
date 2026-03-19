package com.example.nursingsystem.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nursingsystem.knowledge.dto.CategoryQueryDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeCategory;

import java.util.List;

/**
 * 知识分类服务接口
 */
public interface KnowledgeCategoryService extends IService<KnowledgeCategory> {

    /**
     * 分页查询分类
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<KnowledgeCategory> pageCategories(CategoryQueryDTO queryDTO);

    /**
     * 获取分类树形结构
     * @return 树形分类列表
     */
    List<KnowledgeCategory> getCategoryTree();

    /**
     * 获取启用状态的分类列表
     * @return 分类列表
     */
    List<KnowledgeCategory> getEnabledCategories();

    /**
     * 检查分类是否可以被删除
     * @param categoryId 分类ID
     * @return 是否可以删除
     */
    boolean canDeleteCategory(Long categoryId);
}