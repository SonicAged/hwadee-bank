<template>
  <div class="course-detail">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
    <template v-else>
      <el-page-header @back="goBack" :title="course.courseName">
        <template #content>
          <span class="course-code">{{ course.courseCode }}</span>
        </template>
        <template #extra>
          <el-button 
            type="primary" 
            size="small"
            :disabled="course.status !== 1" 
            @click="enrollCourse"
          >
            {{ course.status === 2 ? '已满员' : '报名课程' }}
          </el-button>
        </template>
      </el-page-header>

      <el-tabs v-model="activeTab" class="course-tabs">
        <el-tab-pane label="课程信息" name="info">
          <el-card class="info-card">
            <div class="course-header">
              <div class="course-title">
                <h1>{{ course.courseName }}</h1>
                <el-tag :type="getStatusColor(course.status)">
                  {{ getStatusText(course.status) }}
                </el-tag>
              </div>
              <div class="course-meta">
                <div class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ course.startDate }} - {{ course.endDate }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><User /></el-icon>
                  <span>学生人数: {{ course.currentStudents }}/{{ course.maxStudents }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Medal /></el-icon>
                  <span>学分值: {{ course.creditValue }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Clock /></el-icon>
                  <span>学时: {{ course.creditHours }}</span>
                </div>
              </div>
            </div>
            
            <el-divider />
            
            <div class="course-description">
              <h3>课程描述</h3>
              <p>{{ course.description || '暂无描述' }}</p>
            </div>
            
            <div class="course-syllabus">
              <h3>课程大纲</h3>
              <div v-if="course.syllabus" v-html="formattedSyllabus"></div>
              <el-empty v-else description="暂无课程大纲" />
            </div>
          </el-card>
        </el-tab-pane>
        
        <el-tab-pane label="课程资源" name="resources">
          <course-resources :courseId="courseId" />
        </el-tab-pane>
        
        <el-tab-pane label="在线学习" name="learning">
          <course-learning :courseId="courseId" />
        </el-tab-pane>
        
        <el-tab-pane label="测试与作业" name="tests">
          <course-tests :courseId="courseId" />
        </el-tab-pane>
      </el-tabs>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Calendar, User, Medal, Clock } from '@element-plus/icons-vue'
import CourseResources from '../../components/course/CourseResources.vue'
import CourseLearning from '../../components/course/CourseLearning.vue'
import CourseTests from '../../components/course/CourseTests.vue'
import { courseApi } from '../../api/course'
import type { Course } from '../../types/course'

const route = useRoute()
const router = useRouter()
const courseId = ref(Number(route.params.id))
const course = ref<Course>({
  courseId: 0,
  courseName: '',
  courseCode: '',
  categoryId: 0,
  instructorId: 0,
  description: '',
  syllabus: '',
  creditHours: 0,
  creditValue: 0,
  maxStudents: 0,
  currentStudents: 0,
  startDate: '',
  endDate: '',
  status: 0
})
const loading = ref(true)
const activeTab = ref('info')

onMounted(async () => {
  try {
    if (courseId.value) {
      const response = await courseApi.getCourseById(courseId.value)
      course.value = response
    }
  } catch (error) {
    ElMessage.error('获取课程信息失败')
    console.error(error)
  } finally {
    loading.value = false
  }
})

const formattedSyllabus = computed(() => {
  if (!course.value.syllabus) return ''
  // 将换行符转换为HTML换行
  return course.value.syllabus.replace(/\n/g, '<br>')
})

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '关闭'
    case 1: return '开放'
    case 2: return '满员'
    default: return '未知'
  }
}

const getStatusColor = (status: number) => {
  switch (status) {
    case 0: return 'danger'
    case 1: return 'success'
    case 2: return 'warning'
    default: ''
  }
}

const goBack = () => {
  router.push('/course/list')
}

const enrollCourse = async () => {
  try {
    await courseApi.enrollCourse(courseId.value)
    ElMessage.success('报名成功')
    // 重新获取课程信息
    const response = await courseApi.getCourseById(courseId.value)
    course.value = response
  } catch (error) {
    ElMessage.error('报名失败')
    console.error(error)
  }
}
</script>

<style scoped>
.course-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading-container {
  padding: 20px;
}

.course-code {
  color: #909399;
  font-size: 14px;
  margin-left: 8px;
}

.course-tabs {
  margin-top: 20px;
}

.info-card {
  margin-top: 20px;
}

.course-header {
  margin-bottom: 20px;
}

.course-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.course-title h1 {
  margin: 0;
  font-size: 24px;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 16px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.course-description, .course-syllabus {
  margin-top: 24px;
}

.course-description h3, .course-syllabus h3 {
  font-size: 18px;
  margin-bottom: 12px;
  color: #303133;
}

.course-description p {
  line-height: 1.6;
  color: #606266;
}
</style> 