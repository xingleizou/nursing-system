package com.example.nursingsystem.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.knowledge.entity.KnowledgeResourceTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源标签关联 Mapper 接口
 */
@Mapper
public interface KnowledgeResourceTagMapper extends BaseMapper<KnowledgeResourceTag> {
}
