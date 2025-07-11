<template>
  <div class="credit-application">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>学分认证申请</span>
          <el-button type="primary" @click="showDialog = true">新增申请</el-button>
        </div>
      </template>
      
      <el-table :data="applications" border stripe>
        <el-table-column prop="applicationId" label="申请ID" width="100" />
        <el-table-column prop="applicationType" label="申请类型" width="120" />
        <el-table-column prop="achievementName" label="成果名称" min-width="150" />
        <el-table-column prop="appliedCredits" label="申请学分" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewApplication(row)">查看</el-button>
            <el-button type="warning" link v-if="row.status === 1" @click="withdrawApplication(row)">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增申请对话框 -->
    <el-dialog v-model="showDialog" title="新增学分认证申请" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="申请类型">
          <el-select v-model="form.applicationType" placeholder="请选择申请类型">
            <el-option label="学历教育" value="学历教育" />
            <el-option label="职业培训" value="职业培训" />
            <el-option label="技能证书" value="技能证书" />
          </el-select>
        </el-form-item>
        <el-form-item label="成果名称">
          <el-input v-model="form.achievementName" placeholder="请输入成果名称" />
        </el-form-item>
        <el-form-item label="成果描述">
          <el-input v-model="form.achievementDescription" type="textarea" :rows="3" placeholder="请输入成果描述" />
        </el-form-item>
        <el-form-item label="申请学分">
          <el-input-number v-model="form.appliedCredits" :min="0.1" :step="0.5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="submitApplication">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const showDialog = ref(false)
const applications = ref([
  {
    applicationId: 1,
    applicationType: '学历教育',
    achievementName: 'Java高级编程证书',
    appliedCredits: 3.0,
    status: 2,
    applyTime: '2024-12-20 10:00:00'
  },
  {
    applicationId: 2,
    applicationType: '职业培训',
    achievementName: 'Spring Boot实战培训证书',
    appliedCredits: 1.5,
    status: 3,
    applyTime: '2024-12-15 14:30:00'
  }
])

const form = reactive({
  applicationType: '',
  achievementName: '',
  achievementDescription: '',
  appliedCredits: 1.0
})

const getStatusText = (status: number) => {
  switch (status) {
    case 1: return '待审核'
    case 2: return '审核中'
    case 3: return '通过'
    case 4: return '拒绝'
    case 5: return '撤回'
    default: return '未知'
  }
}

const getStatusColor = (status: number) => {
  switch (status) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'success'
    case 4: return 'danger'
    case 5: return ''
    default: return ''
  }
}

const viewApplication = (row: any) => {
  ElMessage.info(`查看申请: ${row.achievementName}`)
}

const withdrawApplication = (row: any) => {
  ElMessage.info(`撤回申请: ${row.achievementName}`)
}

const submitApplication = () => {
  ElMessage.success('申请提交成功')
  showDialog.value = false
}
</script>

<style scoped>
.credit-application {
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