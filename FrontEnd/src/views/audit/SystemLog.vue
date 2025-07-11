<template>
  <div class="system-log">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>系统日志</span>
        </div>
      </template>
      
      <el-table :data="logs" border stripe>
        <el-table-column prop="logId" label="日志ID" width="80" />
        <el-table-column prop="logLevel" label="级别" width="80">
          <template #default="{ row }">
            <el-tag :type="getLevelColor(row.logLevel)">
              {{ row.logLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="loggerName" label="Logger" min-width="200" />
        <el-table-column prop="message" label="消息" min-width="300" />
        <el-table-column prop="serverName" label="服务器" width="120" />
        <el-table-column prop="createTime" label="记录时间" width="180" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
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
    logLevel: 'INFO',
    loggerName: 'org.hwadee.backend.controller.AuthController',
    message: '用户登录成功: admin',
    serverName: 'server-01',
    createTime: '2024-12-25 10:00:00'
  },
  {
    logId: 2,
    logLevel: 'WARN',
    loggerName: 'org.hwadee.backend.service.SysUserService',
    message: '用户登录失败，密码错误: test',
    serverName: 'server-01',
    createTime: '2024-12-25 10:02:00'
  },
  {
    logId: 3,
    logLevel: 'ERROR',
    loggerName: 'org.hwadee.backend.mapper.SysUserMapper',
    message: '数据库连接超时',
    serverName: 'server-01',
    createTime: '2024-12-25 10:03:00'
  }
])

const getLevelColor = (level: string) => {
  switch (level) {
    case 'DEBUG': return 'info'
    case 'INFO': return 'success'
    case 'WARN': return 'warning'
    case 'ERROR': return 'danger'
    default: return ''
  }
}

const viewDetail = (row: any) => {
  ElMessage.info(`查看系统日志详情: ${row.message}`)
}
</script>

<style scoped>
.system-log {
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