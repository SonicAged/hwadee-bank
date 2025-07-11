<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const user = computed(() => authStore.user)

// 统计数据
const totalCredits = ref(0)
const availableCredits = ref(0)
const monthlyCredits = ref(0)
const totalCourses = ref(0)

// 模拟数据加载
onMounted(() => {
  // 这里应该从API获取实际数据
  totalCredits.value = 120.5
  availableCredits.value = 98.0
  monthlyCredits.value = 15.5
  totalCourses.value = 8
})
</script>

<template>
  <div class="home-container">
    <div class="welcome-card">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>欢迎来到终身学习学分银行平台</span>
          </div>
        </template>
        <div class="welcome-content">
          <h2>您好，{{ user?.realName || '用户' }}！</h2>
          <p>欢迎使用终身学习学分银行平台管理系统</p>
          
          <div class="stats-grid">
            <div class="stat-item">
              <el-statistic title="总学分" :value="totalCredits" />
            </div>
            <div class="stat-item">
              <el-statistic title="可用学分" :value="availableCredits" />
            </div>
            <div class="stat-item">
              <el-statistic title="本月新增" :value="monthlyCredits" />
            </div>
            <div class="stat-item">
              <el-statistic title="学习课程" :value="totalCourses" />
            </div>
          </div>
        </div>
      </el-card>
    </div>
    
    <div class="quick-actions">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>快捷操作</span>
          </div>
        </template>
        <div class="actions-grid">
          <el-button type="primary" icon="DocumentAdd" size="large" @click="router.push('/dashboard/credit/application')">
            申请学分认证
          </el-button>
          <el-button type="success" icon="Reading" size="large" @click="router.push('/dashboard/courses/list')">
            浏览课程
          </el-button>
          <el-button type="info" icon="Files" size="large" @click="router.push('/dashboard/resources/library')">
            学习资源
          </el-button>
          <el-button type="warning" icon="Document" size="large" @click="router.push('/dashboard/credit/records')">
            学分记录
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

.welcome-content h2 {
  color: #303133;
  margin-bottom: 8px;
}

.welcome-content p {
  color: #606266;
  margin-bottom: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
  margin-top: 24px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
