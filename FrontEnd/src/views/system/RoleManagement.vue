<template>
  <div class="role-management">
    <div class="page-header">
      <h2>角色管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 新增角色
        </el-button>
      </div>
    </div>

    <!-- 搜索表单 -->
    <el-card class="search-form">
      <el-form :inline="true" :model="queryParams" ref="queryForm">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="wide-select" popper-class="wide-dropdown">
            <el-option :value="1" label="启用"></el-option>
            <el-option :value="0" label="禁用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="table-container">
      <el-table
        v-loading="loading"
        :data="roleList"
        border
        row-key="roleId"
        stripe
      >
        <el-table-column type="index" width="50" align="center" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleKey" label="角色标识" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              link
              @click="handleEdit(scope.row)"
            >
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button 
              type="primary" 
              link
              @click="handlePermission(scope.row)"
            >
              <el-icon><Key /></el-icon> 权限
            </el-button>
            <el-button 
              type="danger" 
              link
              @click="handleDelete(scope.row)"
            >
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

             <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑角色对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      @close="resetForm"
      append-to-body
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="roleForm.roleKey" placeholder="请输入角色标识" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog
      title="分配权限"
      v-model="permissionDialogVisible"
      width="600px"
      append-to-body
    >
      <el-form label-width="100px">
        <el-form-item label="角色名称">
          <span>{{ currentRole?.roleName }}</span>
        </el-form-item>
        <el-form-item label="权限分配">
          <el-tree
            ref="permissionTreeRef"
            :data="permissionTree"
            show-checkbox
            node-key="id"
            :props="defaultProps"
            :default-checked-keys="checkedPermissionIds"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPermission">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh, Key } from '@element-plus/icons-vue'
import request from '../../utils/request'
import { ResponseData } from '../../utils/request'
import { SysRole, PageResult, QueryParams, TreeNode } from '../../types/system'

// 查询参数
const queryParams = reactive<QueryParams>({
  roleName: '',
  status: undefined,
  pageNum: 1,
  pageSize: 10
})

// 表格数据
const roleList = ref<SysRole[]>([])
const total = ref(0)
const loading = ref(false)

// 对话框控制
const dialogVisible = ref(false)
const dialogTitle = ref('添加角色')
const roleForm = reactive<SysRole>({
  roleId: undefined as unknown as number,
  roleName: '',
  roleKey: '',
  description: '',
  status: 1
})
const roleFormRef = ref()

// 表单校验规则
const roleRules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度必须在2到20之间', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '角色标识不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '角色标识长度必须在2到20之间', trigger: 'blur' },
    { pattern: /^[a-z][a-z0-9_]*$/, message: '角色标识必须以小写字母开头，只能包含小写字母、数字和下划线', trigger: 'blur' }
  ],
}

// 权限树配置
const permissionDialogVisible = ref(false)
const currentRole = ref<SysRole | null>(null)
const permissionTree = ref<TreeNode[]>([])
const checkedPermissionIds = ref<number[]>([])
const defaultProps = {
  children: 'children',
  label: 'label'
}
const permissionTreeRef = ref()

// 获取角色列表
const getList = async () => {
  try {
    loading.value = true
    // 调整响应处理逻辑，因为响应拦截器已经返回了data部分
    const result = await request.get<PageResult<SysRole>>('/system/role/page', { params: queryParams })
    // result直接就是PageResult对象
    roleList.value = result.list || []
    total.value = result.total || 0
  } catch (error) {
    console.error('获取角色列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置
const resetQuery = () => {
  queryParams.roleName = ''
  queryParams.status = undefined
  handleQuery()
}

// 添加角色
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '添加角色'
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = (row: SysRole) => {
  resetForm()
  dialogTitle.value = '编辑角色'
  
  // 克隆对象，避免直接修改表格数据
  const { roleId, roleName, roleKey, description, status } = row
  Object.assign(roleForm, { roleId, roleName, roleKey, description, status })
  
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!roleFormRef.value) return
  
  await roleFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        const isEdit = roleForm.roleId !== undefined
        const url = isEdit ? '/system/role' : '/system/role'
        const method = isEdit ? 'put' : 'post'
        
        const result = await request[method](url, roleForm)
        
        // 请求成功即可视为操作成功
        ElMessage.success(isEdit ? '修改成功' : '添加成功')
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('提交表单失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 删除角色
const handleDelete = (row: SysRole) => {
  ElMessageBox.confirm(`确认删除角色"${row.roleName}"吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/system/role/${row.roleId}`)
        // 请求成功即可视为删除成功
        ElMessage.success('删除成功')
        getList()
      } catch (error) {
        console.error('删除角色失败:', error)
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// 分配权限
const handlePermission = async (row: SysRole) => {
  currentRole.value = row
  permissionDialogVisible.value = true
  
  try {
    // 获取全部权限树
    const permTree = await request.get<TreeNode[]>('/system/permission/menu-tree')
    permissionTree.value = permTree || []
    
    // 获取角色已有权限ID列表
    const rolePerms = await request.get<number[]>(`/system/role/${row.roleId}/permissions`)
    checkedPermissionIds.value = rolePerms || []
  } catch (error) {
    console.error('获取权限信息失败:', error)
    ElMessage.error('获取权限信息失败')
  }
}

// 提交权限
const submitPermission = async () => {
  if (!permissionTreeRef.value || !currentRole.value) return
  
  const checkedKeys = permissionTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
  const allCheckedKeys = [...new Set([...checkedKeys, ...halfCheckedKeys])]
  
  try {
    await request.post(`/system/role/${currentRole.value.roleId}/permissions`, allCheckedKeys)
    
    // 请求成功即可视为操作成功
    ElMessage.success('权限分配成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('分配权限失败:', error)
    ElMessage.error('权限分配失败')
  }
}

// 重置表单
const resetForm = () => {
  if (roleFormRef.value) {
    roleFormRef.value.resetFields()
  }
  
  roleForm.roleId = undefined as unknown as number
  roleForm.roleName = ''
  roleForm.roleKey = ''
  roleForm.description = ''
  roleForm.status = 1
}

// 分页处理
const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  getList()
}

const handleCurrentChange = (current: number) => {
  queryParams.pageNum = current
  getList()
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.role-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.search-form {
  margin-bottom: 16px;
}

.table-container {
  margin-bottom: 16px;
}

.pagination-container {
  margin-top: 16px;
  text-align: right;
}
</style> 