<template>
  <div class="knowledge-tag">
    <el-card>
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="标签名称">
            <el-input v-model="queryParams.tagName" placeholder="搜索标签名称" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button type="primary" @click="handleAdd">新增标签</el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="tagList" v-loading="loading" border stripe>
        <el-table-column prop="tagId" label="ID" width="80" align="center" />
        <el-table-column prop="tagName" label="标签名称" min-width="200" />
        <el-table-column prop="categoryIds" label="关联分类" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
              {{ scope.row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column prop="updateTime" label="更新时间" width="180" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标签名称" prop="tagName">
          <el-input v-model="form.tagName" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="关联分类" prop="categoryIds">
          <el-select v-model="form.categoryIds" multiple placeholder="请选择关联分类" style="width: 100%">
            <el-option
              v-for="item in categoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="loading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tagName: ''
})

const loading = ref(false)
const tagList = ref([])
const total = ref(0)
const categoryOptions = ref([
  { value: 1, label: '护理知识' },
  { value: 2, label: '医疗文档' }
])

// 获取列表数据（模拟）
const getList = async () => {
  loading.value = true
  try {
    // TODO: 调用后端 API 获取标签列表
    // const data: any = await getTagPage(queryParams)
    // tagList.value = data.records
    // total.value = data.total
    
    // 模拟数据
    tagList.value = [
      {
        tagId: 1,
        tagName: '基础护理',
        categoryIds: '护理知识',
        sort: 1,
        status: '1',
        createTime: '2024-01-01 10:00:00',
        updateTime: '2024-01-01 10:00:00'
      },
      {
        tagId: 2,
        tagName: '临床护理',
        categoryIds: '护理知识，医疗文档',
        sort: 2,
        status: '1',
        createTime: '2024-01-02 10:00:00',
        updateTime: '2024-01-02 10:00:00'
      }
    ]
    total.value = tagList.value.length
  } catch (error) {
    console.error('获取标签列表失败', error)
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
  queryParams.tagName = ''
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

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive({
  tagId: null as number | null,
  tagName: '',
  categoryIds: [] as number[],
  sort: 0,
  status: '1'
})

const rules = reactive<FormRules>({
  tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
})

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增标签'
  form.tagId = null
  form.tagName = ''
  form.categoryIds = []
  form.sort = 0
  form.status = '1'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑标签'
  form.tagId = row.tagId
  form.tagName = row.tagName
  // TODO: 需要将 categoryIds 字符串转换为数组
  form.categoryIds = row.categoryIds ? row.categoryIds.split(',').map((id: string) => parseInt(id)) : []
  form.sort = row.sort
  form.status = row.status
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // TODO: 调用后端 API 保存标签
        // if (form.tagId) {
        //   await updateTag(form)
        // } else {
        //   await addTag(form)
        // }
        
        ElMessage.success(dialogTitle.value === '新增标签' ? '新增成功' : '修改成功')
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('保存失败', error)
      }
    }
  })
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm('是否确定删除该标签？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      // TODO: 调用后端 API 删除标签
      // await deleteTag(row.tagId)
      
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.knowledge-tag {
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
