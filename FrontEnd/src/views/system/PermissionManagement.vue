<template>
  <div class="permission-management">
    <div class="page-header">
      <h2>权限管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 新增权限
        </el-button>
      </div>
    </div>

    <!-- 搜索表单 -->
    <el-card class="search-form">
      <el-form :inline="true" :model="queryParams" ref="queryForm">
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="queryParams.permissionName" placeholder="请输入权限名称" clearable />
        </el-form-item>
        <el-form-item label="权限类型" prop="permissionType">
                      <el-select v-model="queryParams.permissionType" placeholder="请选择权限类型" clearable class="wide-select" popper-class="wide-dropdown">
            <el-option :value="1" label="目录"></el-option>
            <el-option :value="2" label="菜单"></el-option>
            <el-option :value="3" label="按钮"></el-option>
          </el-select>
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
        :data="permissionList"
        row-key="permissionId"
        border
        stripe
      >
        <el-table-column type="index" width="50" align="center" />
        <el-table-column prop="permissionName" label="权限名称" />
        <el-table-column prop="permissionKey" label="权限标识" />
        <el-table-column prop="permissionType" label="类型" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.permissionType === 1" type="success">目录</el-tag>
            <el-tag v-else-if="scope.row.permissionType === 2" type="primary">菜单</el-tag>
            <el-tag v-else-if="scope.row.permissionType === 3" type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="icon" label="图标">
          <template #default="scope">
            <div v-if="scope.row.icon" class="icon-display">
              <el-icon>
                <component :is="resolveIconComponent(scope.row.icon)"></component>
              </el-icon>
              <span class="icon-name">{{ scope.row.icon }}</span>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="60" align="center" />
        <el-table-column prop="status" label="状态" align="center" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              link
              @click="handleEdit(scope.row)"
            >
              <el-icon><Edit /></el-icon> 编辑
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

    <!-- 添加/编辑权限对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      @close="resetForm"
      append-to-body
    >
      <el-form
        ref="permissionFormRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="100px"
      >
        <el-form-item label="上级权限" prop="parentId">
          <el-tree-select
            v-model="permissionForm.parentId"
            :data="permissionOptions"
            :props="{ label: 'label', children: 'children', value: 'id' }"
            placeholder="请选择上级权限"
            check-strictly
            :render-after-expand="false"
          />
        </el-form-item>
        <el-form-item label="权限类型" prop="permissionType">
          <el-radio-group v-model="permissionForm.permissionType">
            <el-radio :label="1">目录</el-radio>
            <el-radio :label="2">菜单</el-radio>
            <el-radio :label="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permissionKey">
          <el-input v-model="permissionForm.permissionKey" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item 
          label="路由地址" 
          prop="path" 
          v-if="permissionForm.permissionType !== 3"
        >
          <el-input v-model="permissionForm.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item 
          label="组件路径" 
          prop="component" 
          v-if="permissionForm.permissionType === 2"
        >
          <el-input v-model="permissionForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item 
          label="图标" 
          prop="icon" 
          v-if="permissionForm.permissionType !== 3"
        >
          <div class="icon-select">
            <el-input v-model="permissionForm.icon" placeholder="请输入图标名称">
              <template #append>
                <el-button @click="showIconSelector = true">
                  <el-icon><View /></el-icon>
                </el-button>
              </template>
            </el-input>
            <div v-if="permissionForm.icon" class="icon-preview">
              <span>图标预览:</span>
              <el-icon>
                <component :is="resolveIconComponent(permissionForm.icon)"></component>
              </el-icon>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="显示排序" prop="sortOrder">
          <el-input-number v-model="permissionForm.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="permissionForm.status">
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

    <!-- 图标选择器对话框 -->
    <el-dialog
      title="选择图标"
      v-model="showIconSelector"
      width="700px"
      append-to-body
    >
      <div class="icon-grid">
        <div 
          v-for="iconName in commonIcons" 
          :key="iconName" 
          class="icon-item"
          @click="selectIcon(iconName)"
        >
          <el-icon>
            <component :is="iconName"></component>
          </el-icon>
          <div class="icon-name">{{ iconName }}</div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showIconSelector = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Edit, Delete, Search, Refresh, Setting, User, Menu, Key, 
  HomeFilled, Document, Tickets, InfoFilled, Operation, Link, 
  List, Location, More, House, Shop, OfficeBuilding, Folder, 
  Connection, Monitor, Tools, Service, Star, View, Money,
  UserFilled, MessageBox, Bell, Notification, Files, Wallet, CreditCard
} from '@element-plus/icons-vue'
import request from '../../utils/request'
import { SysPermission, TreeNode, QueryParams, PageResult } from '../../types/system'
import { ResponseData } from '../../utils/request'

// 查询参数
const queryParams = reactive<QueryParams>({
  permissionName: '',
  permissionType: undefined,
  status: undefined,
  pageNum: 1,
  pageSize: 10
})

// 表格数据
const permissionList = ref<SysPermission[]>([])
const total = ref(0)
const loading = ref(false)

// 对话框控制
const dialogVisible = ref(false)
const dialogTitle = ref('添加权限')
const permissionForm = reactive<SysPermission>({
  permissionId: undefined as unknown as number,
  parentId: 0,
  permissionName: '',
  permissionKey: '',
  permissionType: 1,
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  status: 1
})
const permissionFormRef = ref()

// 表单校验规则
const permissionRules = {
  permissionName: [
    { required: true, message: '权限名称不能为空', trigger: 'blur' },
    { min: 2, max: 50, message: '权限名称长度必须在2到50个字符之间', trigger: 'blur' }
  ],
  permissionKey: [
    { required: true, message: '权限标识不能为空', trigger: 'blur' },
    { min: 2, max: 100, message: '权限标识长度必须在2到100个字符之间', trigger: 'blur' },
    { pattern: /^[a-z][:a-z0-9]*$/, message: '权限标识必须以小写字母开头，可包含小写字母、数字和冒号', trigger: 'blur' }
  ],
  permissionType: [
    { required: true, message: '权限类型不能为空', trigger: 'change' }
  ],
  path: [
    { pattern: /^\//, message: '路由地址必须以/开头', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '显示排序不能为空', trigger: 'blur' }
  ]
}

// 权限选项树
const permissionOptions = ref<TreeNode[]>([])

// 图标选择器控制
const showIconSelector = ref(false)
const commonIcons = ref([
  'Setting', 'User', 'UserFilled', 'Key', 'CreditCard', 'Wallet',
  'Document', 'Edit', 'Folder', 'Files', 'Menu', 'Bell', 'Star',
  'Location', 'List', 'Connection', 'Monitor', 'Tools', 'Service',
  'Plus', 'Delete', 'Search', 'Refresh', 'HomeFilled', 'Tickets', 
  'InfoFilled', 'Operation', 'Link', 'More', 'House', 'Shop', 
  'OfficeBuilding', 'View', 'MessageBox', 'Notification', 'Money'
])

// 监听权限类型变化
watch(() => permissionForm.permissionType, (val) => {
  if (val === 3) {
    // 如果是按钮，路由地址和组件路径为空
    permissionForm.path = ''
    permissionForm.component = ''
    permissionForm.icon = ''
  }
})

// 解析图标组件
const resolveIconComponent = (iconName: string) => {
  if (!iconName) return null;
  
  // 首字母大写
  const formatName = (name: string) => {
    return name.charAt(0).toUpperCase() + name.slice(1);
  };
  
  // 尝试处理常见的图标名称格式
  // 例如: user -> User, user-filled -> UserFilled
  if (iconName.includes('-')) {
    const parts = iconName.split('-');
    const camelCase = parts.map(formatName).join('');
    return camelCase;
  }
  
  return formatName(iconName);
};

// 选择图标
const selectIcon = (iconName: string) => {
  permissionForm.icon = iconName
  showIconSelector.value = false
}

// 获取权限列表
const getList = async () => {
  try {
    loading.value = true
    // 调整响应处理逻辑，因为响应拦截器已经返回了data部分
    const result = await request.get<PageResult<SysPermission>>('/system/permission/page', { params: queryParams })
    // result直接就是PageResult对象
    permissionList.value = result.list || []
    total.value = result.total || 0
  } catch (error) {
    console.error('获取权限列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取权限树数据
const getPermissionTree = async () => {
  try {
    const treeData = await request.get<TreeNode[]>('/system/permission/menu-tree')
    // 添加一个"顶级权限"选项
    permissionOptions.value = [{
      id: 0,
      label: '顶级权限',
      children: treeData || []
    }]
  } catch (error) {
    console.error('获取权限树失败:', error)
  }
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置
const resetQuery = () => {
  queryParams.permissionName = ''
  queryParams.permissionType = undefined
  queryParams.status = undefined
  handleQuery()
}

// 添加权限
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '添加权限'
  dialogVisible.value = true
  // 获取权限树数据
  getPermissionTree()
}

// 编辑权限
const handleEdit = (row: SysPermission) => {
  resetForm()
  dialogTitle.value = '编辑权限'
  
  // 获取权限树数据
  getPermissionTree()
  
  // 克隆对象，避免直接修改表格数据
  const { 
    permissionId, parentId, permissionName, permissionKey, 
    permissionType, path, component, icon, sortOrder, status 
  } = row
  
  Object.assign(permissionForm, { 
    permissionId, parentId, permissionName, permissionKey, 
    permissionType, path, component, icon, sortOrder, status 
  })
  
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!permissionFormRef.value) return
  
  await permissionFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        const isEdit = permissionForm.permissionId !== undefined
        const url = isEdit ? '/system/permission' : '/system/permission'
        const method = isEdit ? 'put' : 'post'
        
        await request[method](url, permissionForm)
        
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

// 删除权限
const handleDelete = (row: SysPermission) => {
  ElMessageBox.confirm(`确认删除权限"${row.permissionName}"吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        await request.delete(`/system/permission/${row.permissionId}`)
        // 请求成功即可视为删除成功
        ElMessage.success('删除成功')
        getList()
      } catch (error) {
        console.error('删除权限失败:', error)
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

// 重置表单
const resetForm = () => {
  if (permissionFormRef.value) {
    permissionFormRef.value.resetFields()
  }

  permissionForm.permissionId = 0 // 修复类型错误，避免赋值为 undefined
  permissionForm.parentId = 0
  permissionForm.permissionName = ''
  permissionForm.permissionKey = ''
  permissionForm.permissionType = 1
  permissionForm.path = ''
  permissionForm.component = ''
  permissionForm.icon = ''
  permissionForm.sortOrder = 0
  permissionForm.status = 1
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
.permission-management {
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

.icon-display {
  display: flex;
  align-items: center;
  gap: 8px;
}

.icon-name {
  font-size: 12px;
  color: #606266;
}

.icon-select {
  display: flex;
  align-items: center;
  gap: 8px;
}

.icon-preview {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 10px;
  padding: 10px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.icon-item:hover {
  background-color: #f5f7fa;
  border-color: #409eff;
  color: #409eff;
}

.icon-name {
  font-size: 12px;
  color: #606266;
  margin-top: 5px;
  text-align: center;
}
</style> 