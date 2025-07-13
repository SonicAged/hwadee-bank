<template>
  <div class="training-program">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>培训项目</span>
          <el-button type="primary" @click="openProgramDialog()" icon="Plus">发布项目</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="queryParams" @submit.prevent>
          <el-form-item label="项目名称">
            <el-input v-model="queryParams.programName" placeholder="请输入项目名称" clearable />
          </el-form-item>
          <el-form-item label="项目类型">
            <el-select v-model="queryParams.programType" placeholder="请选择类型" clearable class="wider-select">
              <el-option label="线上培训" :value="1" />
              <el-option label="线下培训" :value="2" />
              <el-option label="混合培训" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="wider-select">
              <el-option label="未开始" :value="0" />
              <el-option label="进行中" :value="1" />
              <el-option label="已结束" :value="2" />
              <el-option label="已取消" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getList">搜索</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 项目列表 -->
      <div class="program-grid">
        <div v-for="program in programList" :key="program.programId" class="program-item">
          <el-card shadow="hover" class="program-card">
            <div class="program-content">
              <div class="program-header">
                <h3 :title="program.programName">{{ program.programName }}</h3>
                <p class="program-type">{{ getTypeText(program.programType) }}</p>
              </div>
              <div class="program-body">
                <div class="program-info">
                  <div class="info-item">
                    <span class="label">学分值:</span>
                    <span class="value">{{ program.creditValue }}</span>
                  </div>
                  <div class="info-item">
                    <span class="label">费用:</span>
                    <span class="value">{{ program.cost === 0 ? '免费' : `¥${program.cost}` }}</span>
                  </div>
                  <div class="info-item">
                    <span class="label">参与人数:</span>
                    <span class="value">{{ program.currentParticipants }}/{{ program.maxParticipants }}</span>
                  </div>
                </div>
                <div class="program-time">
                  <div>开始：{{ formatDateTime(program.startTime) }}</div>
                  <div>结束：{{ formatDateTime(program.endTime) }}</div>
                  <div v-if="program.enrollDeadline">报名截止：{{ formatDateTime(program.enrollDeadline) }}</div>
                </div>
                <div v-if="program.location" class="program-location" :title="program.location">
                  <div>地点：{{ program.location }}</div>
                </div>
                <div class="program-status">
                  <el-tag :type="getStatusType(program.status)">{{ getStatusText(program.status) }}</el-tag>
                </div>
              </div>
              <div class="program-actions">
                <el-button type="primary" size="small" @click="viewProgram(program.programId)">查看详情</el-button>
                <el-button 
                  v-if="isEnrolled[program.programId]"
                  type="danger" 
                  size="small" 
                  @click="cancelEnrollment(program.programId)"
                  :disabled="isProgramStarted(program.startTime)"
                >
                  取消报名
                </el-button>
                <el-button 
                  v-else-if="hasCancelled[program.programId]"
                  type="info" 
                  size="small" 
                  disabled
                >
                  无法再次报名
                </el-button>
                <el-button 
                  v-else-if="isProgramStarted(program.startTime)"
                  type="info" 
                  size="small" 
                  disabled
                >
                  报名已截止
                </el-button>
                <el-button 
                  v-else
                  type="success" 
                  size="small" 
                  @click="enrollProgram(program.programId)"
                  :disabled="program.status !== 0 && program.status !== 1"
                >
                  报名参加
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          :current-page="queryParams.page"
          :page-size="queryParams.size"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 培训项目弹窗 -->
    <el-dialog 
      v-model="programDialog.visible" 
      :title="programDialog.isEdit ? '编辑培训项目' : '发布培训项目'"
      width="650px"
    >
      <el-form 
        ref="programFormRef"
        :model="programForm" 
        :rules="programRules" 
        label-width="100px"
      >
        <el-form-item label="项目名称" prop="programName">
          <el-input v-model="programForm.programName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目编码" prop="programCode">
          <el-input v-model="programForm.programCode" placeholder="请输入项目编码" />
        </el-form-item>
        <el-form-item label="项目类型" prop="programType">
          <el-select v-model="programForm.programType" placeholder="请选择项目类型" class="wider-select">
            <el-option label="线上培训" :value="1" />
            <el-option label="线下培训" :value="2" />
            <el-option label="混合培训" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="学分值" prop="creditValue">
          <el-input-number v-model="programForm.creditValue" :precision="2" :step="0.5" :min="0" />
        </el-form-item>
        <el-form-item label="培训费用" prop="cost">
          <el-input-number v-model="programForm.cost" :precision="2" :step="50" :min="0" />
        </el-form-item>
        <el-form-item label="最大人数" prop="maxParticipants">
          <el-input-number v-model="programForm.maxParticipants" :min="0" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="programForm.startTime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="programForm.endTime" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
        <el-form-item label="报名截止" prop="enrollDeadline">
          <el-date-picker v-model="programForm.enrollDeadline" type="datetime" placeholder="选择报名截止时间" />
        </el-form-item>
        <el-form-item label="地点" prop="location" v-if="programForm.programType !== 1">
          <el-input v-model="programForm.location" placeholder="请输入培训地点" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="programForm.status" placeholder="请选择状态" class="wider-select">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="programForm.description" type="textarea" :rows="3" placeholder="请输入项目描述" />
        </el-form-item>
        <el-form-item label="培训内容" prop="content">
          <el-input v-model="programForm.content" type="textarea" :rows="5" placeholder="请输入培训内容大纲" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="programDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 培训项目详情弹窗 -->
    <el-dialog 
      v-model="detailDialog.visible" 
      title="培训项目详情"
      width="800px"
    >
      <el-descriptions v-if="currentProgram" :column="2" border>
        <el-descriptions-item label="项目名称">{{ currentProgram.programName }}</el-descriptions-item>
        <el-descriptions-item label="项目编码">{{ currentProgram.programCode }}</el-descriptions-item>
        <el-descriptions-item label="项目类型">{{ getTypeText(currentProgram.programType) }}</el-descriptions-item>
        <el-descriptions-item label="学分值">{{ currentProgram.creditValue }}</el-descriptions-item>
        <el-descriptions-item label="培训费用">{{ currentProgram.cost === 0 ? '免费' : `¥${currentProgram.cost}` }}</el-descriptions-item>
        <el-descriptions-item label="参与人数">{{ currentProgram.currentParticipants }}/{{ currentProgram.maxParticipants }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ formatDateTime(currentProgram.startTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ formatDateTime(currentProgram.endTime) }}</el-descriptions-item>
        <el-descriptions-item label="报名截止">{{ formatDateTime(currentProgram.enrollDeadline) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentProgram.status)">{{ getStatusText(currentProgram.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="地点" :span="2" v-if="currentProgram.location">
          {{ currentProgram.location }}
        </el-descriptions-item>
        <el-descriptions-item label="项目描述" :span="2">
          {{ currentProgram.description }}
        </el-descriptions-item>
        <el-descriptions-item label="培训内容" :span="2">
          <div class="content-preview">{{ currentProgram.content }}</div>
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialog.visible = false">关闭</el-button>
          <el-button v-if="isAdmin" type="primary" @click="editProgram(currentProgram)">编辑</el-button>
          <el-button 
            v-if="currentProgram && isEnrolled[currentProgram.programId]"
            type="danger" 
            @click="cancelEnrollment(currentProgram.programId)"
            :disabled="currentProgram && isProgramStarted(currentProgram.startTime)"
          >
            取消报名
          </el-button>
          <el-button 
            v-else-if="currentProgram && hasCancelled[currentProgram.programId]"
            type="info" 
            disabled
          >
            无法再次报名
          </el-button>
          <el-button 
            v-else-if="currentProgram && isProgramStarted(currentProgram.startTime)"
            type="info" 
            disabled
          >
            报名已截止
          </el-button>
          <el-button 
            v-else-if="currentProgram"
            type="success" 
            @click="enrollProgram(currentProgram.programId)"
            :disabled="currentProgram.status !== 0 && currentProgram.status !== 1"
          >
            报名参加
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { trainingApi } from '../../api/training'
import { useAuthStore } from '../../stores/auth'
import type { TrainingProgram } from '../../types/training'
import { formatDateTime, formatDateForBackend } from '../../utils/format'

// 获取用户信息
const authStore = useAuthStore()
const isAdmin = computed(() => {
  return authStore.hasPermission('course:training:manage')
})

// 查询参数
const queryParams = reactive({
  page: 1,
  size: 10,
  programName: '',
  programType: undefined as number | undefined,
  status: undefined as number | undefined
})

// 分页数据
const programList = ref<TrainingProgram[]>([])
const total = ref(0)
const isEnrolled = ref<Record<number, boolean>>({})
const hasCancelled = ref<Record<number, boolean>>({}) // 新增：记录已取消的报名状态

// 培训项目表单
const programFormRef = ref()
const programDialog = reactive({
  visible: false,
  isEdit: false
})

// 项目详情对话框
const detailDialog = reactive({
  visible: false
})
const currentProgram = ref<TrainingProgram | null>(null)

// 表单数据
const programForm = reactive<Partial<TrainingProgram>>({
  programName: '',
  programCode: '',
  programType: 1,
  description: '',
  content: '',
  creditValue: 1,
  cost: 0,
  maxParticipants: 30,
  currentParticipants: 0,
  startTime: '',
  endTime: '',
  location: '',
  status: 0,
  enrollDeadline: ''
})

// 表单校验规则
const programRules = {
  programName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  programCode: [
    { required: true, message: '请输入项目编码', trigger: 'blur' }
  ],
  programType: [
    { required: true, message: '请选择项目类型', trigger: 'change' }
  ],
  creditValue: [
    { required: true, message: '请输入学分值', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'blur' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'blur' }
  ]
}

// 获取项目列表
const getList = async () => {
  try {
    console.log('开始获取项目列表')
    const { list, total: totalCount } = await trainingApi.getProgramList(queryParams)
    programList.value = list
    total.value = totalCount
    
    // 清空之前的报名状态
    isEnrolled.value = {}
    hasCancelled.value = {} // 清空已取消的报名状态
    
    // 检查用户是否已报名 - 添加await确保等待完成
    await checkEnrollStatus()
    console.log('项目列表和报名状态获取完成')
  } catch (error) {
    console.error('获取培训项目列表失败', error)
    ElMessage.error('获取培训项目列表失败')
  }
}

// 重置查询参数
const resetQuery = () => {
  queryParams.programName = ''
  queryParams.programType = undefined
  queryParams.status = undefined
  getList()
}

// 分页事件处理
const handleCurrentChange = async (page: number) => {
  console.log(`切换到第${page}页`)
  queryParams.page = page
  // 清空之前的报名状态
  isEnrolled.value = {}
  hasCancelled.value = {} // 清空已取消的报名状态
  await getList()
}

// 获取项目类型文本
const getTypeText = (type: number) => {
  switch (type) {
    case 1: return '线上培训'
    case 2: return '线下培训'
    case 3: return '混合培训'
    default: return '未知'
  }
}

// 获取状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '未开始'
    case 1: return '进行中'
    case 2: return '已结束'
    case 3: return '已取消'
    default: return '未知'
  }
}

// 获取状态标签类型
const getStatusType = (status: number) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return 'info'
  }
}

// 打开项目表单对话框
const openProgramDialog = () => {
  programDialog.isEdit = false
  resetForm()
  programDialog.visible = true
}

// 重置表单
const resetForm = () => {
  if (programFormRef.value) {
    programFormRef.value.resetFields()
  }
  Object.assign(programForm, {
    programId: undefined,
    programName: '',
    programCode: '',
    programType: 1,
    description: '',
    content: '',
    creditValue: 1,
    cost: 0,
    maxParticipants: 30,
    currentParticipants: 0,
    startTime: '',
    endTime: '',
    location: '',
    status: 0,
    enrollDeadline: ''
  })
}

// 提交表单
const submitForm = async () => {
  if (!programFormRef.value) return
  
  try {
    // 打印表单数据，便于调试
    console.log('提交的表单数据:', programForm)
    
    // 表单验证
    await programFormRef.value.validate((valid: boolean, fields: any) => {
      if (!valid) {
        console.error('表单验证失败:', fields)
        throw new Error('表单验证失败')
      }
    })
    
    // 检查日期格式并转换
    const formData = { ...programForm }
    
    // 转换日期格式
    if (formData.startTime) {
      console.log('转换开始时间格式，原始值:', formData.startTime)
      formData.startTime = formatDateForBackend(formData.startTime)
      console.log('转换后的开始时间:', formData.startTime)
    }
    
    if (formData.endTime) {
      console.log('转换结束时间格式，原始值:', formData.endTime)
      formData.endTime = formatDateForBackend(formData.endTime)
      console.log('转换后的结束时间:', formData.endTime)
    }
    
    if (formData.enrollDeadline) {
      console.log('转换报名截止时间格式，原始值:', formData.enrollDeadline)
      formData.enrollDeadline = formatDateForBackend(formData.enrollDeadline)
      console.log('转换后的报名截止时间:', formData.enrollDeadline)
    }
    
    console.log('转换后的表单数据:', formData)
    
    // 提交表单
    if (programDialog.isEdit && formData.programId) {
      console.log('更新培训项目:', formData.programId)
      await trainingApi.updateProgram(formData.programId, formData)
      ElMessage.success('更新成功')
    } else {
      console.log('创建培训项目')
      await trainingApi.createProgram(formData)
      ElMessage.success('创建成功')
    }
    
    programDialog.visible = false
    getList()
  } catch (error: any) {
    console.error('表单提交失败', error)
    
    // 显示更详细的错误信息
    if (error.message) {
      ElMessage.error(`提交失败: ${error.message}`)
    } else if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(`提交失败: ${error.response.data.message}`)
    } else {
      ElMessage.error('表单验证失败或提交失败，请检查表单数据')
    }
  }
}

// 查看项目详情
const viewProgram = async (programId: number) => {
  try {
    currentProgram.value = await trainingApi.getProgramById(programId)
    detailDialog.visible = true
  } catch (error) {
    console.error('获取项目详情失败', error)
    ElMessage.error('获取项目详情失败')
  }
}

// 编辑项目
const editProgram = (program: TrainingProgram | null) => {
  if (!program) return
  
  programDialog.isEdit = true
  Object.assign(programForm, program)
  detailDialog.visible = false
  programDialog.visible = true
}

// 取消报名
const cancelEnrollment = async (programId?: number) => {
  if (!programId) return
  
  try {
    // 检查项目是否已开始
    const program = programList.value.find(p => p.programId === programId);
    if (program && isProgramStarted(program.startTime)) {
      ElMessage.warning('培训已开始，无法取消报名');
      return;
    }
    
    await ElMessageBox.confirm('确定要取消报名该培训项目吗？', '确认取消', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    console.log(`开始取消报名，项目ID: ${programId}`)
    await trainingApi.cancelEnrollment(programId)
    console.log(`取消报名API调用成功，项目ID: ${programId}`)
    ElMessage.success('取消报名成功')
    
    // 立即更新报名状态
    isEnrolled.value[programId] = false
    hasCancelled.value[programId] = true // 标记为已取消
    console.log(`手动设置报名状态为false，项目ID: ${programId}，当前状态:`, isEnrolled.value)
    
    // 直接再次检查报名状态，不通过getList
    const checkResult = await trainingApi.checkUserInProgram(programId)
    console.log(`直接检查报名状态结果，项目ID: ${programId}, 是否已报名: ${checkResult}`)
    
    if (checkResult) {
      console.warn(`警告：后端返回的报名状态与预期不符，项目ID: ${programId}`)
      // 强制设置为未报名状态
      isEnrolled.value[programId] = false
      hasCancelled.value[programId] = true
    }
    
    // 获取最新的项目数据，但不重新检查报名状态
    console.log(`开始获取最新项目数据，项目ID: ${programId}`)
    const { list, total: totalCount } = await trainingApi.getProgramList(queryParams)
    programList.value = list
    total.value = totalCount
    console.log(`项目数据获取完成，当前报名状态:`, isEnrolled.value)
    
    // 如果详情弹窗打开，也需要更新当前项目详情
    if (detailDialog.visible && currentProgram.value && currentProgram.value.programId === programId) {
      currentProgram.value = await trainingApi.getProgramById(programId)
    }
  } catch (error: any) {
    if (error === 'cancel') {
      // 用户取消操作，不做处理
      return
    }
    
    console.error('取消报名失败', error)
    // 显示后端返回的错误信息
    ElMessage.error(error.message || '取消报名失败，请稍后重试')
  }
}

// 报名参加项目
const enrollProgram = async (programId?: number) => {
  if (!programId) return
  
  try {
    // 检查项目是否已开始
    const program = programList.value.find(p => p.programId === programId);
    if (program && isProgramStarted(program.startTime)) {
      ElMessage.warning('培训已开始，无法报名');
      return;
    }
    
    // 检查是否已取消过报名
    if (hasCancelled.value[programId]) {
      ElMessage.warning('您已取消过报名，无法再次报名');
      return;
    }
    
    await ElMessageBox.confirm('确定要报名参加此培训项目吗？', '确认报名', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    console.log(`开始报名，项目ID: ${programId}`)
    await trainingApi.enrollProgram(programId)
    console.log(`报名API调用成功，项目ID: ${programId}`)
    ElMessage.success('报名成功')
    
    // 立即更新报名状态
    isEnrolled.value[programId] = true
    console.log(`手动设置报名状态为true，项目ID: ${programId}，当前状态:`, isEnrolled.value)
    
    // 获取最新的项目数据，但不重新检查报名状态
    console.log(`开始获取最新项目数据，项目ID: ${programId}`)
    const { list, total: totalCount } = await trainingApi.getProgramList(queryParams)
    programList.value = list
    total.value = totalCount
    console.log(`项目数据获取完成，当前报名状态:`, isEnrolled.value)
  } catch (error: any) {
    if (error === 'cancel') {
      // 用户取消操作，不做处理
      return
    }
    
    console.error('报名失败', error)
    // 显示后端返回的错误信息
    ElMessage.error(error.message || '报名失败，请稍后重试')
  }
}

// 检查用户报名状态
const checkEnrollStatus = async () => {
  console.log('开始检查用户报名状态，项目数量:', programList.value.length)
  try {
    // 使用Promise.all并行处理所有请求，提高性能
    const enrollmentChecks = programList.value.map(program => 
      trainingApi.checkUserInProgram(program.programId)
        .then(enrolled => {
          console.log(`项目ID: ${program.programId}, 报名状态检查结果: ${enrolled}`)
          
          // 如果已经标记为已取消，保持取消状态
          if (hasCancelled.value[program.programId]) {
            console.log(`项目ID: ${program.programId} 已标记为取消状态，保持取消状态`)
            isEnrolled.value[program.programId] = false
            return { programId: program.programId, enrolled: false }
          }
          
          // 如果之前手动设置了false（取消报名），且后端也返回false，保持false状态
          if (isEnrolled.value[program.programId] === false && enrolled === false) {
            console.log(`项目ID: ${program.programId} 保持取消报名状态`)
          } else {
            isEnrolled.value[program.programId] = enrolled
          }
          return { programId: program.programId, enrolled }
        })
        .catch(error => {
          console.error(`检查项目 ${program.programId} 报名状态失败`, error)
          return { programId: program.programId, enrolled: false }
        })
    )
    
    const results = await Promise.all(enrollmentChecks)
    console.log('所有报名状态检查完成', results)
    console.log('最终报名状态:', isEnrolled.value)
    console.log('已取消状态:', hasCancelled.value)
  } catch (error) {
    console.error('检查报名状态失败', error)
  }
}

// 判断项目是否已开始
const isProgramStarted = (startTime: string) => {
  if (!startTime) return false;
  const programStartTime = new Date(startTime).getTime();
  const now = new Date().getTime();
  return now >= programStartTime;
};

onMounted(() => {
  getList()
})
</script>

<style scoped>
.training-program {
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

.search-bar {
  margin-bottom: 20px;
}

/* 增加选择框宽度 */
.wider-select {
  min-width: 140px; /* 增加宽度以适应文字 */
}

/* 确保下拉菜单也足够宽 */
:deep(.wider-select .el-select__popper) {
  min-width: 140px !important;
}

/* 表单中的选择框样式 */
.el-form-item .wider-select {
  width: 100%; /* 在表单中使用100%宽度 */
}

.program-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.program-item {
  height: 100%;
}

.program-card {
  height: 100%;
}

.program-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 400px;
}

.program-header {
  margin-bottom: 16px;
}

.program-header h3 {
  margin: 0 0 8px 0;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.program-type {
  color: #909399;
  margin: 0;
  font-size: 14px;
}

.program-body {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.program-info {
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.label {
  color: #606266;
}

.value {
  color: #303133;
  font-weight: 500;
}

.program-time {
  margin-bottom: 16px;
  font-size: 14px;
  color: #606266;
}

.program-location {
  margin-bottom: 16px;
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.program-status {
  margin-bottom: 16px;
  text-align: center;
}

.program-actions {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: auto;
  padding-top: 16px;
}

.program-actions .el-button {
  flex: 1;
  min-width: 90px;
  max-width: 120px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.content-preview {
  white-space: pre-line;
  max-height: 200px;
  overflow-y: auto;
}

.dialog-footer {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.dialog-footer .el-button {
  min-width: 90px;
}
</style> 