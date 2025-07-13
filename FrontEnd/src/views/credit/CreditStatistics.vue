<template>
  <div class="credit-statistics">
    <el-row :gutter="24">
      <!-- 概览卡片 -->
      <el-col :span="24">
        <el-card class="overview-card">
          <template #header>
            <div class="card-header">
              <span>学分统计概览</span>
              <el-button type="primary" icon="Refresh" @click="loadStatistics">刷新</el-button>
            </div>
          </template>
          
          <el-row :gutter="20" v-loading="loading">
            <el-col :span="6">
              <div class="stat-item total">
                <div class="stat-value">{{ overview.totalCredits || 0 }}</div>
                <div class="stat-label">总学分</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item available">
                <div class="stat-value">{{ overview.availableCredits || 0 }}</div>
                <div class="stat-label">可用学分</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item frozen">
                <div class="stat-value">{{ overview.frozenCredits || 0 }}</div>
                <div class="stat-label">冻结学分</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item records">
                <div class="stat-value">{{ overview.totalRecords || 0 }}</div>
                <div class="stat-label">学分记录</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" style="margin-top: 20px;">
      <!-- 申请统计 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>申请统计</span>
          </template>
          
          <div class="application-stats">
            <div class="stat-row">
              <span class="label">总申请数：</span>
              <span class="value">{{ overview.totalApplications || 0 }}</span>
            </div>
            <div class="stat-row">
              <span class="label">待审核：</span>
              <span class="value pending">{{ overview.pendingApplications || 0 }}</span>
            </div>
            <div class="stat-row">
              <span class="label">已通过：</span>
              <span class="value approved">{{ overview.approvedApplications || 0 }}</span>
            </div>
            <div class="stat-row">
              <span class="label">已拒绝：</span>
              <span class="value rejected">{{ overview.rejectedApplications || 0 }}</span>
            </div>
          </div>

          <div class="progress-section">
            <div class="progress-item">
              <span>通过率</span>
              <el-progress 
                :percentage="getApprovalRate()" 
                color="#67c23a"
                :show-text="true"
              />
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 操作趋势 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>操作统计</span>
          </template>
          
          <div class="trend-stats">
            <div class="trend-item gain">
              <div class="trend-icon">📈</div>
              <div class="trend-content">
                <div class="trend-value">{{ trend.gainRecords || 0 }}</div>
                <div class="trend-label">获得记录</div>
              </div>
            </div>
            <div class="trend-item consume">
              <div class="trend-icon">📉</div>
              <div class="trend-content">
                <div class="trend-value">{{ trend.consumeRecords || 0 }}</div>
                <div class="trend-label">消费记录</div>
              </div>
            </div>
            <div class="trend-item convert">
              <div class="trend-icon">🔄</div>
              <div class="trend-content">
                <div class="trend-value">{{ trend.convertRecords || 0 }}</div>
                <div class="trend-label">转换记录</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" style="margin-top: 20px;">
      <!-- 学分类型分布 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>学分类型分布</span>
          </template>
          
          <div class="distribution-chart">
            <div v-for="(value, type) in distribution.typeDistribution" :key="type" class="distribution-item">
              <div class="distribution-header">
                <span class="type-name">{{ type }}</span>
                <span class="type-value">{{ value }}%</span>
              </div>
              <el-progress 
                :percentage="value" 
                :color="getTypeColor(type)"
                :show-text="false"
              />
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 学分账户状态 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>账户状态</span>
          </template>
          
          <div class="account-status">
            <div class="status-chart">
              <div class="chart-item">
                <div class="chart-ring">
                  <div class="ring-progress" :style="{ '--progress': getUsageRate() + '%' }">
                    <div class="ring-content">
                      <div class="usage-rate">{{ getUsageRate() }}%</div>
                      <div class="usage-label">使用率</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="status-details">
              <div class="detail-item">
                <span class="detail-label">可用学分占比：</span>
                <span class="detail-value">{{ getAvailableRate() }}%</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">冻结学分占比：</span>
                <span class="detail-value">{{ getFrozenRate() }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { creditApi, type CreditStatisticsOverview, type CreditTrendStatistics, type CreditDistributionStatistics } from '../../api/credit'

// 用户认证
const authStore = useAuthStore()

// 响应式数据
const loading = ref(false)
const overview = reactive<CreditStatisticsOverview>({
  totalCredits: 0,
  availableCredits: 0,
  frozenCredits: 0,
  totalRecords: 0,
  totalApplications: 0,
  pendingApplications: 0,
  approvedApplications: 0,
  rejectedApplications: 0
})

const trend = reactive<CreditTrendStatistics>({
  gainRecords: 0,
  consumeRecords: 0,
  convertRecords: 0
})

const distribution = reactive<CreditDistributionStatistics>({
  typeDistribution: {}
})

// 加载统计数据
const loadStatistics = async () => {
  loading.value = true
  try {
    // 获取当前用户ID
    const userId = authStore.user?.userId

    // 并行加载各种统计数据，传递用户ID
    const [overviewRes, trendRes, distributionRes] = await Promise.all([
      creditApi.statistics.getOverview(userId),
      creditApi.statistics.getTrend(userId),
      creditApi.statistics.getDistribution(userId)
    ])

    Object.assign(overview, overviewRes)
    Object.assign(trend, trendRes)
    Object.assign(distribution, distributionRes)
  } catch (error) {
    ElMessage.error('加载统计数据失败')
  } finally {
    loading.value = false
  }
}

// 计算通过率
const getApprovalRate = () => {
  const total = overview.totalApplications
  if (total === 0) return 0
  return Math.round((overview.approvedApplications / total) * 100)
}

// 计算使用率
const getUsageRate = () => {
  const total = Number(overview.totalCredits)
  const available = Number(overview.availableCredits)
  if (total === 0) return 0
  return Math.round(((total - available) / total) * 100)
}

// 计算可用学分占比
const getAvailableRate = () => {
  const total = Number(overview.totalCredits)
  const available = Number(overview.availableCredits)
  if (total === 0) return 100
  return Math.round((available / total) * 100)
}

// 计算冻结学分占比
const getFrozenRate = () => {
  const total = Number(overview.totalCredits)
  const frozen = Number(overview.frozenCredits)
  if (total === 0) return 0
  return Math.round((frozen / total) * 100)
}

// 获取类型颜色
const getTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    '学历教育': '#409eff',
    '职业培训': '#67c23a',
    '技能证书': '#e6a23c',
    '其他': '#f56c6c'
  }
  return colors[type] || '#909399'
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
  
  loadStatistics()
})
</script>

<style scoped>
.credit-statistics {
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

.overview-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stat-item {
  text-align: center;
  padding: 20px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
}

.stat-item.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-item.available {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-item.frozen {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-item.records {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.chart-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 300px;
}

.application-stats {
  margin-bottom: 20px;
}

.stat-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.stat-row:last-child {
  border-bottom: none;
}

.label {
  font-weight: 500;
  color: #606266;
}

.value {
  font-weight: bold;
  font-size: 16px;
}

.value.pending {
  color: #e6a23c;
}

.value.approved {
  color: #67c23a;
}

.value.rejected {
  color: #f56c6c;
}

.progress-section {
  margin-top: 20px;
}

.progress-item {
  margin-bottom: 12px;
}

.progress-item span {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
}

.trend-stats {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 200px;
}

.trend-item {
  text-align: center;
  padding: 20px;
  border-radius: 12px;
  background: #f8f9fa;
  transition: all 0.3s ease;
  flex: 1;
  margin: 0 8px;
}

.trend-item:hover {
  background: #e9ecef;
  transform: translateY(-2px);
}

.trend-icon {
  font-size: 24px;
  margin-bottom: 12px;
}

.trend-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 4px;
}

.trend-label {
  font-size: 14px;
  color: #606266;
}

.distribution-chart {
  padding: 20px 0;
}

.distribution-item {
  margin-bottom: 20px;
}

.distribution-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.type-name {
  font-weight: 500;
  color: #303133;
}

.type-value {
  font-weight: bold;
  color: #409eff;
}

.account-status {
  display: flex;
  align-items: center;
  height: 200px;
}

.status-chart {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chart-ring {
  position: relative;
  width: 120px;
  height: 120px;
}

.ring-progress {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: conic-gradient(#409eff 0deg, #409eff calc(var(--progress) * 3.6deg), #e4e7ed calc(var(--progress) * 3.6deg), #e4e7ed 360deg);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.ring-progress::before {
  content: '';
  position: absolute;
  width: 80px;
  height: 80px;
  background: white;
  border-radius: 50%;
}

.ring-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.usage-rate {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.usage-label {
  font-size: 12px;
  color: #606266;
}

.status-details {
  flex: 1;
  padding-left: 30px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 8px 0;
}

.detail-label {
  color: #606266;
  font-size: 14px;
}

.detail-value {
  font-weight: bold;
  color: #409eff;
}
</style> 