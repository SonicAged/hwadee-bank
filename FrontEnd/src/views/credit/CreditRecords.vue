<template>
  <div class="credit-records">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>学分记录</span>
          <el-button type="primary" icon="Refresh" @click="loadRecords">刷新</el-button>
        </div>
      </template>
      
      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-form :model="filterForm" :inline="true">
        <el-form-item label="学分类型">
          <el-select
            v-model="filterForm.creditType"
            placeholder="请选择类型"
            clearable
            @change="handleSearch"
            class="custom-select"
            popper-class="custom-popper"
          >
          <!-- 选项内容保持不变 -->
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select
            v-model="filterForm.operationType"
            placeholder="请选择操作类型"
            clearable
            @change="handleSearch"
            class="custom-select"
            popper-class="custom-popper"
          >
            <!-- 选项内容保持不变 -->
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="filterForm.status"
            placeholder="请选择状态"
            clearable
            @change="handleSearch"
            class="custom-select"
            popper-class="custom-popper"
          >
            <!-- 选项内容保持不变 -->
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
      
      <!-- 记录列表 -->
      <el-table :data="recordList" v-loading="loading" border stripe>
        <el-table-column prop="recordId" label="记录ID" width="100" />
        <el-table-column prop="creditType" label="学分类型" width="120" />
        <el-table-column prop="creditSource" label="学分来源" min-width="150" />
        <el-table-column prop="creditAmount" label="学分数量" width="120">
          <template #default="{ row }">
            <el-tag :type="getAmountType(row.operationType)" size="small">
              {{ getAmountPrefix(row.operationType) }}{{ row.creditAmount }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationType(row.operationType)" size="small">
              {{ getOperationText(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="记录时间" width="180" />
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
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
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { creditApi, type CreditRecord } from '../../api/credit'

// 用户认证
const authStore = useAuthStore()

// 响应式数据
const loading = ref(false)
const recordList = ref<CreditRecord[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 筛选表单
const filterForm = reactive({
  creditType: '',
  operationType: undefined,
  status: undefined
})

// 获取学分记录
const loadRecords = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      creditType: filterForm.creditType || undefined,
      operationType: filterForm.operationType,
      status: filterForm.status
    }

    // 如果有筛选条件，使用搜索接口
    const hasFilters = filterForm.creditType || 
                      filterForm.operationType !== undefined || 
                      filterForm.status !== undefined

    const response = hasFilters
      ? await creditApi.record.search(params)
      : await creditApi.record.getMyRecords({ page: currentPage.value, size: pageSize.value })

    recordList.value = response.list
    total.value = response.total

    // 如果是第一页，更新总数
    if (currentPage.value === 1) {
      try {
        const count = await creditApi.record.getCount()
        total.value = count
      } catch {
        // 忽略获取总数失败的错误
      }
    }
  } catch (error) {
    ElMessage.error('获取学分记录失败')
  } finally {
    loading.value = false
  }
}

// 操作类型文本
const getOperationText = (type: number) => {
  switch (type) {
    case 1: return '获得'
    case 2: return '消费'
    case 3: return '转换'
    default: return '未知'
  }
}

// 操作类型样式
const getOperationType = (type: number) => {
  switch (type) {
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'info'
    default: return ''
  }
}

// 状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '无效'
    case 1: return '有效'
    case 2: return '待审核'
    default: return '未知'
  }
}

// 状态样式
const getStatusType = (status: number) => {
  switch (status) {
    case 0: return 'danger'
    case 1: return 'success'
    case 2: return 'warning'
    default: return ''
  }
}

// 学分数量样式
const getAmountType = (operationType: number) => {
  switch (operationType) {
    case 1: return 'success'
    case 2: return 'danger'
    case 3: return 'warning'
    default: return ''
  }
}

// 学分数量前缀
const getAmountPrefix = (operationType: number) => {
  switch (operationType) {
    case 1: return '+'
    case 2: return '-'
    case 3: return '±'
    default: return ''
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadRecords()
}

// 重置
const handleReset = () => {
  filterForm.creditType = ''
  filterForm.operationType = undefined
  filterForm.status = undefined
  currentPage.value = 1
  loadRecords()
}

// 分页
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  loadRecords()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadRecords()
}

// 组件挂载时获取数据
onMounted(async () => {
  // 确保用户信息已加载
  if (!authStore.user && authStore.token) {
    try {
      await authStore.getUserInfo()
    } catch (error) {
      ElMessage.error('获取用户信息失败，请重新登录')
      return
    }
  }
  
  loadRecords()
})
</script>

<style scoped>
.credit-records {
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

.filter-bar {
  margin-bottom: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 调整按钮的样式 */
.custom-button {
  min-width: 200px;
  padding: 10px 20px;
  font-size: 14px;
  height: 40px;
  margin-left: 10px;
}

</style>