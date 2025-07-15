<template>
  <div class="user-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div>
            <el-button type="primary" icon="Plus" @click="openUserDialog()">新增用户</el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <div class="search-form">
        <el-form :model="searchForm" :inline="true" @submit.prevent>
          <el-form-item label="用户名">
            <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="searchForm.realName" placeholder="请输入真实姓名" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable class="wide-select" popper-class="wide-dropdown">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 用户表格 -->
      <el-table :data="userList" v-loading="loading" border stripe>
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="150" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <span>{{ getGenderText(row.gender) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openUserDialog(row)">编辑</el-button>
            <el-button type="warning" link @click="handleResetPassword(row)">重置密码</el-button>
            <el-button type="danger" link @click="handleDelete(row)">禁用</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          :current-page="searchForm.page"
          :page-size="searchForm.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 用户编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      @closed="resetForm"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username" :disabled="editMode">
          <el-input v-model="userForm.username" :disabled="editMode" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!editMode">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="userForm.gender" placeholder="请选择性别" class="wide-select" popper-class="wide-dropdown">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
            <el-option label="未知" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="userForm.status"
            :active-value="1"
            :inactive-value="0"
            inline-prompt
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 重置密码对话框 -->
    <el-dialog
      title="重置密码"
      v-model="resetPwdDialogVisible"
      width="400px"
    >
      <el-form
        ref="resetPwdFormRef"
        :model="resetPwdForm"
        :rules="resetPwdFormRules"
        label-width="100px"
      >
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="resetPwdForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPwdForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetPwdDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitResetPwd" :loading="resettingPwd">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { userApi, UserInfo } from '../../api/user'

// 调试模式标志（开发时设为true，发布时设为false）
const isDev = true

// 响应式数据
const loading = ref(false)
const userList = ref<UserInfo[]>([])
const total = ref(0)
const dialogVisible = ref(false)
const editMode = ref(false)
const dialogTitle = ref('新增用户')
const submitting = ref(false)
const resetPwdDialogVisible = ref(false)
const resettingPwd = ref(false)
const currentUserId = ref<number>(0)

// 表单引用
const userFormRef = ref<FormInstance>()
const resetPwdFormRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  status: undefined as number | undefined,
  page: 1,
  size: 10
})

// 用户表单
const userForm = reactive({
  userId: undefined as number | undefined,
  username: '',
  password: '',
  realName: '',
  email: '',
  phone: '',
  gender: 0,
  status: 1
})

// 重置密码表单
const resetPwdForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const userFormRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
})

// 重置密码表单验证规则
const resetPwdFormRules = reactive<FormRules>({
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== resetPwdForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

// 获取性别文本
const getGenderText = (gender: number) => {
  switch (gender) {
    case 1: return '男'
    case 2: return '女'
    default: return '未知'
  }
}

// 搜索用户
const handleSearch = async () => {
  loading.value = true
  try {
    // 调用API获取用户列表
    const response = await userApi.getUserList(searchForm)
    userList.value = response.list
    total.value = response.total
    
    // 显示错误提示
    if (!response.list || response.list.length === 0) {
      ElMessage.info('未查询到用户数据')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const handleReset = () => {
  searchForm.username = ''
  searchForm.realName = ''
  searchForm.status = undefined
  searchForm.page = 1
  handleSearch()
}

// 打开用户对话框
const openUserDialog = (row?: UserInfo) => {
  if (row) {
    // 编辑模式
    editMode.value = true
    dialogTitle.value = '编辑用户'
    userForm.userId = row.userId
    userForm.username = row.username
    userForm.realName = row.realName
    userForm.email = row.email || ''
    userForm.phone = row.phone || ''
    userForm.gender = row.gender
    userForm.status = row.status
  } else {
    // 新增模式
    editMode.value = false
    dialogTitle.value = '新增用户'
    resetForm()
  }
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  userForm.userId = undefined
  userForm.username = ''
  userForm.password = ''
  userForm.realName = ''
  userForm.email = ''
  userForm.phone = ''
  userForm.gender = 0
  userForm.status = 1
  
  // 重置表单验证
  if (userFormRef.value) {
    userFormRef.value.resetFields()
  }
}

// 提交表单
const submitForm = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (editMode.value) {
          // 编辑用户
          await userApi.updateUser(userForm.userId!, {
            realName: userForm.realName,
            email: userForm.email,
            phone: userForm.phone,
            gender: userForm.gender,
            status: userForm.status
          })
          ElMessage.success('用户更新成功')
        } else {
          // 创建用户
          await userApi.createUser({
            username: userForm.username,
            password: userForm.password,
            realName: userForm.realName,
            email: userForm.email,
            phone: userForm.phone,
            gender: userForm.gender,
            status: userForm.status
          })
          ElMessage.success('用户创建成功')
        }
        dialogVisible.value = false
        handleSearch() // 刷新列表
      } catch (error: any) {
        console.error('操作失败:', error)
        ElMessage.error(`操作失败: ${error.message || '未知错误'}`)
      } finally {
        submitting.value = false
      }
    }
  })
}

// 处理重置密码
const handleResetPassword = (row: UserInfo) => {
  currentUserId.value = row.userId
  resetPwdForm.newPassword = ''
  resetPwdForm.confirmPassword = ''
  resetPwdDialogVisible.value = true
  
  // 重置表单验证
  if (resetPwdFormRef.value) {
    resetPwdFormRef.value.resetFields()
  }
}

// 提交重置密码
const submitResetPwd = async () => {
  if (!resetPwdFormRef.value || currentUserId.value === 0) return
  
  await resetPwdFormRef.value.validate(async (valid) => {
    if (valid) {
      resettingPwd.value = true
      try {
        await userApi.resetPassword(currentUserId.value, resetPwdForm.newPassword)
        ElMessage.success('密码重置成功')
        resetPwdDialogVisible.value = false
      } catch (error: any) {
        console.error('密码重置失败:', error)
        ElMessage.error(`密码重置失败: ${error.message || '未知错误'}`)
      } finally {
        resettingPwd.value = false
      }
    }
  })
}

// 删除用户
const handleDelete = async (row: UserInfo) => {
  try {
    await ElMessageBox.confirm(`确定要禁用用户 ${row.realName} 吗？此操作将使该用户无法登录系统，但保留其历史数据。`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    try {
      await userApi.deleteUser(row.userId)
      ElMessage.success('用户已成功禁用')
      handleSearch() // 刷新列表
    } catch (error: any) {
      console.error('禁用失败:', error)
      ElMessage.error(`禁用失败: ${error.message || '未知错误'}`)
    }
  } catch {
    // 用户取消
  }
}

// 分页处理
const handleSizeChange = (val: number) => {
  searchForm.size = val
  searchForm.page = 1
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  searchForm.page = val
  handleSearch()
}

// 添加调试函数
// 调试API
const debugApi = async () => {
  try {
    console.log('开始测试API...')
    console.log('调用参数:', searchForm)
    
    const rawResponse = await fetch(`http://localhost:8080/api/users/list?username=${searchForm.username || ''}&realName=${searchForm.realName || ''}&page=${searchForm.page}&size=${searchForm.size}`)
    const responseData = await rawResponse.json()
    
    console.log('API原始响应:', responseData)
    
    ElMessage.success('API测试完成，请查看控制台')
  } catch (error) {
    console.error('API测试失败:', error)
    ElMessage.error('API测试失败')
  }
}

// 页面加载时获取用户列表
onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
.user-management {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

.search-form {
  margin-bottom: 16px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}
</style> 