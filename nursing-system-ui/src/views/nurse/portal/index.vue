<template>
  <div class="nurse-portal">
    <!-- 顶部导航栏 -->
    <el-header class="portal-header">
      <div class="header-content">
        <div class="logo" @click="goHome">
          <el-icon :size="28"><Reading /></el-icon>
          <span class="logo-text">护理知识库</span>
        </div>
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索课件、文档、疾病知识..."
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
            <template #append>
              <el-button @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </div>
        <div class="user-actions">
          <el-button text @click="toggleLayout">
            <el-icon><Grid /></el-icon>
          </el-button>
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32">{{ userName.charAt(0) }}</el-avatar>
              <span class="user-name">{{ userName }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <!-- 主体内容 -->
    <el-main class="portal-main">
      <!-- 分类导航 -->
      <section class="category-section">
        <h3 class="section-title">按科室分类</h3>
        <div class="category-grid">
          <div
            v-for="cat in departmentCategories"
            :key="cat.categoryId"
            class="category-card"
            @click="selectCategory(cat.categoryId)"
          >
            <el-icon :size="32" :style="{ color: cat.color }">
              <component :is="cat.icon" />
            </el-icon>
            <span class="category-name">{{ cat.categoryName }}</span>
            <span class="resource-count">{{ cat.count }} 个资源</span>
          </div>
        </div>
      </section>

      <!-- 热门推荐 -->
      <section class="recommend-section">
        <div class="section-header">
          <h3 class="section-title">🔥 热门推荐</h3>
          <el-radio-group v-model="hotTab" size="small" @change="loadHotResources">
            <el-radio-button label="today">今日热门</el-radio-button>
            <el-radio-button label="week">本周热门</el-radio-button>
            <el-radio-button label="month">本月热门</el-radio-button>
          </el-radio-group>
        </div>
        <div class="hot-resources">
          <div
            v-for="(resource, index) in hotResources"
            :key="resource.resourceId"
            class="hot-item"
            @click="viewDetail(resource.resourceId)"
          >
            <div class="hot-rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</div>
            <div class="hot-info">
              <div class="hot-title">{{ resource.title }}</div>
              <div class="hot-meta">
                <el-tag size="small" type="info">{{ resource.fileType }}</el-tag>
                <span><el-icon><View /></el-icon> {{ resource.viewCount }}</span>
                <span><el-icon><Download /></el-icon> {{ resource.downloadCount }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 资源列表 -->
      <section class="resource-section">
        <div class="section-header">
          <h3 class="section-title">📚 全部资源</h3>
          <div class="view-toggle">
            <el-button :type="viewMode === 'list' ? 'primary' : ''" text @click="viewMode = 'list'">
              <el-icon><List /></el-icon>
            </el-button>
            <el-button :type="viewMode === 'grid' ? 'primary' : ''" text @click="viewMode = 'grid'">
              <el-icon><Grid /></el-icon>
            </el-button>
          </div>
        </div>

        <!-- 筛选条件 -->
        <div class="filter-bar">
          <el-form :inline="true" size="default">
            <el-form-item label="文件类型">
              <el-select v-model="filters.fileType" placeholder="全部类型" clearable style="width: 150px">
                <el-option label="PPT 课件" value="ppt" />
                <el-option label="PPTX 课件" value="pptx" />
                <el-option label="Word 文档" value="doc" />
                <el-option label="Word 文档" value="docx" />
                <el-option label="PDF 文档" value="pdf" />
                <el-option label="视频" value="mp4" />
              </el-select>
            </el-form-item>
            <el-form-item label="上传时间">
              <el-select v-model="filters.timeRange" placeholder="全部时间" clearable style="width: 150px">
                <el-option label="最近一周" value="week" />
                <el-option label="最近一月" value="month" />
                <el-option label="最近三月" value="quarter" />
                <el-option label="最近一年" value="year" />
              </el-select>
            </el-form-item>
            <el-form-item label="排序">
              <el-select v-model="filters.sortBy" placeholder="默认排序" style="width: 150px">
                <el-option label="最新发布" value="create_time_desc" />
                <el-option label="最多浏览" value="view_count_desc" />
                <el-option label="最多下载" value="download_count_desc" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadResources">查询</el-button>
              <el-button @click="resetFilters">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 资源列表 - 列表视图 -->
        <el-table
          v-if="viewMode === 'list'"
          :data="resourceList"
          v-loading="loading"
          stripe
          style="width: 100%"
          @row-click="viewDetail"
        >
          <el-table-column prop="title" label="资源标题" min-width="300" show-overflow-tooltip>
            <template #default="scope">
              <div class="resource-title">
                <el-icon v-if="getFileIcon(scope.row.fileType)" :size="18">
                  <component :is="getFileIcon(scope.row.fileType)" />
                </el-icon>
                <span>{{ scope.row.title }}</span>
                <el-tag v-if="scope.row.isTop === '1'" size="small" type="danger">置顶</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="categoryName" label="分类" width="120" align="center" />
          <el-table-column prop="fileType" label="类型" width="100" align="center">
            <template #default="scope">
              <el-tag size="small" :type="getTypeTag(scope.row.fileType)">
                {{ scope.row.fileType?.toUpperCase() }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="fileSizeFormatted" label="大小" width="100" align="center" />
          <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
          <el-table-column prop="downloadCount" label="下载" width="80" align="center" />
          <el-table-column prop="createTime" label="上传时间" width="160" align="center" />
          <el-table-column label="操作" width="150" align="center" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" link @click.stop="viewDetail(scope.row.resourceId)">查看</el-button>
              <el-button size="small" type="success" link @click.stop="downloadResource(scope.row.resourceId)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 资源列表 - 卡片视图 -->
        <div v-else class="resource-grid" v-loading="loading">
          <div
            v-for="resource in resourceList"
            :key="resource.resourceId"
            class="resource-card"
            @click="viewDetail(resource.resourceId)"
          >
            <div class="card-icon">
              <el-icon :size="48" :color="getFileColor(resource.fileType)">
                <component :is="getFileIcon(resource.fileType)" />
              </el-icon>
              <el-tag v-if="resource.isTop === '1'" size="small" type="danger" class="top-tag">置顶</el-tag>
            </div>
            <div class="card-content">
              <div class="card-title">{{ resource.title }}</div>
              <div class="card-desc">{{ resource.description || '暂无描述' }}</div>
              <div class="card-meta">
                <el-tag size="small" type="info">{{ resource.categoryName }}</el-tag>
                <span>{{ resource.fileType?.toUpperCase() }}</span>
                <span>{{ resource.fileSizeFormatted }}</span>
              </div>
              <div class="card-stats">
                <span><el-icon><View /></el-icon> {{ resource.viewCount }}</span>
                <span><el-icon><Download /></el-icon> {{ resource.downloadCount }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :page-sizes="[12, 24, 48]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </section>
    </el-main>

    <!-- 资源详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="资源详情"
      width="800px"
      destroy-on-close
    >
      <div v-if="currentResource" class="resource-detail">
        <div class="detail-header">
          <div class="detail-icon">
            <el-icon :size="64" :color="getFileColor(currentResource.fileType)">
              <component :is="getFileIcon(currentResource.fileType)" />
            </el-icon>
          </div>
          <div class="detail-info">
            <h2 class="detail-title">{{ currentResource.title }}</h2>
            <div class="detail-meta">
              <el-tag>{{ currentResource.categoryName }}</el-tag>
              <span>{{ currentResource.fileType?.toUpperCase() }}</span>
              <span>{{ currentResource.fileSizeFormatted }}</span>
              <span>上传于 {{ currentResource.createTime }}</span>
            </div>
            <div class="detail-stats">
              <span><el-icon><View /></el-icon> {{ currentResource.viewCount }} 次浏览</span>
              <span><el-icon><Download /></el-icon> {{ currentResource.downloadCount }} 次下载</span>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <h4>资源简介</h4>
          <p>{{ currentResource.description || '暂无描述' }}</p>
        </div>
        <div class="detail-tags" v-if="currentResource.tags && currentResource.tags.length > 0">
          <h4>标签</h4>
          <el-tag
            v-for="tag in currentResource.tags"
            :key="tag.tagId"
            size="small"
            :color="tag.color"
            effect="plain"
            style="margin-right: 8px"
          >
            {{ tag.tagName }}
          </el-tag>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
          <el-button type="primary" @click="downloadResource(currentResource?.resourceId)">
            <el-icon><Download /></el-icon> 下载资源
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { getKnowledgePage, getKnowledgeDetail, downloadKnowledgeResource } from '@/api/knowledge'
import { ElMessage } from 'element-plus'
import {
  Reading, Search, Grid, List, View, Download,
  Document, Files, VideoCamera, Picture
} from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

// 用户信息
const userName = ref(authStore.user?.userName || '护士')

// 搜索相关
const searchKeyword = ref('')
const handleSearch = () => {
  queryParams.title = searchKeyword.value
  queryParams.pageNum = 1
  loadResources()
}

// 视图模式
const viewMode = ref<'list' | 'grid'>('list')
const toggleLayout = () => {
  viewMode.value = viewMode.value === 'list' ? 'grid' : 'list'
}

// 分类数据
const departmentCategories = ref([
  { categoryId: 1, categoryName: '内科', count: 120, icon: 'Document', color: '#409EFF' },
  { categoryId: 2, categoryName: '外科', count: 85, icon: 'Document', color: '#67C23A' },
  { categoryId: 3, categoryName: '妇产科', count: 65, icon: 'Document', color: '#E6A23C' },
  { categoryId: 4, categoryName: '儿科', count: 78, icon: 'Document', color: '#F56C6C' },
  { categoryId: 5, categoryName: '急诊科', count: 92, icon: 'Document', color: '#909399' },
  { categoryId: 6, categoryName: 'ICU', count: 56, icon: 'Document', color: '#722ED1' },
  { categoryId: 7, categoryName: '手术室', count: 43, icon: 'Document', color: '#13C2C2' },
  { categoryId: 8, categoryName: '其他', count: 150, icon: 'Files', color: '#FA8C16' },
])

// 热门资源
const hotTab = ref('week')
const hotResources = ref<any[]>([])
const loadHotResources = async () => {
  // TODO: 调用后端 API 获取热门资源
  // 这里使用模拟数据
  hotResources.value = [
    { resourceId: 1, title: '心肺复苏操作规范 (2024 版)', fileType: 'PPT', viewCount: 2345, downloadCount: 892 },
    { resourceId: 2, title: '静脉输液护理技术', fileType: 'PDF', viewCount: 1876, downloadCount: 654 },
    { resourceId: 3, title: '糖尿病患者的护理查房', fileType: 'PPTX', viewCount: 1654, downloadCount: 521 },
    { resourceId: 4, title: '创伤急救处理流程', fileType: 'MP4', viewCount: 1432, downloadCount: 478 },
    { resourceId: 5, title: '院内感染防控指南', fileType: 'DOCX', viewCount: 1298, downloadCount: 412 },
  ]
}

// 资源列表
const loading = ref(false)
const resourceList = ref<any[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 12,
  title: '',
  categoryId: undefined as number | undefined,
  status: '1', // 只看已发布
  sortBy: 'create_time_desc'
})

const filters = reactive({
  fileType: '',
  timeRange: '',
  sortBy: 'create_time_desc'
})

const loadResources = async () => {
  loading.value = true
  try {
    const params = {
      ...queryParams,
      fileType: filters.fileType || undefined,
      timeRange: filters.timeRange || undefined,
      sortField: filters.sortBy.split('_')[0],
      sortOrder: filters.sortBy.split('_')[1]
    }
    
    const data: any = await getKnowledgePage(params)
    resourceList.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取资源列表失败', error)
    ElMessage.error('加载资源失败')
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  filters.fileType = ''
  filters.timeRange = ''
  filters.sortBy = 'create_time_desc'
  queryParams.title = ''
  searchKeyword.value = ''
  queryParams.pageNum = 1
  loadResources()
}

const selectCategory = (categoryId: number) => {
  queryParams.categoryId = categoryId
  queryParams.pageNum = 1
  loadResources()
}

// 分页
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  loadResources()
}
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  loadResources()
}

// 详情查看
const detailVisible = ref(false)
const currentResource = ref<any>(null)
const viewDetail = async (resourceId: number) => {
  try {
    const data: any = await getKnowledgeDetail(resourceId)
    currentResource.value = data
    detailVisible.value = true
  } catch (error) {
    console.error('获取资源详情失败', error)
    ElMessage.error('加载资源详情失败')
  }
}

// 下载资源
const downloadResource = async (resourceId: number) => {
  try {
    const filePath = await downloadKnowledgeResource(resourceId)
    ElMessage.success('开始下载')
    // TODO: 实现真正的下载逻辑
    window.open(filePath, '_blank')
  } catch (error) {
    console.error('下载失败', error)
    ElMessage.error('下载失败')
  }
}

// 文件类型图标和颜色
const getFileIcon = (fileType: string) => {
  const map: Record<string, any> = {
    'ppt': Document,
    'pptx': Document,
    'pdf': Document,
    'doc': Files,
    'docx': Files,
    'mp4': VideoCamera,
    'jpg': Picture,
    'png': Picture
  }
  return map[fileType?.toLowerCase()] || Files
}

const getFileColor = (fileType: string) => {
  const map: Record<string, string> = {
    'ppt': '#E6A23C',
    'pptx': '#E6A23C',
    'pdf': '#F56C6C',
    'doc': '#409EFF',
    'docx': '#409EFF',
    'mp4': '#67C23A',
    'jpg': '#909399',
    'png': '#909399'
  }
  return map[fileType?.toLowerCase()] || '#909399'
}

const getTypeTag = (fileType: string) => {
  const map: Record<string, string> = {
    'ppt': 'warning',
    'pptx': 'warning',
    'pdf': 'danger',
    'doc': 'primary',
    'docx': 'primary',
    'mp4': 'success'
  }
  return map[fileType?.toLowerCase()] || 'info'
}

// 退出登录
const handleLogout = async () => {
  try {
    await authStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch (error) {
    console.error('退出登录失败', error)
  }
}

// 返回首页
const goHome = () => {
  resetFilters()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  loadResources()
  loadHotResources()
})
</script>

<style scoped lang="scss">
.nurse-portal {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.portal-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .header-content {
    max-width: 1400px;
    margin: 0 auto;
    padding: 15px 30px;
    display: flex;
    align-items: center;
    gap: 40px;
    
    .logo {
      display: flex;
      align-items: center;
      gap: 10px;
      color: #fff;
      font-size: 20px;
      font-weight: bold;
      cursor: pointer;
      
      .logo-text {
        font-size: 22px;
      }
    }
    
    .search-bar {
      flex: 1;
      max-width: 600px;
      
      :deep(.el-input__wrapper) {
        border-radius: 20px;
      }
    }
    
    .user-actions {
      display: flex;
      align-items: center;
      gap: 15px;
      
      .user-info {
        display: flex;
        align-items: center;
        gap: 8px;
        color: #fff;
        cursor: pointer;
        
        .user-name {
          font-size: 14px;
        }
      }
    }
  }
}

.portal-main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px 30px;
  
  .section-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin: 0 0 20px 0;
    padding-left: 12px;
    border-left: 4px solid #667eea;
  }
  
  // 分类区域
  .category-section {
    margin-bottom: 40px;
    
    .category-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
      gap: 16px;
      
      .category-card {
        background: #fff;
        border-radius: 12px;
        padding: 24px 16px;
        text-align: center;
        cursor: pointer;
        transition: all 0.3s;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        
        &:hover {
          transform: translateY(-4px);
          box-shadow: 0 8px 16px rgba(102, 126, 234, 0.2);
        }
        
        .category-name {
          display: block;
          margin-top: 12px;
          font-size: 15px;
          font-weight: 500;
          color: #333;
        }
        
        .resource-count {
          display: block;
          margin-top: 6px;
          font-size: 12px;
          color: #999;
        }
      }
    }
  }
  
  // 热门推荐区域
  .recommend-section {
    margin-bottom: 40px;
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      .section-title {
        margin: 0;
        border: none;
        padding: 0;
      }
    }
    
    .hot-resources {
      .hot-item {
        display: flex;
        align-items: center;
        padding: 12px 16px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;
        
        &:hover {
          background: #f5f7fa;
        }
        
        .hot-rank {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 16px;
          margin-right: 16px;
          
          &.rank-1 {
            background: linear-gradient(135deg, #FFD700, #FFA500);
            color: #fff;
          }
          &.rank-2 {
            background: linear-gradient(135deg, #C0C0C0, #808080);
            color: #fff;
          }
          &.rank-3 {
            background: linear-gradient(135deg, #CD7F32, #8B4513);
            color: #fff;
          }
          &.rank-n {
            background: #f0f2f5;
            color: #666;
          }
        }
        
        .hot-info {
          flex: 1;
          
          .hot-title {
            font-size: 14px;
            color: #333;
            margin-bottom: 6px;
          }
          
          .hot-meta {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 12px;
            color: #999;
            
            span {
              display: flex;
              align-items: center;
              gap: 4px;
            }
          }
        }
      }
    }
  }
  
  // 资源列表区域
  .resource-section {
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      .section-title {
        margin: 0;
        border: none;
        padding: 0;
      }
      
      .view-toggle {
        display: flex;
        gap: 8px;
      }
    }
    
    .filter-bar {
      margin-bottom: 24px;
      padding-bottom: 20px;
      border-bottom: 1px solid #ebeef5;
    }
    
    .resource-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 20px;
      
      .resource-card {
        background: #fff;
        border: 1px solid #ebeef5;
        border-radius: 12px;
        padding: 20px;
        cursor: pointer;
        transition: all 0.3s;
        
        &:hover {
          border-color: #667eea;
          box-shadow: 0 4px 16px rgba(102, 126, 234, 0.15);
          transform: translateY(-2px);
        }
        
        .card-icon {
          height: 80px;
          display: flex;
          align-items: center;
          justify-content: center;
          position: relative;
          
          .top-tag {
            position: absolute;
            top: 0;
            right: 0;
          }
        }
        
        .card-content {
          margin-top: 16px;
          
          .card-title {
            font-size: 15px;
            font-weight: 500;
            color: #333;
            margin-bottom: 8px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }
          
          .card-desc {
            font-size: 13px;
            color: #999;
            margin-bottom: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            min-height: 36px;
          }
          
          .card-meta {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 12px;
            color: #666;
            margin-bottom: 12px;
            flex-wrap: wrap;
          }
          
          .card-stats {
            display: flex;
            gap: 16px;
            font-size: 12px;
            color: #999;
            
            span {
              display: flex;
              align-items: center;
              gap: 4px;
            }
          }
        }
      }
    }
    
    .pagination {
      margin-top: 32px;
      display: flex;
      justify-content: center;
    }
  }
}

// 资源详情
.resource-detail {
  .detail-header {
    display: flex;
    gap: 24px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 20px;
    
    .detail-icon {
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100px;
      height: 100px;
      background: #f5f7fa;
      border-radius: 12px;
    }
    
    .detail-info {
      flex: 1;
      
      .detail-title {
        font-size: 22px;
        font-weight: bold;
        color: #333;
        margin: 0 0 12px 0;
      }
      
      .detail-meta {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 14px;
        color: #666;
        margin-bottom: 12px;
        flex-wrap: wrap;
      }
      
      .detail-stats {
        display: flex;
        gap: 20px;
        font-size: 14px;
        color: #999;
        
        span {
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }
  
  .detail-description {
    margin-bottom: 20px;
    
    h4 {
      margin: 0 0 12px 0;
      color: #333;
    }
    
    p {
      color: #666;
      line-height: 1.8;
      margin: 0;
    }
  }
  
  .detail-tags {
    h4 {
      margin: 0 0 12px 0;
      color: #333;
    }
  }
}
</style>
