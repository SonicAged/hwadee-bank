<template>
  <div class="credit-account">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>学分账户</span>
        </div>
      </template>
      
      <div class="account-summary">
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="stat-card">
              <el-statistic title="总学分" :value="120.5" suffix="分" />
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <el-statistic title="可用学分" :value="98.0" suffix="分" />
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card">
              <el-statistic title="冻结学分" :value="22.5" suffix="分" />
            </div>
          </el-col>
        </el-row>
      </div>
      
      <div class="account-details">
        <h3>账户详情</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="账户ID">ACC001</el-descriptions-item>
          <el-descriptions-item label="创建时间">2024-01-01</el-descriptions-item>
          <el-descriptions-item label="最后更新">2024-12-25</el-descriptions-item>
          <el-descriptions-item label="账户状态">正常</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
// 这里会添加具体的业务逻辑
</script>

<style scoped>
.credit-account {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.account-summary {
  margin-bottom: 24px;
}

.stat-card {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  text-align: center;
}

.account-details h3 {
  margin-bottom: 16px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 