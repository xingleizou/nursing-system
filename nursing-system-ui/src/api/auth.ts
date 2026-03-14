import request from '@/utils/request'

/**
 * 登录请求
 * @param data 登录表单数据
 * @returns 登录结果，包含 token
 */
export function login(data: any) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

/**
 * 退出登录
 */
export function logout() {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  })
}
