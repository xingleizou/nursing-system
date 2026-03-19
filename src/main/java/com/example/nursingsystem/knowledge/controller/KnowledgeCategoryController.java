package com.example.nursingsystem.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.knowledge.dto.CategoryQueryDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeCategory;
import com.example.nursingsystem.knowledge.service.KnowledgeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识分类控制器
 */
@RestController
@RequestMapping("/api/knowledge/category")
@RequiredArgsConstructor
public class KnowledgeCategoryController {

    private final KnowledgeCategoryService knowledgeCategoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public Result<Void> addCategory(@RequestBody KnowledgeCategory category) {
        knowledgeCategoryService.save(category);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @PutMapping
    public Result<Void> updateCategory(@RequestBody KnowledgeCategory category) {
        knowledgeCategoryService.updateById(category);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{categoryId}")
    public Result<Void> deleteCategory(@PathVariable Long categoryId) {
        knowledgeCategoryService.removeById(categoryId);
        return Result.success();
    }

    /**
     * 根据ID获取分类详情
     */
    @GetMapping("/{categoryId}")
    public Result<KnowledgeCategory> getCategoryById(@PathVariable Long categoryId) {
        KnowledgeCategory category = knowledgeCategoryService.getById(categoryId);
        return Result.success(category);
    }

    /**
     * 分页查询分类列表
     */
    @GetMapping("/page")
    public Result<Page<KnowledgeCategory>> pageCategories(CategoryQueryDTO queryDTO) {
        Page<KnowledgeCategory> page = knowledgeCategoryService.pageCategories(queryDTO);
        return Result.success(page);
    }

    /**
     * 查询所有分类列表（树形结构）
     */
    @GetMapping("/tree")
    public Result<List<KnowledgeCategory>> getCategoryTree() {
        List<KnowledgeCategory> categoryTree = knowledgeCategoryService.getCategoryTree();
        return Result.success(categoryTree);
    }

    /**
     * 查询启用状态的分类列表
     */
    @GetMapping("/enabled")
    public Result<List<KnowledgeCategory>> getEnabledCategories() {
        List<KnowledgeCategory> categories = knowledgeCategoryService.getEnabledCategories();
        return Result.success(categories);
    }
}