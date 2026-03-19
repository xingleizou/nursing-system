<template>
  <div class="knowledge-category">
    <el-card>
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="分类名称">
            <el-input v-model="queryParams.categoryName" placeholder="搜索分类名称" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button type="primary" @click="handleAdd">新增分类</el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="categoryList" v-loading="loading" border stripe>
        <el-table-column prop="categoryId" label="ID" width="80" align="center" />
        <el-table-column prop="categoryName" label="分类名称" min-width="200" />
        <el-table-column prop="parentId" label="父级 ID" width="100" align="center" />
        <el-table-column prop="orderNum" label="排序" width="100" align="center" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
              {{ scope.row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
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
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父级 ID" prop="parentId">
          <el-input-number v-model="form.parentId" :min="0" placeholder="默认为 0 表示一级分类" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="分类描述" />
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
import { getCategoryPage, addCategory, updateCategory, deleteCategory } from '@/api/knowledge'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  categoryName: ''
})

const loading = ref(false)
const categoryList = ref([])
const total = ref(0)

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const data: any = await getCategoryPage(queryParams)
    categoryList.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取分类列表失败', error)
    ElMessage.error('获取分类列表失败')
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
  queryParams.categoryName = ''
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
  categoryId: null as number | null,
  categoryName: '',
  parentId: 0,
  orderNum: 0,
  description: '',
  status: '1'
})

const rules = reactive<FormRules>({
  categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
})

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增分类'
  form.categoryId = null
  form.categoryName = ''
  form.parentId = 0
  form.orderNum = 0
  form.description = ''
  form.status = '1'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑分类'
  form.categoryId = row.categoryId
  form.categoryName = row.categoryName
  form.parentId = row.parentId
  form.orderNum = row.orderNum
  form.description = row.description
  form.status = row.status
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.categoryId) {
          await updateCategory(form)
        } else {
          await addCategory(form)
        }

        ElMessage.success(dialogTitle.value === '新增分类' ? '新增成功' : '修改成功')
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('保存失败', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm('是否确定删除该分类？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory(row.categoryId)

      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败', error)
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.knowledge-category {
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
