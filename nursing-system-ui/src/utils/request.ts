import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API || '', // 默认使用相对路径，由 Vite 代理处理
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data
    
    // 如果 code 为 200，说明请求成功
    if (code === 200) {
      return data
    } else {
      ElMessage.error(message || '系统错误')
      return Promise.reject(new Error(message || '系统错误'))
    }
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // 未授权，清除本地 Token 并跳转到登录页
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    ElMessage.error(error.message || '系统错误')
    return Promise.reject(error)
  }
)

export default service
