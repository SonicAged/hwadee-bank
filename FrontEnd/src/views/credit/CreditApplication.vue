<template>
  <div class="credit-application">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>学分申请管理</span>
          <el-button type="primary" icon="Plus" @click="showSubmitDialog = true">新建申请</el-button>
        </div>
      </template>
      
      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-form :model="filterForm" :inline="true">
          <el-form-item label="申请状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable @change="handleSearch">
              <el-option label="待审核" :value="1" />
              <el-option label="已通过" :value="3" />
              <el-option label="已拒绝" :value="4" />
            </el-select>
          </el-form-item>
          <el-col :span="6">
            <el-form-item label="申请类型">
              <el-input v-model="filterForm.applicationType" placeholder="请输入申请类型" clearable @change="handleSearch" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="成果名称">
              <el-input v-model="filterForm.achievementName" placeholder="请输入成果名称" clearable @change="handleSearch" />
            </el-form-item>
          </el-col>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 申请列表 -->
      <el-table :data="applicationList" v-loading="loading" border stripe>
        <el-table-column prop="applicationId" label="申请ID" width="100" />
        <el-table-column prop="applicationType" label="申请类型" width="120" />
        <el-table-column prop="achievementName" label="成果名称" min-width="150" />
        <el-table-column prop="appliedCredits" label="申请学分" width="100">
          <template #default="{ row }">
            <el-tag type="success">{{ row.appliedCredits }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column prop="rejectionReason" label="拒绝原因" min-width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewApplication(row)">查看</el-button>
            <el-button v-if="row.status === 1" type="warning" link @click="editApplication(row)">编辑</el-button>
            <el-button v-if="row.status === 1" type="success" link @click="reviewApplication(row, 3)">通过</el-button>
            <el-button v-if="row.status === 1" type="danger" link @click="reviewApplication(row, 4)">拒绝</el-button>
          </template>
        </el-table-column>
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

    <!-- 提交申请对话框 -->
    <el-dialog
      v-model="showSubmitDialog"
      title="提交学分申请"
      width="500px"
      :before-close="handleCloseSubmitDialog"
    >
      <el-form
        ref="submitFormRef"
        :model="submitForm"
        :rules="submitRules"
        label-width="100px"
      >
        <el-form-item label="申请类型" prop="creditType">
          <el-select v-model="submitForm.creditType" placeholder="请选择申请类型">
            <el-option label="学历教育" value="学历教育" />
            <el-option label="职业培训" value="职业培训" />
            <el-option label="技能证书" value="技能证书" />
            <el-option label="在线课程" value="在线课程" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="成果名称" prop="creditSource">
          <el-input v-model="submitForm.creditSource" placeholder="请输入成果名称" />
        </el-form-item>
        <el-form-item label="申请学分" prop="creditAmount">
          <el-input-number v-model="submitForm.creditAmount" :min="0.1" :step="0.1" :precision="1" />
        </el-form-item>
        <el-form-item label="成果描述" prop="description">
          <el-input
            v-model="submitForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入成果描述"
          />
        </el-form-item>
        <el-form-item label="证明材料" prop="evidenceUrl">
          <el-input v-model="submitForm.evidenceUrl" placeholder="请输入证明材料文件路径或URL" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showSubmitDialog = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">提交</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="showReviewDialog"
      title="审核申请"
      width="400px"
    >
      <el-form label-width="100px">
        <el-form-item label="审核结果">
          <el-tag :type="reviewForm.status === 1 ? 'success' : 'danger'">
            {{ reviewForm.status === 1 ? '通过' : '拒绝' }}
          </el-tag>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input
            v-model="reviewForm.reviewComment"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showReviewDialog = false">取消</el-button>
          <el-button type="primary" :loading="reviewing" @click="handleReview">确认审核</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { creditApi, type CreditApplication } from '../../api/credit'

// 用户认证
const authStore = useAuthStore()

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const reviewing = ref(false)
const applicationList = ref<CreditApplication[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const showSubmitDialog = ref(false)
const showReviewDialog = ref(false)

const submitFormRef = ref<FormInstance>()

// 获取当前用户信息
const currentUser = authStore.user

// 筛选表单
const filterForm = reactive({
  status: undefined,
  creditType: '',
  applicationType: '',
  achievementName: ''
})

// 提交表单
const submitForm = reactive({
  creditType: '',       // 对应后端的applicationType
  creditSource: '',     // 对应后端的achievementName
  creditAmount: 1.0,    // 对应后端的appliedCredits
  description: '',      // 对应后端的achievementDescription
  evidenceUrl: ''       // 对应后端的evidenceFiles
})

// 审核表单
const reviewForm = reactive({
  applicationId: null as number | null,
  status: 1,
  reviewComment: ''
})

// 表单验证规则
const submitRules: FormRules = {
  creditType: [
    { required: true, message: '请选择申请类型', trigger: 'change' }
  ],
  creditSource: [
    { required: true, message: '请输入成果名称', trigger: 'blur' }
  ],
  creditAmount: [
    { required: true, message: '请输入申请学分', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入成果描述', trigger: 'blur' }
  ]
}

// 获取申请列表
const loadApplicationList = async () => {
  loading.value = true
  try {
    if (!currentUser?.userId) {
      ElMessage.warning('请先登录')
      return
    }

    // 检查是否有筛选条件
    const hasFilters = filterForm.status !== undefined || 
                      filterForm.applicationType || 
                      filterForm.achievementName

    const params = {
      page: currentPage.value,
      size: pageSize.value,
      status: filterForm.status,
      applicationType: filterForm.applicationType || undefined,
      achievementName: filterForm.achievementName || undefined
    }

    const response = hasFilters
      ? await creditApi.application.search(params)
      : await creditApi.application.getList(params)

    applicationList.value = response.list
    total.value = response.total
  } catch (error) {
    ElMessage.error('获取申请列表失败')
  } finally {
    loading.value = false
  }
}

// 状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case 1: return '待审核'
    case 2: return '审核中'
    case 3: return '已通过'
    case 4: return '已拒绝'
    case 5: return '已撤回'
    default: return '未知'
  }
}

// 状态类型
const getStatusType = (status: number) => {
  switch (status) {
    case 1: return 'warning'  // 待审核
    case 2: return 'info'     // 审核中
    case 3: return 'success'  // 已通过
    case 4: return 'danger'   // 已拒绝
    case 5: return ''         // 已撤回
    default: return 'info'
  }
}

// 查看申请
const viewApplication = (row: CreditApplication) => {
  ElMessage.info(`查看申请ID: ${row.applicationId}`)
}

// 编辑申请
const editApplication = (row: CreditApplication) => {
  // 将后端数据映射到前端表单字段
  let evidenceUrl = ''
  if (row.evidenceFiles) {
    try {
      // 尝试解析 JSON 格式的证明材料
      const evidenceArray = JSON.parse(row.evidenceFiles)
      if (Array.isArray(evidenceArray) && evidenceArray.length > 0) {
        evidenceUrl = evidenceArray[0] // 取第一个文件路径
      }
    } catch (e) {
      // 如果不是 JSON 格式，直接使用原值
      evidenceUrl = row.evidenceFiles
    }
  }
  
  Object.assign(submitForm, {
    creditType: row.applicationType || '',
    creditSource: row.achievementName || '',
    creditAmount: row.appliedCredits || 1.0,
    description: row.achievementDescription || '',
    evidenceUrl: evidenceUrl
  })
  showSubmitDialog.value = true
}

// 审核申请
const reviewApplication = (row: CreditApplication, status: number) => {
  reviewForm.applicationId = row.applicationId
  reviewForm.status = status
  reviewForm.reviewComment = ''
  showReviewDialog.value = true
}

// 重置提交表单
const resetSubmitForm = () => {
  Object.assign(submitForm, {
    creditType: '',
    creditSource: '',
    creditAmount: 1.0,
    description: '',
    evidenceUrl: ''
  })
}

// 提交申请
const handleSubmit = async () => {
  if (!submitFormRef.value) return

  try {
    await submitFormRef.value.validate()
    submitting.value = true

    // 确保用户已登录
    if (!currentUser?.userId) {
      ElMessage.warning('请先登录')
      submitting.value = false
      return
    }

    await creditApi.application.submit({
      userId: currentUser.userId,
      applicationType: submitForm.creditType,
      achievementName: submitForm.creditSource,
      achievementDescription: submitForm.description,
      appliedCredits: submitForm.creditAmount,
      evidenceFiles: submitForm.evidenceUrl
    })

    ElMessage.success('申请提交成功')
    showSubmitDialog.value = false
    resetSubmitForm()
    loadApplicationList()
  } catch (error) {
    ElMessage.error('申请提交失败')
  } finally {
    submitting.value = false
  }
}

// 确认审核
const handleReview = async () => {
  if (!reviewForm.applicationId) return

  reviewing.value = true
  try {
    // 确保用户已登录
    if (!currentUser?.userId) {
      ElMessage.warning('请先登录')
      reviewing.value = false
      return
    }
    
    // 获取当前用户ID
    const reviewerId = currentUser.userId
    
    // 调用API，传递当前用户ID作为审核人ID
    await creditApi.application.review(
      reviewForm.applicationId,
      reviewForm.status,
      reviewForm.reviewComment,
      reviewerId
    )

    ElMessage.success('审核完成')
    showReviewDialog.value = false
    loadApplicationList()
  } catch (error) {
    ElMessage.error('审核失败')
  } finally {
    reviewing.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadApplicationList()
}

// 重置
const handleReset = () => {
  filterForm.status = undefined
  filterForm.creditType = ''
  filterForm.applicationType = ''
  filterForm.achievementName = ''
  handleSearch()
}

// 分页
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadApplicationList()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadApplicationList()
}

// 关闭提交对话框
const handleCloseSubmitDialog = () => {
  resetSubmitForm()
  showSubmitDialog.value = false
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
  
  loadApplicationList()
})
</script>

<style scoped>
.credit-application {
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

.dialog-footer {
  text-align: right;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 