<template>
  <div class="credit-records">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>学分记录</span>
        </div>
      </template>
      
      <el-table :data="records" border stripe>
        <el-table-column prop="recordId" label="记录ID" width="100" />
        <el-table-column prop="creditType" label="学分类型" width="120" />
        <el-table-column prop="creditSource" label="学分来源" min-width="150" />
        <el-table-column prop="creditAmount" label="学分数量" width="100" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeColor(row.operationType)">
              {{ getOperationTypeText(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const records = ref([
  {
    recordId: 1,
    creditType: '学历教育',
    creditSource: 'Java高级编程课程',
    creditAmount: 3.0,
    operationType: 1,
    status: 1,
    createTime: '2024-01-15 10:00:00'
  },
  {
    recordId: 2,
    creditType: '职业培训',
    creditSource: 'Spring Boot实战培训',
    creditAmount: 1.5,
    operationType: 1,
    status: 1,
    createTime: '2024-02-20 14:30:00'
  }
])

const getOperationTypeText = (type: number) => {
  switch (type) {
    case 1: return '获得'
    case 2: return '消费'
    case 3: return '转换'
    default: return '未知'
  }
}

const getOperationTypeColor = (type: number) => {
  switch (type) {
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'info'
    default: return ''
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '无效'
    case 1: return '有效'
    case 2: return '待审核'
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
.credit-records {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 