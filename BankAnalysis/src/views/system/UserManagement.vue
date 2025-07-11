<template>
  <div class="user-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增用户</el-button>
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
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
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
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link @click="handleResetPassword(row)">重置密码</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

interface User {
  userId: number
  username: string
  realName: string
  email: string
  phone: string
  gender: number
  status: number
  createTime: string
}

// 响应式数据
const loading = ref(false)
const userList = ref<User[]>([])
const total = ref(0)

const searchForm = reactive({
  username: '',
  realName: '',
  status: undefined,
  page: 1,
  size: 10
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
    // 这里应该调用API获取用户列表
    // const response = await getUserList(searchForm)
    // userList.value = response.data
    // total.value = response.total
    
    // 模拟数据
    userList.value = [
      {
        userId: 1,
        username: 'admin',
        realName: '系统管理员',
        email: 'admin@example.com',
        phone: '13800138000',
        gender: 1,
        status: 1,
        createTime: '2024-01-01 10:00:00'
      },
      {
        userId: 2,
        username: 'teacher1',
        realName: '张老师',
        email: 'teacher1@example.com',
        phone: '13800138001',
        gender: 1,
        status: 1,
        createTime: '2024-01-02 10:00:00'
      }
    ]
    total.value = 50
  } catch (error) {
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

// 新增用户
const handleAdd = () => {
  ElMessage.info('新增用户功能开发中...')
}

// 编辑用户
const handleEdit = (row: any) => {
  ElMessage.info(`编辑用户: ${row.realName}`)
}

// 重置密码
const handleResetPassword = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要重置用户 ${row.realName} 的密码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('密码重置成功')
  } catch {
    // 用户取消
  }
}

// 删除用户
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要删除用户 ${row.realName} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('用户删除成功')
    handleSearch()
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
</style> 