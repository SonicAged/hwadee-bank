<template>
  <div class="course-list">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>课程列表</span>
          <el-button type="primary" icon="Plus">新增课程</el-button>
        </div>
      </template>

      <!-- 表格 -->
      <el-table :data="courses" border stripe style="width: 100%">
        <el-table-column prop="courseId" label="课程ID" width="80" />
        <el-table-column prop="courseName" label="课程名称" min-width="200" />
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        <el-table-column prop="creditValue" label="学分值" width="80" />
        <el-table-column prop="creditHours" label="学时" width="80" />
        <el-table-column prop="currentStudents" label="当前学生数" width="100" />
        <el-table-column prop="maxStudents" label="最大学生数" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewCourse(row)">查看</el-button>
            <el-button type="success" link @click="enrollCourse(row)">报名</el-button>
            <el-button type="info" link @click="editCourse(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { courseApi, Course } from '@/api/course'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const { user } = storeToRefs(authStore)

const userId = user.value?.userId
const username = user.value?.username

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 课程列表数据
const courses = ref<Course[]>([])

// 搜索条件
const courseName = ref('')
const categoryId = ref<number | null>(null)

// 获取课程列表
const fetchCourseList = async () => {
  const result = await courseApi.getCourseList({
    page: currentPage.value,
    size: pageSize.value,
    courseName: courseName.value,
    categoryId: categoryId.value ?? undefined
  })

  courses.value = result.list
  total.value = result.total
}

// 页面大小变化时重新加载
const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchCourseList()
}

// 当前页码变化时重新加载
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchCourseList()
}

// 组件挂载完成后加载数据
onMounted(() => {
  fetchCourseList()
})

// 状态相关方法
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
    default: return ''
  }
}

// 操作方法保持不变
const viewCourse = (course: any) => {
  console.log('查看课程:', course)
}

const enrollCourse = (course: any) => {
  console.log('尝试报名课程:', course)

  if (course.status === 2) {
    alert('该课程已满员，无法报名')
    return
  }

  if (course.currentStudents >= course.maxStudents) {
    alert('人数已满，无法报名')
    return
  }

  courseApi.enrollCourse(course.courseId, userId)
  alert(`用户 ${username} 正在报名课程：${course.courseName}, uid: ${userId}`)
  course.currentStudents += 1
}

const editCourse = (course: any) => {
  console.log('编辑课程:', course)
}
</script>

<style scoped>
.course-list {
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

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 