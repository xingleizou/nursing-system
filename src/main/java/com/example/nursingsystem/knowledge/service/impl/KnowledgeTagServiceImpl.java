package com.example.nursingsystem.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.knowledge.dto.TagQueryDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeTag;
import com.example.nursingsystem.knowledge.entity.KnowledgeResourceTag;
import com.example.nursingsystem.knowledge.mapper.KnowledgeTagMapper;
import com.example.nursingsystem.knowledge.mapper.KnowledgeResourceTagMapper;
import com.example.nursingsystem.knowledge.service.KnowledgeTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识标签服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeTagServiceImpl extends ServiceImpl<KnowledgeTagMapper, KnowledgeTag> implements KnowledgeTagService {

    private final KnowledgeTagMapper knowledgeTagMapper;
    private final KnowledgeResourceTagMapper knowledgeResourceTagMapper;

    @Override
    public Page<KnowledgeTag> pageTags(TagQueryDTO queryDTO) {
        Page<KnowledgeTag> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<KnowledgeTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeTag::getDeleted, 0);

        // 查询条件
        if (queryDTO.getTagName() != null && !queryDTO.getTagName().isEmpty()) {
            wrapper.like(KnowledgeTag::getTagName, queryDTO.getTagName());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(KnowledgeTag::getStatus, queryDTO.getStatus());
        }

        // 排序
        wrapper.orderByDesc(KnowledgeTag::getCreateTime);

        return knowledgeTagMapper.selectPage(page, wrapper);
    }

    @Override
    public List<KnowledgeTag> getEnabledTags() {
        LambdaQueryWrapper<KnowledgeTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeTag::getDeleted, 0);
        wrapper.eq(KnowledgeTag::getStatus, 1); // 启用状态
        wrapper.orderByDesc(KnowledgeTag::getCreateTime);

        return knowledgeTagMapper.selectList(wrapper);
    }

    @Override
    public List<KnowledgeTag> getTagsByResourceId(Long resourceId) {
        // 查询资源标签关联
        LambdaQueryWrapper<KnowledgeResourceTag> resourceTagWrapper = new LambdaQueryWrapper<>();
        resourceTagWrapper.eq(KnowledgeResourceTag::getResourceId, resourceId);
        List<KnowledgeResourceTag> resourceTags = knowledgeResourceTagMapper.selectList(resourceTagWrapper);

        // 提取标签ID列表
        List<Long> tagIds = resourceTags.stream()
                .map(KnowledgeResourceTag::getTagId)
                .collect(Collectors.toList());

        if (tagIds.isEmpty()) {
            return List.of();
        }

        // 查询标签信息
        LambdaQueryWrapper<KnowledgeTag> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.in(KnowledgeTag::getTagId, tagIds);
        tagWrapper.eq(KnowledgeTag::getDeleted, 0);


        return knowledgeTagMapper.selectList(tagWrapper);
    }

    @Override
    public boolean canDeleteTag(Long tagId) {
        // 检查是否有资源关联到这个标签
        LambdaQueryWrapper<KnowledgeResourceTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeResourceTag::getTagId, tagId);

        Long resourceCount = knowledgeResourceTagMapper.selectCount(wrapper);
        return resourceCount == 0; // 没有资源关联，可以删除
    }
}