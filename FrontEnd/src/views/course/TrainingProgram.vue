<template>
  <div class="training-program">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>培训项目</span>
          <el-button type="primary" icon="Plus">发布项目</el-button>
        </div>
      </template>
      
      <div class="program-grid">
        <div v-for="program in programs" :key="program.programId" class="program-item">
          <el-card shadow="hover">
            <div class="program-content">
              <h3>{{ program.programName }}</h3>
              <p class="program-type">{{ getTypeText(program.programType) }}</p>
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
                <div>开始：{{ program.startTime }}</div>
                <div>结束：{{ program.endTime }}</div>
              </div>
              <div class="program-actions">
                <el-button type="primary" size="small">查看详情</el-button>
                <el-button type="success" size="small" :disabled="program.status !== 1">报名参加</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const programs = ref([
  {
    programId: 1,
    programName: 'Spring Boot实战培训',
    programType: 1,
    creditValue: 1.5,
    cost: 500.00,
    currentParticipants: 15,
    maxParticipants: 20,
    startTime: '2024-04-01 09:00',
    endTime: '2024-04-03 17:00',
    status: 1
  },
  {
    programId: 2,
    programName: 'AI技术应用研讨会',
    programType: 2,
    creditValue: 0.5,
    cost: 0.00,
    currentParticipants: 45,
    maxParticipants: 50,
    startTime: '2024-05-10 14:00',
    endTime: '2024-05-10 18:00',
    status: 1
  }
])

const getTypeText = (type: number) => {
  switch (type) {
    case 1: return '线上培训'
    case 2: return '线下培训'
    case 3: return '混合培训'
    default: return '未知'
  }
}
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

.program-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.program-content h3 {
  margin: 0 0 8px 0;
  color: #303133;
}

.program-type {
  color: #909399;
  margin: 0 0 16px 0;
  font-size: 14px;
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

.program-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 