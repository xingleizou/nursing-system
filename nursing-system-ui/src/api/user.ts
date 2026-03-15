import request from '@/utils/request'

/**
 * 分页查询用户
 * @param params 分页参数及搜索条件
 */
export function getUserPage(params: any) {
  return request({
    url: '/api/system/user/page',
    method: 'get',
    params
  })
}

/**
 * 创建用户
 * @param data 用户数据
 */
export function createUser(data: any) {
  return request({
    url: '/api/system/user',
    method: 'post',
    data
  })
}

/**
 * 更新用户
 * @param data 用户数据
 */
export function updateUser(data: any) {
  return request({
    url: '/api/system/user',
    method: 'put',
    data
  })
}

/**
 * 删除用户
 * @param userId 用户 ID
 */
export function deleteUser(userId: number | string) {
  return request({
    url: `/api/system/user/${userId}`,
    method: 'delete'
  })
}

/**
 * 物理删除用户 (彻底删除，不可恢复)
 * @param userId 用户 ID
 */
export function physicallyDeleteUser(userId: number | string) {
  return request({
    url: `/api/system/user/${userId}/physical`,
    method: 'delete'
  })
}
