# 知识资源上传功能实现说明

## 已实现的功能

### 1. 数据库表设计 ✅
- `knowledge_resource` - 知识资源主表
- `knowledge_category` - 知识分类表
- `knowledge_tag` - 标签表
- `knowledge_resource_tag` - 资源标签关联表
- `knowledge_audit_log` - 审核日志表
- `knowledge_download_log` - 下载日志表

### 2. 实体类 (Entity) ✅
- [KnowledgeResource](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\entity\KnowledgeResource.java#L12-L135) - 知识资源实体
- [KnowledgeCategory](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\entity\KnowledgeCategory.java#L12-L80) - 知识分类实体
- [KnowledgeTag](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\entity\KnowledgeTag.java#L12-L54) - 标签实体
- [KnowledgeResourceTag](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\entity\KnowledgeResourceTag.java#L12-L26) - 资源标签关联实体
- [KnowledgeAuditLog](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\entity\KnowledgeAuditLog.java#L12-L57) - 审核日志实体
- [KnowledgeDownloadLog](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\entity\KnowledgeDownloadLog.java#L12-L57) - 下载日志实体

### 3. Mapper 层 ✅
- [KnowledgeResourceMapper](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\mapper\KnowledgeResourceMapper.java#L10-L33) - 包含 MD5 去重、计数更新方法
- [KnowledgeCategoryMapper](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\mapper\KnowledgeCategoryMapper.java#L9-L11)
- [KnowledgeTagMapper](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\mapper\KnowledgeTagMapper.java#L9-L11)
- [KnowledgeResourceTagMapper](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\mapper\KnowledgeResourceTagMapper.java#L9-L11)
- [KnowledgeAuditLogMapper](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\mapper\KnowledgeAuditLogMapper.java#L9-L11)
- [KnowledgeDownloadLogMapper](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\mapper\KnowledgeDownloadLogMapper.java#L9-L11)

### 4. DTO 层 ✅
- [ResourceUploadDTO](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\dto\ResourceUploadDTO.java#L10-L42) - 上传数据传输对象
- [ResourceQueryDTO](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\dto\ResourceQueryDTO.java#L8-L54) - 查询数据传输对象
- [ResourceResponseDTO](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\dto\ResourceResponseDTO.java#L8-L141) - 响应数据传输对象

### 5. Service 层 ✅
**接口**: [KnowledgeResourceService](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\service\KnowledgeResourceService.java#L12-L64)

**实现类**: [KnowledgeResourceServiceImpl](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\service\impl\KnowledgeResourceServiceImpl.java#L31-L425)

**核心功能**:
- ✅ 文件校验（大小、类型、MIME）
- ✅ MD5 计算与去重
- ✅ 文件存储（本地/OSS）
- ✅ 资源信息保存
- ✅ 标签关联处理
- ✅ 分页查询
- ✅ 资源详情查询
- ✅ 审核功能
- ✅ 删除功能
- ✅ 浏览次数/下载次数统计

### 6. Controller 层 ✅
[KnowledgeResourceController](file://D:\idea%20code\nursing-system\src\main\java\com\example\nursingsystem\knowledge\controller\KnowledgeResourceController.java#L17-L102) - 提供 RESTful API

## API 接口列表

### 1. 上传资源
```http
POST /api/knowledge/resource/upload
Content-Type: multipart/form-data

参数:
- title: String (必填) - 资源标题
- description: String (可选) - 资源简介
- categoryId: Long (必填) - 分类 ID
- tagIds: List<Long> (可选) - 标签 ID 列表
- isTop: String (可选，默认"0") - 是否置顶
- file: MultipartFile (必填) - 上传的文件

返回:
{
  "code": 200,
  "message": "操作成功",
  "data": 123  // 资源 ID
}
```

### 2. 分页查询资源
```http
GET /api/knowledge/resource/page?pageNum=1&pageSize=10&status=1

参数:
- pageNum: Integer - 当前页
- pageSize: Integer - 每页大小
- title: String - 标题关键词
- categoryId: Long - 分类 ID
- status: String - 状态
- sortField: String - 排序字段
- sortOrder: String - 排序方式

返回:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [...],
    "total": 100,
    "size": 10,
    "current": 1
  }
}
```

### 3. 获取资源详情
```http
GET /api/knowledge/resource/{resourceId}

返回: 资源详情对象
```

### 4. 审核资源
```http
POST /api/knowledge/resource/audit/{resourceId}

参数:
- auditAction: String (必填) - 1:通过 2:驳回
- auditRemark: String (可选) - 审核意见

返回: 操作结果
```

### 5. 删除资源
```http
DELETE /api/knowledge/resource/{resourceId}

返回: 操作结果
```

### 6. 下载资源
```http
POST /api/knowledge/resource/download/{resourceId}

返回: 文件路径或下载 URL
```

## 核心功能实现

### 1. 文件校验
```java
// 支持的文件类型
- PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX
- MP4, AVI, MOV, WMV
- JPG, JPEG, PNG, GIF

// 文件大小限制
最大 100MB
```

### 2. MD5 去重
上传时自动计算文件 MD5 值，如果已存在相同 MD5 的文件，则拒绝上传。

### 3. 文件存储
当前实现为本地存储，路径：`uploads/knowledge/yyyyMMdd/uuid.ext`
TODO: 后续可集成阿里云 OSS

### 4. 审核流程
- 上传后状态为 "0" (待审核)
- 管理员审核后变为 "1" (已发布) 或 "2" (已驳回)
- 记录审核日志到 `knowledge_audit_log`

### 5. 统计功能
- `view_count` - 浏览次数（查看资源详情时自动 +1）
- `download_count` - 下载次数（下载时自动 +1）

## 待完善功能

### 1. 认证集成
当前用户信息是硬编码的，需要从 JWT Token 中获取真实用户信息：
```java
// TODO: 从认证信息中获取用户 ID 和用户名
Long userId = getCurrentUserId();
String userName = getCurrentUserName();
```

### 2. 权限控制
需要添加权限注解，控制不同角色的访问：
- 普通用户：只能上传、查看已发布资源
- 管理员：可以审核、删除所有资源

### 3. OSS 集成
将文件存储从本地迁移到阿里云 OSS：
```java
// 使用 AliOSSUtil 工具类
String filePath = aliOSSUtil.upload(file.getBytes());
```

### 4. 下载功能完善
实现真实的文件下载（流式输出、断点续传）：
```java
@GetMapping("/download/{resourceId}")
public void download(@PathVariable Long resourceId, HttpServletResponse response) {
    // 流式输出文件
}
```

### 5. 全文检索
利用 `content_text` 字段实现 MySQL 全文检索：
```sql
SELECT * FROM knowledge_resource 
WHERE MATCH(content_text) AGAINST('护理规范' IN NATURAL LANGUAGE MODE);
```

### 6. 浏览日志优化
使用 Redis 缓存浏览记录，定时聚合更新到主表：
```java
// 写入 Redis
redisTemplate.opsForZSet().incrementScore("view_count:" + resourceId, userId, 1);

// 定时任务聚合
@Scheduled(cron = "0 0 * * * ?")
public void aggregateViewCount() {
    // 每小时聚合一次
}
```

## 测试建议

### 1. 单元测试
测试 Service 层的各个方法

### 2. 接口测试
使用 Postman 或 Apifox 测试上传接口：
- 测试正常上传
- 测试文件类型限制
- 测试文件大小限制
- 测试 MD5 去重

### 3. 性能测试
- 批量上传测试
- 大文件上传测试
- 并发上传测试

## 注意事项

1. **数据库执行**: 记得先执行 SQL 脚本创建表
2. **目录创建**: 确保 `uploads/knowledge/` 目录存在或有写权限
3. **事务管理**: Service 方法已添加 `@Transactional` 注解
4. **异常处理**: 全局异常处理器会自动处理 BusinessException
5. **日志记录**: 关键操作都有日志记录
