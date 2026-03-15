package com.example.nursingsystem.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.knowledge.entity.KnowledgeAuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审核日志 Mapper 接口
 */
@Mapper
public interface KnowledgeAuditLogMapper extends BaseMapper<KnowledgeAuditLog> {
}
