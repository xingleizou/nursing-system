import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '用户登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    children: [
      {
        path: 'system/user',
        name: 'UserManagement',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '基础用户管理' }
      },
      {
        path: 'knowledge/resource',
        name: 'KnowledgeResource',
        component: () => import('@/views/knowledge/resource/index.vue'),
        meta: { title: '知识资源管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：检查 Token
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
