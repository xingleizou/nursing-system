package com.example.nursingsystem.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.knowledge.dto.CategoryQueryDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeCategory;
import com.example.nursingsystem.knowledge.mapper.KnowledgeCategoryMapper;
import com.example.nursingsystem.knowledge.service.KnowledgeCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeCategoryServiceImpl extends ServiceImpl<KnowledgeCategoryMapper, KnowledgeCategory> implements KnowledgeCategoryService {

    private final KnowledgeCategoryMapper knowledgeCategoryMapper;

    @Override
    public Page<KnowledgeCategory> pageCategories(CategoryQueryDTO queryDTO) {
        Page<KnowledgeCategory> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<KnowledgeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeCategory::getDeleted, 0);

        // 查询条件
        if (queryDTO.getCategoryName() != null && !queryDTO.getCategoryName().isEmpty()) {
            wrapper.like(KnowledgeCategory::getCategoryName, queryDTO.getCategoryName());
        }
        if (queryDTO.getParentId() != null) {
            wrapper.eq(KnowledgeCategory::getParentId, queryDTO.getParentId());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(KnowledgeCategory::getStatus, queryDTO.getStatus());
        }

        // 排序
        wrapper.orderByAsc(KnowledgeCategory::getOrderNum);
        wrapper.orderByDesc(KnowledgeCategory::getCreateTime);

        return knowledgeCategoryMapper.selectPage(page, wrapper);
    }

    @Override
    public List<KnowledgeCategory> getCategoryTree() {
        // 查询所有未删除的分类
        LambdaQueryWrapper<KnowledgeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeCategory::getDeleted, 0);
        wrapper.orderByAsc(KnowledgeCategory::getOrderNum);
        wrapper.orderByDesc(KnowledgeCategory::getCreateTime);

        List<KnowledgeCategory> allCategories = knowledgeCategoryMapper.selectList(wrapper);

        // 构建树形结构
        return buildCategoryTree(allCategories);
    }

    @Override
    public List<KnowledgeCategory> getEnabledCategories() {
        LambdaQueryWrapper<KnowledgeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeCategory::getDeleted, 0);
        wrapper.eq(KnowledgeCategory::getStatus, 1); // 启用状态
        wrapper.orderByAsc(KnowledgeCategory::getOrderNum);
        wrapper.orderByDesc(KnowledgeCategory::getCreateTime);

        return knowledgeCategoryMapper.selectList(wrapper);
    }

    @Override
    public boolean canDeleteCategory(Long categoryId) {
        // 检查是否有子分类
        LambdaQueryWrapper<KnowledgeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeCategory::getParentId, categoryId);
        wrapper.eq(KnowledgeCategory::getDeleted, 0);

        Long childCount = knowledgeCategoryMapper.selectCount(wrapper);
        if (childCount > 0) {
            return false; // 有子分类，不能删除
        }

        // 检查是否有资源关联到这个分类
        // 这里需要检查 knowledge_resource 表，但为了简化，我们先返回 true
        // 在实际项目中，需要查询资源表检查是否有资源使用这个分类
        return true;
    }

    /**
     * 构建分类树形结构
     */
    private List<KnowledgeCategory> buildCategoryTree(List<KnowledgeCategory> allCategories) {
        List<KnowledgeCategory> rootCategories = new ArrayList<>();

        // 找到所有根节点（parentId为0或null）
        for (KnowledgeCategory category : allCategories) {
            if (category.getParentId() == null || category.getParentId() == 0) {
                rootCategories.add(category);
            }
        }

        // 为每个根节点构建子树
        for (KnowledgeCategory rootCategory : rootCategories) {
            buildSubTree(rootCategory, allCategories);
        }

        return rootCategories;
    }

    /**
     * 构建子树
     */
    private void buildSubTree(KnowledgeCategory parentCategory, List<KnowledgeCategory> allCategories) {
        List<KnowledgeCategory> children = new ArrayList<>();

        for (KnowledgeCategory category : allCategories) {
            if (category.getParentId() != null && category.getParentId().equals(parentCategory.getCategoryId())) {
                children.add(category);
                // 递归构建子节点的子树
                buildSubTree(category, allCategories);
            }
        }

        parentCategory.setChildren(children);
    }
}