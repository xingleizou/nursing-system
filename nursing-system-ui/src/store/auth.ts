import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null')
  }),
  getters: {
    // 是否为管理员
    isAdmin(): boolean {
      return this.userInfo?.roleCode === 'admin'
    },
    // 角色代码
    roleCode(): string {
      return this.userInfo?.roleCode || ''
    }
  },
  actions: {
    async login(loginForm: any) {
      const data: any = await loginApi(loginForm)
      // 后端返回 result { token, tokenPrefix, userId, username, nickname, roleCode }
      this.token = data.token
      this.userInfo = {
        userId: data.userId,
        username: data.username,
        nickname: data.nickname,
        roleCode: data.roleCode
      }
      localStorage.setItem('token', data.token)
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
      return data
    },
    async logout() {
      await logoutApi()
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  }
})
