<template>
  <div class="course-tests">
    <el-card shadow="never" class="tests-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">测试与作业</span>
          <div class="header-actions">
            <el-select v-model="testType" placeholder="全部类型" class="filter-select">
              <el-option label="全部类型" value="all" />
              <el-option label="测验" value="quiz" />
              <el-option label="作业" value="assignment" />
              <el-option label="考试" value="exam" />
            </el-select>
            <el-select v-model="testStatus" placeholder="全部状态" class="filter-select">
              <el-option label="全部状态" value="all" />
              <el-option label="未开始" value="not_started" />
              <el-option label="进行中" value="in_progress" />
              <el-option label="已完成" value="completed" />
              <el-option label="已过期" value="expired" />
            </el-select>
          </div>
        </div>
      </template>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="filteredTests.length === 0" class="empty-container">
        <el-empty description="暂无测试内容" />
      </div>
      
      <div v-else class="tests-list">
        <el-card 
          v-for="test in filteredTests" 
          :key="test.testId"
          class="test-card" 
          shadow="hover"
        >
          <div class="test-content">
            <div class="test-info">
              <div class="test-name-row">
                <el-tag size="small" :type="getTestTypeColor(test.testType)">
                  {{ getTestTypeName(test.testType) }}
                </el-tag>
                <h3 class="test-name">{{ test.testName }}</h3>
                <el-tag 
                  size="small" 
                  :type="getTestStatusColor(test.status)" 
                  effect="plain"
                >
                  {{ getTestStatusName(test.status) }}
                </el-tag>
              </div>
              
              <div class="test-meta">
                <div class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  <span>开始时间: {{ formatDate(test.startTime) }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Timer /></el-icon>
                  <span>截止时间: {{ formatDate(test.endTime) }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Document /></el-icon>
                  <span>题目数量: {{ test.questionCount }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Clock /></el-icon>
                  <span>时长: {{ test.duration }}分钟</span>
                </div>
                <div class="meta-item">
                  <el-icon><Medal /></el-icon>
                  <span>分值: {{ test.totalScore }}分</span>
                </div>
              </div>
            </div>
            
            <div class="test-status" v-if="test.status === 'completed'">
              <div class="score-box">
                <div class="score">{{ test.score || '-' }}</div>
                <div class="total-score">/ {{ test.totalScore }}</div>
              </div>
              <div class="review-btn">
                <el-button type="info" size="small" @click="reviewTest(test)">查看回顾</el-button>
              </div>
            </div>
            <div class="test-actions" v-else>
              <el-button 
                type="primary" 
                :disabled="!canTakeTest(test)"
                @click="takeTest(test)"
              >
                {{ getActionButtonText(test) }}
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </el-card>

    <!-- 测验对话框 -->
    <el-dialog
      v-model="testDialogVisible"
      :title="currentTest ? currentTest.testName : ''"
      width="70%"
      destroy-on-close
    >
      <div v-if="currentTest" class="test-dialog-content">
        <div class="test-header">
          <div class="test-timer" v-if="remainingTime !== null">
            <el-icon><Timer /></el-icon>
            <span>剩余时间: {{ formatTime(remainingTime) }}</span>
          </div>
          <div class="test-progress">
            <span>进度: {{ currentQuestionIndex + 1 }} / {{ currentTest.questions?.length || 0 }}</span>
            <el-progress :percentage="(currentQuestionIndex + 1) / (currentTest.questions?.length || 1) * 100" />
          </div>
        </div>

        <div v-if="currentQuestion" class="question-container">
          <div class="question-content">
            <div class="question-number">问题 {{ currentQuestionIndex + 1 }}</div>
            <div class="question-text">{{ currentQuestion.questionText }}</div>
            
            <!-- 单选题 -->
            <div v-if="currentQuestion.type === 'single'" class="options-container">
              <el-radio-group v-model="userAnswers[currentQuestionIndex]">
                <div 
                  v-for="(option, index) in currentQuestion.options" 
                  :key="index" 
                  class="option-item"
                >
                  <el-radio :label="option.value">{{ option.text }}</el-radio>
                </div>
              </el-radio-group>
            </div>
            
            <!-- 多选题 -->
            <div v-else-if="currentQuestion.type === 'multiple'" class="options-container">
              <el-checkbox-group v-model="userAnswers[currentQuestionIndex]">
                <div 
                  v-for="(option, index) in currentQuestion.options" 
                  :key="index" 
                  class="option-item"
                >
                  <el-checkbox :label="option.value">{{ option.text }}</el-checkbox>
                </div>
              </el-checkbox-group>
            </div>
            
            <!-- 判断题 -->
            <div v-else-if="currentQuestion.type === 'boolean'" class="options-container">
              <el-radio-group v-model="userAnswers[currentQuestionIndex]">
                <div class="option-item">
                  <el-radio :label="true">正确</el-radio>
                </div>
                <div class="option-item">
                  <el-radio :label="false">错误</el-radio>
                </div>
              </el-radio-group>
            </div>
            
            <!-- 填空题 -->
            <div v-else-if="currentQuestion.type === 'text'" class="options-container">
              <el-input 
                v-model="userAnswers[currentQuestionIndex]"
                type="textarea"
                :rows="3"
                placeholder="请输入您的答案"
              />
            </div>
          </div>
        </div>
        <div v-else class="empty-question">
          <el-empty description="暂无问题" />
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="prevQuestion" :disabled="currentQuestionIndex <= 0">上一题</el-button>
          <el-button @click="nextQuestion" v-if="currentQuestionIndex < (currentTest?.questions?.length || 0) - 1">下一题</el-button>
          <el-button type="primary" @click="submitTest" v-else>提交测验</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Calendar, Clock, Document, Timer, Medal, 
  Check, Close, ArrowRight, ArrowLeft 
} from '@element-plus/icons-vue'
import type { CourseTest, TestQuestion } from '@/types/course'

// 定义测试和问题接口
interface TestQuestionOption {
  value: string
  text: string
}

// 定义props
const props = defineProps({
  courseId: {
    type: Number,
    required: true
  }
})

// 响应式状态
const loading = ref(true)
const tests = ref<CourseTest[]>([])
const testType = ref('all')
const testStatus = ref('all')
const testDialogVisible = ref(false)
const currentTest = ref<CourseTest | null>(null)
const currentQuestionIndex = ref(0)
const userAnswers = ref<(string | string[] | boolean | null)[]>([])
const remainingTime = ref<number | null>(null)
const timerInterval = ref<number | null>(null)

// 计算属性：筛选后的测试
const filteredTests = computed(() => {
  let filtered = tests.value

  if (testType.value !== 'all') {
    filtered = filtered.filter(test => test.testType === testType.value)
  }
  
  if (testStatus.value !== 'all') {
    filtered = filtered.filter(test => test.status === testStatus.value)
  }
  
  return filtered
})

// 当前问题
const currentQuestion = computed(() => {
  if (!currentTest.value || !currentTest.value.questions || currentTest.value.questions.length === 0) {
    return null
  }
  
  return currentTest.value.questions[currentQuestionIndex.value] || null
})

// 获取测试列表
const fetchTests = async () => {
  loading.value = true
  
  // 这里应该是实际的API调用
  // const response = await courseApi.getCourseTests(props.courseId)
  
  // 模拟API响应
  setTimeout(() => {
    const mockTests: CourseTest[] = [
      {
        testId: 1,
        testName: 'Java基础语法测验',
        testType: 'quiz',
        courseId: props.courseId,
        startTime: '2024-05-01 00:00:00',
        endTime: '2024-06-30 23:59:59',
        duration: 30,
        questionCount: 10,
        totalScore: 100,
        status: 'not_started',
        questions: [
          {
            questionId: 1,
            questionText: 'Java中用于声明常量的关键字是？',
            type: 'single',
            options: [
              { value: 'A', text: 'const' },
              { value: 'B', text: 'final' },
              { value: 'C', text: 'static' },
              { value: 'D', text: 'constant' }
            ],
            answer: 'B'
          },
          {
            questionId: 2,
            questionText: 'Java支持的访问修饰符包括？（多选）',
            type: 'multiple',
            options: [
              { value: 'A', text: 'public' },
              { value: 'B', text: 'protected' },
              { value: 'C', text: 'private' },
              { value: 'D', text: 'friend' }
            ],
            answer: ['A', 'B', 'C']
          }
          // 更多问题...
        ]
      },
      {
        testId: 2,
        testName: '面向对象编程作业',
        testType: 'assignment',
        courseId: props.courseId,
        startTime: '2024-05-01 00:00:00',
        endTime: '2024-05-15 23:59:59',
        duration: 120,
        questionCount: 5,
        totalScore: 50,
        status: 'completed',
        score: 45
      },
      {
        testId: 3,
        testName: 'Java期末考试',
        testType: 'exam',
        courseId: props.courseId,
        startTime: '2024-06-20 09:00:00',
        endTime: '2024-06-20 12:00:00',
        duration: 180,
        questionCount: 30,
        totalScore: 150,
        status: 'not_started'
      }
    ]
    
    tests.value = mockTests
    loading.value = false
  }, 800)
}

// 测试类型名称
const getTestTypeName = (type: string): string => {
  const types: Record<string, string> = {
    quiz: '测验',
    assignment: '作业',
    exam: '考试'
  }
  return types[type] || '未知类型'
}

// 测试类型颜色
const getTestTypeColor = (type: string): string => {
  const colors: Record<string, string> = {
    quiz: 'info',
    assignment: 'success',
    exam: 'danger'
  }
  return colors[type] || ''
}

// 测试状态名称
const getTestStatusName = (status: string): string => {
  const statuses: Record<string, string> = {
    not_started: '未开始',
    in_progress: '进行中',
    completed: '已完成',
    expired: '已过期'
  }
  return statuses[status] || '未知状态'
}

// 测试状态颜色
const getTestStatusColor = (status: string): string => {
  const colors: Record<string, string> = {
    not_started: 'info',
    in_progress: 'warning',
    completed: 'success',
    expired: 'danger'
  }
  return colors[status] || ''
}

// 格式化日期
const formatDate = (dateStr: string): string => {
  if (!dateStr) return ''
  
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 格式化时间（分:秒）
const formatTime = (seconds: number | null): string => {
  if (seconds === null) return ''
  
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${String(secs).padStart(2, '0')}`
}

// 获取操作按钮文本
const getActionButtonText = (test: CourseTest): string => {
  if (test.status === 'in_progress') {
    return '继续'
  } else if (test.status === 'not_started') {
    const now = new Date()
    const startTime = new Date(test.startTime)
    
    if (now < startTime) {
      return '未开始'
    }
    return '开始'
  } else if (test.status === 'expired') {
    return '已过期'
  }
  return '查看'
}

// 检查是否可以参加测试
const canTakeTest = (test: CourseTest): boolean => {
  if (test.status === 'completed' || test.status === 'expired') {
    return false
  }
  
  const now = new Date()
  const startTime = new Date(test.startTime)
  const endTime = new Date(test.endTime)
  
  return now >= startTime && now <= endTime
}

// 参加测试
const takeTest = (test: CourseTest): void => {
  currentTest.value = test
  currentQuestionIndex.value = 0
  testDialogVisible.value = true
  
  // 初始化用户答案
  if (test.questions) {
    userAnswers.value = new Array(test.questions.length).fill(null)
    
    // 对于多选题，初始化为空数组
    test.questions.forEach((q, index) => {
      if (q.type === 'multiple') {
        userAnswers.value[index] = []
      }
    })
  }
  
  // 设置剩余时间（如果有限时）
  if (test.duration) {
    remainingTime.value = test.duration * 60
    startTimer()
  } else {
    remainingTime.value = null
  }
}

// 开始计时器
const startTimer = (): void => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
  
  timerInterval.value = window.setInterval(() => {
    if (remainingTime.value !== null && remainingTime.value <= 0) {
      if (timerInterval.value) {
        clearInterval(timerInterval.value)
      }
      ElMessage.warning('时间已到，系统将自动提交您的答案')
      submitTest()
      return
    }
    
    if (remainingTime.value !== null) {
      remainingTime.value--
    }
  }, 1000)
}

// 停止计时器
const stopTimer = (): void => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
    timerInterval.value = null
  }
}

// 查看测试回顾
const reviewTest = (test: CourseTest): void => {
  ElMessage.info('查看测试回顾功能开发中...')
}

// 上一题
const prevQuestion = (): void => {
  if (currentQuestionIndex.value > 0) {
    currentQuestionIndex.value--
  }
}

// 下一题
const nextQuestion = (): void => {
  if (currentTest.value && currentTest.value.questions && 
      currentQuestionIndex.value < currentTest.value.questions.length - 1) {
    currentQuestionIndex.value++
  }
}

// 提交测试
const submitTest = (): void => {
  ElMessageBox.confirm('确定要提交测验吗？提交后将不能修改答案。', '提交确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 停止计时器
    stopTimer()
    
    // 关闭对话框
    testDialogVisible.value = false
    
    // 模拟提交结果
    setTimeout(() => {
      // 更新测试状态
      if (currentTest.value) {
        const testIndex = tests.value.findIndex(t => t.testId === currentTest.value!.testId)
        if (testIndex !== -1) {
          tests.value[testIndex].status = 'completed'
          tests.value[testIndex].score = 85 // 模拟评分
        }
      }
      
      ElMessage.success('测验已提交，得分：85')
    }, 500)
    
    // 这里应该是实际的API调用
    // await courseApi.submitTest(currentTest.value.testId, userAnswers.value)
  }).catch(() => {
    // 用户取消提交
  })
}

// 生命周期钩子
onMounted(() => {
  fetchTests()
})

// 监听组件销毁
onUnmounted(() => {
  stopTimer()
})

// 监听课程ID变化
watch(() => props.courseId, () => {
  fetchTests()
})
</script>

<style scoped>
.course-tests {
  margin-top: 20px;
}

.tests-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.filter-select {
  width: 140px;
}

.loading-container, .empty-container {
  padding: 40px 0;
  text-align: center;
}

.tests-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-top: 16px;
}

.test-card {
  transition: transform 0.2s;
}

.test-card:hover {
  transform: translateY(-2px);
}

.test-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.test-info {
  flex: 1;
}

.test-name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.test-name {
  margin: 0;
  font-size: 16px;
  flex: 1;
}

.test-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 14px;
}

.test-status {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.score-box {
  display: flex;
  align-items: baseline;
}

.score {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.total-score {
  font-size: 16px;
  color: #909399;
}

.test-dialog-content {
  min-height: 400px;
}

.test-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.test-timer {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #f56c6c;
  font-weight: 600;
}

.test-progress {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 200px;
}

.question-container {
  margin-top: 24px;
}

.question-number {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.question-text {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  line-height: 1.6;
}

.options-container {
  margin-top: 20px;
}

.option-item {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.option-item:last-child {
  border-bottom: none;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
}
</style> 