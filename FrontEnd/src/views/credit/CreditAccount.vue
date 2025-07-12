<template>
  <div class="credit-account">
    <el-card class="box-card" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>学分账户</span>
          <div class="header-actions">
            <el-button type="primary" icon="Refresh" @click="loadAccountInfo">刷新</el-button>
            <el-button v-if="!account" type="success" icon="Plus" @click="createAccount">创建账户</el-button>
          </div>
        </div>
      </template>
      
      <div v-if="account" class="account-content">
        <div class="account-summary">
          <el-row :gutter="24">
            <el-col :span="8">
              <div class="stat-card total">
                <el-statistic 
                  title="总学分" 
                  :value="account.totalCredits" 
                  suffix="分" 
                  :precision="1"
                />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-card available">
                <el-statistic 
                  title="可用学分" 
                  :value="account.availableCredits" 
                  suffix="分" 
                  :precision="1"
                />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-card frozen">
                <el-statistic 
                  title="冻结学分" 
                  :value="account.frozenCredits" 
                  suffix="分" 
                  :precision="1"
                />
              </div>
            </el-col>
          </el-row>
        </div>
        
        <div class="account-details">
          <h3>账户详情</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="账户ID">{{ account.accountId }}</el-descriptions-item>
            <el-descriptions-item label="用户ID">{{ account.userId }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDateTime(account.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="最后更新">{{ formatDateTime(account.updateTime) }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      
      <div v-else class="no-account">
        <el-empty description="暂无学分账户">
          <el-button type="primary" @click="createAccount">创建学分账户</el-button>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { creditApi, type CreditAccount } from '../../api/credit'

// 用户认证
const authStore = useAuthStore()
const currentUser = authStore.user

// 响应式数据
const loading = ref(false)
const account = ref<CreditAccount | null>(null)

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 加载账户信息
const loadAccountInfo = async () => {
  if (!currentUser?.userId) {
    ElMessage.warning('请先登录')
    return
  }

  loading.value = true
  try {
    const result = await creditApi.account.getUserAccount(currentUser.userId)
    account.value = result
  } catch (error: any) {
    // 如果账户不存在，不显示错误信息
    if (error.message && error.message.includes('账户不存在')) {
      account.value = null
    } else {
      ElMessage.error('获取账户信息失败')
    }
  } finally {
    loading.value = false
  }
}

// 创建账户
const createAccount = async () => {
  if (!currentUser?.userId) {
    ElMessage.warning('请先登录')
    return
  }

  loading.value = true
  try {
    await creditApi.account.createAccount(currentUser.userId)
    ElMessage.success('学分账户创建成功')
    await loadAccountInfo()
  } catch (error) {
    ElMessage.error('创建账户失败')
  } finally {
    loading.value = false
  }
}

// 组件挂载时加载数据
onMounted(async () => {
  // 确保用户信息已加载
  if (!authStore.user && authStore.token) {
    try {
      await authStore.getUserInfo()
    } catch (error) {
      ElMessage.error('获取用户信息失败，请重新登录')
      return
    }
  }
  
  await loadAccountInfo()
})
</script>

<style scoped>
.credit-account {
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

.header-actions {
  display: flex;
  gap: 8px;
}

.account-content {
  min-height: 400px;
}

.account-summary {
  margin-bottom: 24px;
}

.stat-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-card.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-card.available {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stat-card.frozen {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-card.total :deep(.el-statistic__content),
.stat-card.available :deep(.el-statistic__content),
.stat-card.frozen :deep(.el-statistic__content) {
  color: white;
}

.stat-card.total :deep(.el-statistic__head),
.stat-card.available :deep(.el-statistic__head),
.stat-card.frozen :deep(.el-statistic__head) {
  color: rgba(255, 255, 255, 0.9);
}

.account-details h3 {
  margin-bottom: 16px;
  color: #303133;
  font-size: 18px;
}

.no-account {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.box-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;
}
</style> 