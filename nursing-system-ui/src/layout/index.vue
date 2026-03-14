<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="aside">
      <el-menu :default-active="$route.path" router class="el-menu-vertical">
        <el-menu-item index="/system/user">
          <el-icon><User /></el-icon>
          <span>基础用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <!-- 主体区域 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <h3>护理系统管理后台</h3>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              个人中心
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { User, ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const handleCommand = async (command: string) => {
  if (command === 'logout') {
    try {
      await authStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    } catch (error) {
      console.error('退出登录失败', error)
    }
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
  
  .aside {
    background-color: #304156;
    .el-menu-vertical {
      border-right: none;
      background-color: #304156;
      --el-menu-text-color: #fff;
      --el-menu-active-color: #409eff;
      --el-menu-hover-bg-color: #263445;
    }
  }
  
  .header {
    background-color: #fff;
    border-bottom: 1px solid #dcdfe6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    
    .header-left h3 {
      margin: 0;
      color: #333;
    }
    
    .el-dropdown-link {
      cursor: pointer;
      display: flex;
      align-items: center;
      color: #409eff;
    }
  }
  
  .main {
    background-color: #f0f2f5;
  }
}
</style>
