<template>
  <div class="operation-log">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
        </div>
      </template>
      
      <el-table :data="logs" border stripe>
        <el-table-column prop="logId" label="日志ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="120" />
        <el-table-column prop="operationName" label="操作名称" min-width="150" />
        <el-table-column prop="requestIp" label="请求IP" width="130" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionTime" label="执行时间(ms)" width="120" />
        <el-table-column prop="createTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const logs = ref([
  {
    logId: 1,
    username: 'admin',
    operationType: '用户管理',
    operationName: '查询用户列表',
    requestIp: '192.168.1.100',
    status: 1,
    executionTime: 125,
    createTime: '2024-12-25 10:00:00'
  },
  {
    logId: 2,
    username: 'teacher1',
    operationType: '学分管理',
    operationName: '申请学分认证',
    requestIp: '192.168.1.101',
    status: 1,
    executionTime: 89,
    createTime: '2024-12-25 10:05:00'
  }
])

const viewDetail = (row: any) => {
  ElMessage.info(`查看日志详情: ${row.operationName}`)
}
</script>

<style scoped>
.operation-log {
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