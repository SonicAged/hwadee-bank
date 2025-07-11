<template>
  <div class="resource-library">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>学习资源库</span>
          <el-button type="primary" icon="Plus">上传资源</el-button>
        </div>
      </template>
      
      <div class="resource-grid">
        <div v-for="resource in resources" :key="resource.resourceId" class="resource-item">
          <el-card :body-style="{ padding: '16px' }" shadow="hover">
            <div class="resource-content">
              <div class="resource-icon">
                <el-icon size="40"><Document /></el-icon>
              </div>
              <h4>{{ resource.resourceName }}</h4>
              <p class="resource-type">{{ resource.resourceType }}</p>
              <div class="resource-meta">
                <span>学分: {{ resource.creditValue }}</span>
                <span>难度: {{ getDifficultyText(resource.difficultyLevel) }}</span>
              </div>
              <div class="resource-stats">
                <span><el-icon><View /></el-icon> {{ resource.viewCount }}</span>
                <span><el-icon><Download /></el-icon> {{ resource.downloadCount }}</span>
              </div>
              <div class="resource-actions">
                <el-button type="primary" size="small">查看详情</el-button>
                <el-button type="success" size="small">开始学习</el-button>
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

const resources = ref([
  {
    resourceId: 1,
    resourceName: 'Java基础编程',
    resourceType: '课程',
    creditValue: 3.0,
    difficultyLevel: 1,
    viewCount: 1250,
    downloadCount: 340
  },
  {
    resourceId: 2,
    resourceName: 'MySQL数据库设计',
    resourceType: '课程',
    creditValue: 2.5,
    difficultyLevel: 2,
    viewCount: 980,
    downloadCount: 280
  },
  {
    resourceId: 3,
    resourceName: 'Python编程入门',
    resourceType: '教材',
    creditValue: 2.0,
    difficultyLevel: 1,
    viewCount: 2100,
    downloadCount: 560
  }
])

const getDifficultyText = (level: number) => {
  switch (level) {
    case 1: return '初级'
    case 2: return '中级'
    case 3: return '高级'
    default: return '未知'
  }
}
</script>

<style scoped>
.resource-library {
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

.resource-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.resource-item {
  transition: transform 0.3s;
}

.resource-item:hover {
  transform: translateY(-2px);
}

.resource-content {
  text-align: center;
}

.resource-icon {
  margin-bottom: 12px;
  color: #409eff;
}

.resource-content h4 {
  margin: 0 0 8px 0;
  color: #303133;
}

.resource-type {
  color: #909399;
  margin: 0 0 12px 0;
  font-size: 14px;
}

.resource-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  color: #606266;
}

.resource-stats {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #909399;
}

.resource-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.resource-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 