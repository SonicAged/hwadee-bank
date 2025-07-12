<template>
  <div class="learning-progress">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的学习进度</span>
          <el-select v-model="sortOption" placeholder="排序方式" size="small">
            <el-option label="最近学习" value="recent" />
            <el-option label="完成度（高到低）" value="completion_desc" />
            <el-option label="完成度（低到高）" value="completion_asc" />
            <el-option label="开课时间" value="start_date" />
          </el-select>
        </div>
      </template>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>

      <div v-else-if="enrolledCourses.length === 0" class="empty-container">
        <el-empty description="暂无课程记录" />
        <div class="empty-action">
          <el-button type="primary" @click="goToCourseList">去选课</el-button>
        </div>
      </div>

      <div v-else class="progress-list">
        <el-card 
          v-for="course in enrolledCourses" 
          :key="course.courseId" 
          class="course-card" 
          shadow="hover"
          @click="viewCourseDetail(course.courseId)"
        >
          <div class="course-content">
            <div class="course-info">
              <h3 class="course-name">{{ course.courseName }}</h3>
              <div class="course-meta">
                <div class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ course.startDate }} - {{ course.endDate }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Timer /></el-icon>
                  <span>最近学习: {{ formatDate(course.lastLearningTime) || '未学习' }}</span>
                </div>
              </div>
              
              <div class="progress-container">
                <div class="progress-header">
                  <span>完成度: {{ course.completionRate }}%</span>
                  <span>{{ course.completedSections }}/{{ course.totalSections }} 小节</span>
                </div>
                <el-progress 
                  :percentage="course.completionRate" 
                  :status="getProgressStatus(course.completionRate)"
                  :stroke-width="10"
                />
              </div>
              
              <div class="course-stats">
                <div class="stat-item">
                  <div class="stat-value">{{ formatDuration(course.totalLearningTime) }}</div>
                  <div class="stat-label">学习时长</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ course.testScore || '-' }}</div>
                  <div class="stat-label">测验分数</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ course.assignmentCount || 0 }}</div>
                  <div class="stat-label">作业完成</div>
                </div>
              </div>
            </div>
            
            <div class="course-actions">
              <el-button type="primary" size="small" @click.stop="continueLearning(course)">
                继续学习
              </el-button>
              <el-tag 
                :type="getLearningStatusType(course.learningStatus)" 
                effect="plain"
                size="small"
              >
                {{ getLearningStatusText(course.learningStatus) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <div class="statistics-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-card-content">
                <div class="stat-card-value">{{ statistics.totalCourses }}</div>
                <div class="stat-card-label">在学课程</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-card-content">
                <div class="stat-card-value">{{ statistics.completedCourses }}</div>
                <div class="stat-card-label">已完成课程</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-card-content">
                <div class="stat-card-value">{{ formatDuration(statistics.totalLearningTime) }}</div>
                <div class="stat-card-label">总学习时长</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="stat-card">
              <div class="stat-card-content">
                <div class="stat-card-value">{{ statistics.averageScore }}</div>
                <div class="stat-card-label">平均分数</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <div class="learning-chart">
        <div class="chart-header">
          <h3>学习趋势</h3>
          <el-radio-group v-model="chartPeriod" size="small">
            <el-radio-button label="week">本周</el-radio-button>
            <el-radio-button label="month">本月</el-radio-button>
            <el-radio-button label="year">全年</el-radio-button>
          </el-radio-group>
        </div>
        <!-- 这里放置学习时长趋势图表，可使用ECharts等库 -->
        <div class="chart-placeholder">
          <el-empty description="图表加载中...">
            <template #image>
              <el-icon style="font-size: 60px;"><PieChart /></el-icon>
            </template>
          </el-empty>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Calendar, Timer, Document, PieChart, Trophy } from '@element-plus/icons-vue'
import { courseApi } from '../../api/course'

// 定义课程学习记录接口
interface EnrolledCourse {
  courseId: number
  courseName: string
  startDate: string
  endDate: string
  lastLearningTime?: string
  completionRate: number
  completedSections: number
  totalSections: number
  totalLearningTime: number
  testScore?: number
  assignmentCount: number
  learningStatus: string
}

// 定义学习统计接口
interface LearningStatistics {
  totalCourses: number
  completedCourses: number
  totalLearningTime: number
  averageScore: number
}

const router = useRouter()
const loading = ref(true)
const enrolledCourses = ref<EnrolledCourse[]>([])
const sortOption = ref('recent')
const chartPeriod = ref('week')

// 学习统计数据
const statistics = ref<LearningStatistics>({
  totalCourses: 0,
  completedCourses: 0,
  totalLearningTime: 0,
  averageScore: 0
})

// 获取我的课程列表
const fetchEnrolledCourses = async () => {
  loading.value = true
  
  // 这里应该是实际的API调用
  // const response = await courseApi.getEnrolledCourses()
  
  // 模拟API响应
  setTimeout(() => {
    const mockCourses: EnrolledCourse[] = [
      {
        courseId: 1,
        courseName: 'Java高级编程',
        startDate: '2024-03-01',
        endDate: '2024-06-30',
        lastLearningTime: '2024-05-10 15:30:00',
        completionRate: 65,
        completedSections: 13,
        totalSections: 20,
        totalLearningTime: 480, // 分钟
        testScore: 85,
        assignmentCount: 3,
        learningStatus: 'in_progress'
      },
      {
        courseId: 2,
        courseName: '数据库系统原理',
        startDate: '2024-02-15',
        endDate: '2024-05-31',
        lastLearningTime: '2024-05-05 10:15:00',
        completionRate: 90,
        completedSections: 18,
        totalSections: 20,
        totalLearningTime: 600, // 分钟
        testScore: 92,
        assignmentCount: 4,
        learningStatus: 'nearly_complete'
      },
      {
        courseId: 3,
        courseName: 'Web前端开发',
        startDate: '2024-04-01',
        endDate: '2024-07-15',
        lastLearningTime: '2024-04-25 09:20:00',
        completionRate: 25,
        completedSections: 5,
        totalSections: 20,
        totalLearningTime: 180, // 分钟
        testScore: 75,
        assignmentCount: 1,
        learningStatus: 'just_started'
      }
    ]
    
    // 模拟统计数据
    statistics.value = {
      totalCourses: 3,
      completedCourses: 1,
      totalLearningTime: 1260, // 分钟
      averageScore: 84
    }
    
    enrolledCourses.value = mockCourses
    loading.value = false
  }, 800)
}

// 格式化日期
const formatDate = (dateStr: string | undefined): string => {
  if (!dateStr) return ''
  
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 格式化学习时长
const formatDuration = (minutes: number): string => {
  if (!minutes) return '0小时'
  
  if (minutes < 60) {
    return `${minutes}分钟`
  } else {
    const hours = Math.floor(minutes / 60)
    const mins = minutes % 60
    return mins > 0 ? `${hours}小时${mins}分钟` : `${hours}小时`
  }
}

// 获取进度条状态
const getProgressStatus = (percentage: number): string => {
  if (percentage >= 100) {
    return 'success'
  } else if (percentage >= 50) {
    return 'warning'
  } else {
    return ''
  }
}

// 学习状态文本
const getLearningStatusText = (status: string): string => {
  const statuses: Record<string, string> = {
    'not_started': '未开始',
    'just_started': '刚开始',
    'in_progress': '学习中',
    'nearly_complete': '接近完成',
    'completed': '已完成',
    'expired': '已过期'
  }
  return statuses[status] || '未知状态'
}

// 学习状态样式
const getLearningStatusType = (status: string): string => {
  const types: Record<string, string> = {
    'not_started': 'info',
    'just_started': 'info',
    'in_progress': 'warning',
    'nearly_complete': 'success',
    'completed': 'success',
    'expired': 'danger'
  }
  return types[status] || ''
}

// 查看课程详情
const viewCourseDetail = (courseId: number): void => {
  router.push(`/course/detail/${courseId}`)
}

// 继续学习
const continueLearning = (course: EnrolledCourse): void => {
  router.push(`/course/detail/${course.courseId}`)
}

// 前往课程列表
const goToCourseList = (): void => {
  router.push('/course/list')
}

// 排序后的课程列表
const sortedCourses = computed(() => {
  const courses = [...enrolledCourses.value]
  
  switch (sortOption.value) {
    case 'recent':
      return courses.sort((a, b) => {
        if (!a.lastLearningTime) return 1
        if (!b.lastLearningTime) return -1
        return new Date(b.lastLearningTime).getTime() - new Date(a.lastLearningTime).getTime()
      })
    case 'completion_desc':
      return courses.sort((a, b) => b.completionRate - a.completionRate)
    case 'completion_asc':
      return courses.sort((a, b) => a.completionRate - b.completionRate)
    case 'start_date':
      return courses.sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime())
    default:
      return courses
  }
})

// 生命周期钩子
onMounted(() => {
  fetchEnrolledCourses()
})
</script>

<style scoped>
.learning-progress {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.loading-container, .empty-container {
  padding: 40px 0;
  text-align: center;
}

.empty-action {
  margin-top: 20px;
}

.progress-list {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.course-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.course-card:hover {
  transform: translateY(-2px);
}

.course-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.course-info {
  flex: 1;
}

.course-name {
  margin: 0 0 12px 0;
  font-size: 18px;
}

.course-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 14px;
}

.progress-container {
  margin-bottom: 16px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
  color: #606266;
}

.course-stats {
  display: flex;
  gap: 32px;
  margin-top: 16px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.course-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: flex-end;
}

.statistics-cards {
  margin-top: 32px;
  margin-bottom: 24px;
}

.stat-card {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-card-content {
  text-align: center;
}

.stat-card-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-card-label {
  font-size: 14px;
  color: #606266;
}

.learning-chart {
  margin-top: 32px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.chart-header h3 {
  margin: 0;
  font-size: 18px;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}
</style> 