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
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="handleDetail(scope.row)">详情</el-button>
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
        <el-form-item label="分类 ID" prop="categoryId">
          <el-input-number v-model="uploadForm.categoryId" :min="1" />
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getKnowledgePage, deleteKnowledgeResource, auditKnowledgeResource, uploadKnowledgeResource, downloadKnowledgeResource } from '@/api/knowledge'
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
  categoryId: 1,
  file: null as File | null
})
const uploadRules = reactive<FormRules>({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
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
        formData.append('categoryId', uploadForm.categoryId.toString())
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
    const filePath = await downloadKnowledgeResource(row.resourceId)
    // TODO: 实现真正的下载逻辑，这里仅提示
    ElMessage.success('开始下载：' + row.title)
    console.log('文件路径：', filePath)
  } catch (error) {
    console.error('下载失败', error)
  }
}

const handleDetail = (row: any) => {
  // TODO: 跳转到详情页或显示详情弹窗
  ElMessage.info('详情功能开发中...')
}

onMounted(() => {
  getList()
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
</style>
