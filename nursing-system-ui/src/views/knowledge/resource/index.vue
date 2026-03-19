<template>
  <div class="knowledge-resource">
    <el-card>
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="标题">
            <el-input v-model="queryParams.title" placeholder="搜索资源标题" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="选择状态" clearable style="width: 150px">
              <el-option label="待审核" value="0" />
              <el-option label="已发布" value="1" />
              <el-option label="已驳回" value="2" />
              <el-option label="已下架" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button type="primary" @click="handleUpload">上传资源</el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="resourceList" v-loading="loading" border stripe>
        <el-table-column prop="resourceId" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="fileType" label="类型" width="100" align="center">
          <template #default="scope">
            <el-tag size="small">{{ scope.row.fileType || '未知' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="大小" width="120" align="center">
          <template #default="scope">
            {{ formatFileSize(scope.row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column prop="downloadCount" label="下载" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="small" type="info" link @click="handlePreview(scope.row)" v-if="scope.row.status === '1'">预览</el-button>
            <el-button size="small" type="success" link @click="handleDownload(scope.row)" v-if="scope.row.status === '1'">下载</el-button>
            <el-button size="small" type="warning" link @click="handleAudit(scope.row)" v-if="scope.row.status === '0'">审核</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 上传资源对话框 -->
    <el-dialog title="上传资源" v-model="uploadDialogVisible" width="500px">
      <el-form :model="uploadForm" :rules="uploadRules" ref="uploadFormRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="uploadForm.title" placeholder="请输入资源标题" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="uploadForm.description" type="textarea" placeholder="资源简要描述" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="uploadForm.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="category in categoryOptions" :key="category.categoryId"
                       :label="category.categoryName" :value="category.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tagIds">
          <el-select v-model="uploadForm.tagIds" multiple placeholder="请选择标签" style="width: 100%">
            <el-option v-for="tag in tagOptions" :key="tag.tagId"
                       :label="tag.tagName" :value="tag.tagId" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件" prop="file">
          <el-upload
            ref="uploadRef"
            action="#"
            :auto-upload="false"
            :limit="1"
            :on-change="handleFileChange"
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">单个文件不超过 100MB</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUpload" :loading="uploadLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog title="资源审核" v-model="auditDialogVisible" width="400px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditAction">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input v-model="auditForm.auditRemark" type="textarea" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="auditDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAudit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="资源详情" v-model="detailDialogVisible" width="700px">
      <div v-if="currentResource" class="resource-detail">
        <!-- 基本信息 -->
        <el-descriptions :column="2" border>
          <el-descriptions-item label="资源ID">{{ currentResource.resourceId }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ currentResource.title }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">
            {{ currentResource.description || '无描述' }}
          </el-descriptions-item>
          <el-descriptions-item label="分类">{{ currentResource.categoryName || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentResource.status)">
              {{ getStatusLabel(currentResource.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 文件信息 -->
        <el-divider>文件信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="文件名">{{ currentResource.fileName }}</el-descriptions-item>
          <el-descriptions-item label="文件类型">{{ currentResource.fileType || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="文件大小">{{ currentResource.fileSizeFormatted || formatFileSize(currentResource.fileSize) }}</el-descriptions-item>
          <el-descriptions-item label="文件扩展名">{{ currentResource.fileExtension || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="MD5">{{ currentResource.md5Code || '无' }}</el-descriptions-item>
          <el-descriptions-item label="是否置顶">
            <el-tag :type="currentResource.isTop === '1' ? 'success' : 'info'">
              {{ currentResource.isTop === '1' ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 统计信息 -->
        <el-divider>统计信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="浏览次数">{{ currentResource.viewCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="下载次数">{{ currentResource.downloadCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentResource.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentResource.updateTime || '无' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 上传信息 -->
        <el-divider>上传信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="上传用户">{{ currentResource.uploadUserName || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="用户ID">{{ currentResource.uploadUserId || '未知' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 标签信息 -->
        <el-divider v-if="currentResource.tags && currentResource.tags.length > 0">标签</el-divider>
        <div v-if="currentResource.tags && currentResource.tags.length > 0" class="tags-container">
          <el-tag
            v-for="tag in currentResource.tags"
            :key="tag.tagId"
            :style="{ backgroundColor: tag.color || '#409EFF', color: 'white', margin: '0 5px 5px 0' }"
          >
            {{ tag.tagName }}
          </el-tag>
        </div>

        <!-- 审核信息 -->
        <el-divider v-if="currentResource.auditRemark">审核信息</el-divider>
        <el-descriptions v-if="currentResource.auditRemark" :column="1" border>
          <el-descriptions-item label="审核备注">{{ currentResource.auditRemark }}</el-descriptions-item>
        </el-descriptions>

        <!-- 操作按钮 -->
        <div class="detail-actions" style="margin-top: 20px; text-align: center;">
          <el-button type="primary" @click="handlePreview(currentResource)" v-if="currentResource.status === '1'">预览</el-button>
          <el-button type="success" @click="handleDownload(currentResource)" v-if="currentResource.status === '1'">下载</el-button>
          <el-button type="warning" @click="handleAudit(currentResource)" v-if="currentResource.status === '0'">审核</el-button>
          <el-button type="danger" @click="handleDelete(currentResource)">删除</el-button>
        </div>
      </div>
      <div v-else style="text-align: center; padding: 40px;">
        <el-empty description="加载中..." />
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getKnowledgePage, deleteKnowledgeResource, auditKnowledgeResource, uploadKnowledgeResource, downloadKnowledgeResource, previewKnowledgeResource, getKnowledgeDetail, getEnabledCategories, getEnabledTags } from '@/api/knowledge'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  status: ''
})

const loading = ref(false)
const resourceList = ref([])
const total = ref(0)

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const data: any = await getKnowledgePage(queryParams)
    resourceList.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取资源列表失败', error)
  } finally {
    loading.value = false
  }
}

// 搜索和重置
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}
const resetQuery = () => {
  queryParams.title = ''
  queryParams.status = ''
  handleQuery()
}

// 分页处理
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

// 状态标签转换
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    '0': 'warning',
    '1': 'success',
    '2': 'danger',
    '3': 'info'
  }
  return map[status] || 'info'
}
const getStatusLabel = (status: string) => {
  const map: Record<string, string> = {
    '0': '待审核',
    '1': '已发布',
    '2': '已驳回',
    '3': '已下架'
  }
  return map[status] || '未知'
}

// 文件大小格式化
const formatFileSize = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 上传相关
const uploadDialogVisible = ref(false)
const uploadLoading = ref(false)
const uploadFormRef = ref<FormInstance>()
const uploadForm = reactive({
  title: '',
  description: '',
  categoryId: null as number | null,
  tagIds: [] as number[],
  file: null as File | null
})
const uploadRules = reactive<FormRules>({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  tagIds: [{ type: 'array', required: false, message: '请选择标签', trigger: 'change' }]
})

const handleUpload = () => {
  uploadDialogVisible.value = true
  uploadForm.title = ''
  uploadForm.description = ''
  uploadForm.file = null
}

const handleFileChange = (file: any) => {
  uploadForm.file = file.raw
}

const submitUpload = async () => {
  if (!uploadFormRef.value) return
  await uploadFormRef.value.validate(async (valid) => {
    if (valid) {
      if (!uploadForm.file) {
        ElMessage.warning('请选择文件')
        return
      }
      uploadLoading.value = true
      try {
        const formData = new FormData()
        formData.append('title', uploadForm.title)
        formData.append('description', uploadForm.description)
        formData.append('categoryId', uploadForm.categoryId!.toString())

        // 添加标签（如果有）
        if (uploadForm.tagIds && uploadForm.tagIds.length > 0) {
          uploadForm.tagIds.forEach((tagId, index) => {
            formData.append(`tagIds[${index}]`, tagId.toString())
          })
        }

        formData.append('file', uploadForm.file)

        await uploadKnowledgeResource(formData)
        ElMessage.success('上传成功')
        uploadDialogVisible.value = false
        getList()
      } catch (error) {
        console.error('上传失败', error)
      } finally {
        uploadLoading.value = false
      }
    }
  })
}

// 审核相关
const auditDialogVisible = ref(false)
const currentResourceId = ref<number | null>(null)
const auditForm = reactive({
  auditAction: '1',
  auditRemark: ''
})

// 详情相关
const detailDialogVisible = ref(false)
const currentResource = ref<any>(null)
const detailLoading = ref(false)

// 分类和标签选项
const categoryOptions = ref<any[]>([])
const tagOptions = ref<any[]>([])

const handleAudit = (row: any) => {
  currentResourceId.value = row.resourceId
  auditForm.auditAction = '1'
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
}

const submitAudit = async () => {
  if (!currentResourceId.value) return
  try {
    await auditKnowledgeResource(currentResourceId.value, auditForm)
    ElMessage.success('操作成功')
    auditDialogVisible.value = false
    getList()
  } catch (error) {
    console.error('审核失败', error)
  }
}

// 删除和下载
const handleDelete = (row: any) => {
  ElMessageBox.confirm('是否确定删除该资源？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteKnowledgeResource(row.resourceId)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

const handleDownload = async (row: any) => {
  try {
    const result = await downloadKnowledgeResource(row.resourceId)
    const filePath = result.data

    // 创建隐藏的a标签触发下载
    const link = document.createElement('a')
    link.href = filePath
    link.download = row.fileName || row.title
    link.style.display = 'none'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    ElMessage.success('开始下载：' + row.title)
  } catch (error) {
    console.error('下载失败', error)
    ElMessage.error('下载失败，请稍后重试')
  }
}

const handlePreview = async (row: any) => {
  try {
    const result = await previewKnowledgeResource(row.resourceId)
    const previewUrl = result.data

    // 在新窗口打开预览
    window.open(previewUrl, '_blank')

    ElMessage.success('正在打开预览...')
  } catch (error) {
    console.error('预览失败', error)
    ElMessage.error('该文件类型不支持在线预览或预览失败')
  }
}

const handleDetail = async (row: any) => {
  detailLoading.value = true
  detailDialogVisible.value = true
  currentResource.value = null

  try {
    const result = await getKnowledgeDetail(row.resourceId)
    currentResource.value = result
    addLog(`获取详情成功：${row.title}`, 'success')
  } catch (error) {
    console.error('获取详情失败', error)
    ElMessage.error('获取详情失败')
    detailDialogVisible.value = false
    addLog(`获取详情失败：${error.message}`, 'error')
  } finally {
    detailLoading.value = false
  }
}

// 添加日志函数（如果不存在）
const addLog = (message: string, type: 'success' | 'error' | 'info' = 'info') => {
  console.log(`[${type.toUpperCase()}] ${message}`)
  // 这里可以添加更复杂的日志记录逻辑
}

// 加载分类和标签选项
const loadOptions = async () => {
  try {
    // 加载分类
    const categories = await getEnabledCategories()
    categoryOptions.value = categories

    // 加载标签
    const tags = await getEnabledTags()
    tagOptions.value = tags

    addLog('分类和标签选项加载成功', 'success')
  } catch (error) {
    console.error('加载选项失败', error)
    addLog('加载分类和标签选项失败', 'error')
  }
}

onMounted(() => {
  getList()
  loadOptions()
})
</script>

<style scoped lang="scss">
.knowledge-resource {
  .search-bar {
    margin-bottom: 20px;
  }
  .action-bar {
    margin-bottom: 20px;
  }
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.resource-detail {
  .el-divider {
    margin: 20px 0;
  }

  .tags-container {
    display: flex;
    flex-wrap: wrap;
    margin: 10px 0;
  }

  .detail-actions {
    .el-button {
      margin: 0 5px;
    }
  }
}

:deep(.el-descriptions) {
  margin-bottom: 10px;

  .el-descriptions__label {
    width: 120px;
    font-weight: bold;
  }

  .el-descriptions__content {
    min-width: 200px;
  }
}
</style>
