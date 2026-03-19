import request from '@/utils/request'

/**
 * 分页查询知识资源
 * @param params 查询参数
 */
export function getKnowledgePage(params: any) {
  return request({
    url: '/api/knowledge/resource/page',
    method: 'get',
    params
  })
}

/**
 * 获取知识资源详情
 * @param resourceId 资源 ID
 */
export function getKnowledgeDetail(resourceId: number | string) {
  return request({
    url: `/api/knowledge/resource/${resourceId}`,
    method: 'get'
  })
}

/**
 * 上传知识资源
 * @param formData 包含文件和其他参数的 FormData 对象
 */
export function uploadKnowledgeResource(formData: FormData) {
  return request({
    url: '/api/knowledge/resource/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 审核知识资源
 * @param resourceId 资源 ID
 * @param data 审核操作参数
 */
export function auditKnowledgeResource(resourceId: number | string, data: any) {
  return request({
    url: `/api/knowledge/resource/audit/${resourceId}`,
    method: 'post',
    params: data
  })
}

/**
 * 删除知识资源
 * @param resourceId 资源 ID
 */
export function deleteKnowledgeResource(resourceId: number | string) {
  return request({
    url: `/api/knowledge/resource/${resourceId}`,
    method: 'delete'
  })
}

/**
 * 下载知识资源
 * @param resourceId 资源 ID
 */
export function downloadKnowledgeResource(resourceId: number | string) {
  return request({
    url: `/api/knowledge/resource/download/${resourceId}`,
    method: 'post'
  })
}

/**
 * 预览知识资源
 * @param resourceId 资源 ID
 */
export function previewKnowledgeResource(resourceId: number | string) {
  return request({
    url: `/api/knowledge/resource/preview/${resourceId}`,
    method: 'get'
  })
}

// ==================== 统计分析 API ====================

/**
 * 获取热门资源排行（按浏览量）
 * @param limit 返回数量限制，默认 10
 */
export function getTopViewedResources(limit: number = 2) {
  return request({
    url: '/api/knowledge/resource/top-viewed',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取热门资源排行（按下载量）
 * @param limit 返回数量限制，默认 10
 */
export function getTopDownloadedResources(limit: number = 2) {
  return request({
    url: '/api/knowledge/resource/top-downloaded',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取零访问预警资源列表
 * @param days 天数阈值（超过该天数未访问的资源），默认 30 天
 * @param limit 返回数量限制，默认 20
 */
export function getZeroAccessWarnings(days: number = 30, limit: number = 20) {
  return request({
    url: '/api/knowledge/resource/zero-access-warning',
    method: 'get',
    params: { days, limit }
  })
}

// ==================== 分类管理 API ====================

/**
 * 分页查询分类
 * @param params 查询参数
 */
export function getCategoryPage(params: any) {
  return request({
    url: '/api/knowledge/category/page',
    method: 'get',
    params
  })
}

/**
 * 获取分类树
 */
export function getCategoryTree() {
  return request({
    url: '/api/knowledge/category/tree',
    method: 'get'
  })
}

/**
 * 获取启用状态的分类列表
 */
export function getEnabledCategories() {
  return request({
    url: '/api/knowledge/category/enabled',
    method: 'get'
  })
}

/**
 * 新增分类
 * @param data 分类数据
 */
export function addCategory(data: any) {
  return request({
    url: '/api/knowledge/category',
    method: 'post',
    data
  })
}

/**
 * 修改分类
 * @param data 分类数据
 */
export function updateCategory(data: any) {
  return request({
    url: '/api/knowledge/category',
    method: 'put',
    data
  })
}

/**
 * 删除分类
 * @param categoryId 分类 ID
 */
export function deleteCategory(categoryId: number | string) {
  return request({
    url: `/api/knowledge/category/${categoryId}`,
    method: 'delete'
  })
}

// ==================== 标签管理 API ====================

/**
 * 分页查询标签
 * @param params 查询参数
 */
export function getTagPage(params: any) {
  return request({
    url: '/api/knowledge/tag/page',
    method: 'get',
    params
  })
}

/**
 * 获取所有标签列表
 */
export function getAllTags() {
  return request({
    url: '/api/knowledge/tag/list',
    method: 'get'
  })
}

/**
 * 获取启用状态的标签列表
 */
export function getEnabledTags() {
  return request({
    url: '/api/knowledge/tag/enabled',
    method: 'get'
  })
}

/**
 * 新增标签
 * @param data 标签数据
 */
export function addTag(data: any) {
  return request({
    url: '/api/knowledge/tag',
    method: 'post',
    data
  })
}

/**
 * 修改标签
 * @param data 标签数据
 */
export function updateTag(data: any) {
  return request({
    url: '/api/knowledge/tag',
    method: 'put',
    data
  })
}

/**
 * 删除标签
 * @param tagId 标签 ID
 */
export function deleteTag(tagId: number | string) {
  return request({
    url: `/api/knowledge/tag/${tagId}`,
    method: 'delete'
  })
}
