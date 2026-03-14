import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null
  }),
  actions: {
    async login(loginForm: any) {
      const data: any = await loginApi(loginForm)
      // 后端返回 result { token: "...", tokenPrefix: "Bearer " }
      this.token = data.token
      localStorage.setItem('token', data.token)
      return data
    },
    async logout() {
      await logoutApi()
      this.token = ''
      localStorage.removeItem('token')
    }
  }
})
