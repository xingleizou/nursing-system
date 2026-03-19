package com.example.nursingsystem.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.knowledge.dto.TagQueryDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeTag;
import com.example.nursingsystem.knowledge.service.KnowledgeTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识标签控制器
 */
@RestController
@RequestMapping("/api/knowledge/tag")
@RequiredArgsConstructor
public class KnowledgeTagController {

    private final KnowledgeTagService knowledgeTagService;

    /**
     * 新增标签
     */
    @PostMapping
    public Result<Void> addTag(@RequestBody KnowledgeTag tag) {
        knowledgeTagService.save(tag);
        return Result.success();
    }

    /**
     * 修改标签
     */
    @PutMapping
    public Result<Void> updateTag(@RequestBody KnowledgeTag tag) {
        knowledgeTagService.updateById(tag);
        return Result.success();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{tagId}")
    public Result<Void> deleteTag(@PathVariable Long tagId) {
        knowledgeTagService.removeById(tagId);
        return Result.success();
    }

    /**
     * 根据ID获取标签详情
     */
    @GetMapping("/{tagId}")
    public Result<KnowledgeTag> getTagById(@PathVariable Long tagId) {
        KnowledgeTag tag = knowledgeTagService.getById(tagId);
        return Result.success(tag);
    }

    /**
     * 分页查询标签列表
     */
    @GetMapping("/page")
    public Result<Page<KnowledgeTag>> pageTags(TagQueryDTO queryDTO) {
        Page<KnowledgeTag> page = knowledgeTagService.pageTags(queryDTO);
        return Result.success(page);
    }

    /**
     * 查询所有标签列表
     */
    @GetMapping("/list")
    public Result<List<KnowledgeTag>> getAllTags() {
        List<KnowledgeTag> tags = knowledgeTagService.list();
        return Result.success(tags);
    }

    /**
     * 查询启用状态的标签列表
     */
    @GetMapping("/enabled")
    public Result<List<KnowledgeTag>> getEnabledTags() {
        List<KnowledgeTag> tags = knowledgeTagService.getEnabledTags();
        return Result.success(tags);
    }

    /**
     * 根据资源ID查询关联的标签
     */
    @GetMapping("/resource/{resourceId}")
    public Result<List<KnowledgeTag>> getTagsByResourceId(@PathVariable Long resourceId) {
        List<KnowledgeTag> tags = knowledgeTagService.getTagsByResourceId(resourceId);
        return Result.success(tags);
    }
}