<template>
  <div class="course-list">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>课程列表</span>
          <el-button type="primary" icon="Plus">新增课程</el-button>
        </div>
      </template>
      
      <el-table :data="courses" border stripe>
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
          <template #default>
            <el-button type="primary" link>查看</el-button>
            <el-button type="success" link>报名</el-button>
            <el-button type="info" link>编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const courses = ref([
  {
    courseId: 1,
    courseName: 'Java高级编程',
    courseCode: 'CS001',
    creditValue: 3.0,
    creditHours: 48,
    currentStudents: 25,
    maxStudents: 30,
    status: 1
  },
  {
    courseId: 2,
    courseName: '数据库系统原理',
    courseCode: 'CS002',
    creditValue: 2.5,
    creditHours: 40,
    currentStudents: 20,
    maxStudents: 25,
    status: 1
  }
])

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