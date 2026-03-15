<template>
  <div class="user-management">
    <el-card>
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="queryParams">
          <el-form-item label="用户名">
            <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button type="primary" @click="handleAdd">新增用户</el-button>
      </div>
      
      <!-- 表格列表 -->
      <el-table :data="userList" v-loading="loading" border stripe>
        <el-table-column prop="userId" label="用户 ID" width="100" align="center" />
        <el-table-column prop="username" label="用户名" width="150" align="center" />
        <el-table-column prop="nickname" label="昵称" width="150" align="center" />
        <el-table-column prop="email" label="邮箱" width="200" align="center" />
        <el-table-column prop="phone" label="手机号" width="150" align="center" />
        <el-table-column prop="roleName" label="角色" width="150" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.roleName" type="success">
              {{ scope.row.roleName }}
            </el-tag>
            <el-tag v-else type="info">未分配角色</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" min-width="200">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-dropdown size="small" trigger="click">
              <el-button size="small">
                更多操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-button type="danger" text size="small" @click="handlePhysicalDelete(scope.row)">
                      彻底删除
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="!!userForm.userId" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.userId">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="userForm.roleId" placeholder="请选择角色" style="width: 100%">
            <el-option
              v-for="role in roleList"
              :key="role.roleId"
              :label="role.roleName"
              :value="role.roleId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUserPage, createUser, updateUser, deleteUser, physicallyDeleteUser } from '@/api/user'
import { getRoleList } from '@/api/role'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

const loading = ref(false)
const userList = ref([])
const total = ref(0)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const userFormRef = ref<FormInstance>()
const roleList = ref<any[]>([])
const userForm = reactive({
  userId: undefined,
  username: '',
  nickname: '',
  password: '',
  email: '',
  phone: '',
  status: 1,
  roleId: undefined
})

const userRules = reactive<FormRules>({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }]
})

const getList = async () => {
  loading.value = true
  try {
    const data: any = await getUserPage(queryParams)
    userList.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('获取用户列表失败', error)
  } finally {
    loading.value = false
  }
}

const getRoles = async () => {
  try {
    const data: any = await getRoleList()
    roleList.value = data
  } catch (error) {
    console.error('获取角色列表失败', error)
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const resetQuery = () => {
  queryParams.username = ''
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
  Object.assign(userForm, {
    userId: undefined,
    username: '',
    nickname: '',
    password: '',
    email: '',
    phone: '',
    status: 1,
    roleId: undefined
  })
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
  Object.assign(userForm, {
    ...row,
    roleId: row.roleId || undefined
  })
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('是否确认删除该用户？删除后数据将保留在回收站，可以恢复。', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.userId)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除用户失败', error)
    }
  })
}

const handlePhysicalDelete = (row: any) => {
  ElMessageBox.confirm(
    '警告：彻底删除将永久删除该用户的所有数据，此操作不可恢复！\n\n确定要彻底删除用户 "' + row.username + '" 吗？',
    '危险操作确认',
    {
      type: 'error',
      distinguishCancelAndClose: true,
      confirmButtonText: '确定删除',
      cancelButtonText: '取消'
    }
  ).then(async () => {
    try {
      await physicallyDeleteUser(row.userId)
      ElMessage.success('彻底删除成功')
      getList()
    } catch (error) {
      console.error('彻底删除用户失败', error)
    }
  }).catch(() => {
    // 用户取消操作
  })
}

const submitForm = async () => {
  if (!userFormRef.value) return
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (userForm.userId) {
          await updateUser(userForm)
          ElMessage.success('修改成功')
        } else {
          await createUser(userForm)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('提交失败', error)
      }
    }
  })
}

onMounted(() => {
  getList()
  getRoles()
})
</script>

<style scoped lang="scss">
.user-management {
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
