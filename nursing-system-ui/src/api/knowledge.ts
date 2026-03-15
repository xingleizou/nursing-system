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
