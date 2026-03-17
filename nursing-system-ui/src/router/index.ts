import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { ElMessage } from 'element-plus'

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
        path: 'knowledge',
        name: 'Knowledge',
        meta: { title: '知识管理' },
        children: [
          {
            path: 'resource',
            name: 'KnowledgeResource',
            component: () => import('@/views/knowledge/resource/index.vue'),
            meta: { title: '资源管理' }
          },
          {
            path: 'category',
            name: 'KnowledgeCategory',
            component: () => import('@/views/knowledge/category/index.vue'),
            meta: { title: '分类管理' }
          },
          {
            path: 'tag',
            name: 'KnowledgeTag',
            component: () => import('@/views/knowledge/tag/index.vue'),
            meta: { title: '标签管理' }
          },
          {
            path: 'analysis',
            name: 'KnowledgeAnalysis',
            component: () => import('@/views/knowledge/analysis/index.vue'),
            meta: { title: '统计分析' }
          }
        ]
      }
    ]
  },
  {
    path: '/nurse',
    name: 'NursePortal',
    component: () => import('@/views/nurse/portal/index.vue'),
    meta: { title: '护理知识库' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：检查 Token 和角色权限
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null')
  const roleCode = userInfo?.roleCode
  
  if (to.path === '/login') {
    if (token) {
      // 已登录用户访问登录页，根据角色跳转
      if (roleCode === 'admin') {
        next('/')
      } else {
        next('/nurse')
      }
    } else {
      next()
    }
  } else if (to.path === '/nurse') {
    // 前台系统页面，所有登录用户都可访问
    if (token) {
      next()
    } else {
      next('/login')
    }
  } else if (to.path.startsWith('/knowledge')) {
    // 后台知识管理模块，只允许管理员访问
    if (!token) {
      next('/login')
    } else if (roleCode === 'admin') {
      next()
    } else {
      ElMessage.error('无权访问，请联系管理员')
      next('/nurse')
    }
  } else {
    // 其他后台页面，只允许管理员访问
    if (!token) {
      next('/login')
    } else if (roleCode === 'admin') {
      next()
    } else {
      ElMessage.error('无权访问，请联系管理员')
      next('/nurse')
    }
  }
})

export default router
