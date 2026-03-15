import request from '@/utils/request'

/**
 * 获取所有角色列表（用于下拉选择）
 */
export function getRoleList() {
  return request({
    url: '/api/system/role/list',
    method: 'get'
  })
}

/**
 * 分页查询角色
 * @param params 分页参数及搜索条件
 */
export function getRolePage(params: any) {
  return request({
    url: '/api/system/role/page',
    method: 'get',
    params
  })
}

/**
 * 创建角色
 * @param data 角色数据
 */
export function createRole(data: any) {
  return request({
    url: '/api/system/role',
    method: 'post',
    data
  })
}

/**
 * 更新角色
 * @param data 角色数据
 */
export function updateRole(data: any) {
  return request({
    url: '/api/system/role',
    method: 'put',
    data
  })
}

/**
 * 删除角色
 * @param roleIds 角色 ID 数组
 */
export function deleteRoles(roleIds: number[] | string[]) {
  return request({
    url: `/api/system/role/${roleIds}`,
    method: 'delete'
  })
}
