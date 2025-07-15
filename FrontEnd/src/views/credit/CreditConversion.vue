<template>
  <div class="credit-conversion">
    <el-row :gutter="24">
      <!-- 转换功能卡片 -->
      <el-col :span="12">
        <el-card class="conversion-card">
          <template #header>
            <div class="card-header">
              <span>学分转换</span>
            </div>
          </template>
          
          <el-form :model="conversionForm" :rules="conversionRules" ref="conversionFormRef" label-width="100px">
            <el-form-item label="源学分类型" prop="sourceType">
              <el-select 
                v-model="conversionForm.sourceType" 
                placeholder="请选择源学分类型"
                @change="onSourceTypeChange"
                class="wide-select"
                popper-class="wide-dropdown"
              >
                <el-option label="学历教育" value="学历教育" />
                <el-option label="职业培训" value="职业培训" />
                <el-option label="技能证书" value="技能证书" />
                <el-option label="在线课程" value="在线课程" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="目标类型" prop="targetType">
              <el-select 
                v-model="conversionForm.targetType" 
                placeholder="请选择目标学分类型"
                @change="onTargetTypeChange"
                class="wide-select"
                popper-class="wide-dropdown"
              >
                <el-option label="学历教育" value="学历教育" />
                <el-option label="职业培训" value="职业培训" />
                <el-option label="技能证书" value="技能证书" />
                <el-option label="在线课程" value="在线课程" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="转换学分" prop="sourceCredits">
              <el-input-number 
                v-model="conversionForm.sourceCredits" 
                :min="0.1" 
                :step="0.1" 
                :precision="1"
                @change="calculateConversion"
              />
              <span style="margin-left: 8px; color: #999;">分</span>
            </el-form-item>
            
            <el-form-item label="转换规则" v-if="currentRule">
              <div class="rule-info">
                <p><strong>转换比例：</strong> 1 : {{ currentRule.conversionRate }}</p>
                <p v-if="currentRule.minCredits"><strong>最小转换：</strong> {{ currentRule.minCredits }} 分</p>
                <p v-if="currentRule.maxCredits"><strong>最大转换：</strong> {{ currentRule.maxCredits }} 分</p>
              </div>
            </el-form-item>
            
            <el-form-item label="预计获得" v-if="predictedCredits > 0">
              <div class="predicted-credits">
                <el-tag type="success" size="large">{{ predictedCredits }} 分</el-tag>
              </div>
            </el-form-item>
            
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleConversion" 
                :loading="converting"
                :disabled="!canConvert"
              >
                执行转换
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      
      <!-- 转换规则列表 -->
      <el-col :span="12">
        <el-card class="rules-card">
          <template #header>
            <div class="card-header">
              <span>转换规则</span>
              <el-button type="text" @click="loadRules" icon="Refresh">刷新</el-button>
            </div>
          </template>
          
          <div class="rules-list" v-loading="rulesLoading">
            <div v-if="rules.length === 0" class="no-rules">
              <el-empty description="暂无转换规则" />
            </div>
            <div v-else>
              <div 
                v-for="rule in rules" 
                :key="rule.ruleId" 
                class="rule-item"
                :class="{ 'active': currentRule && currentRule.ruleId === rule.ruleId }"
              >
                <div class="rule-header">
                  <span class="rule-title">{{ rule.sourceType }} → {{ rule.targetType }}</span>
                  <el-tag :type="rule.status === 1 ? 'success' : 'danger'" size="small">
                    {{ rule.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </div>
                <div class="rule-content">
                  <p><strong>转换比例：</strong> 1 : {{ rule.conversionRate }}</p>
                  <div class="rule-limits">
                    <span v-if="rule.minCredits">最小：{{ rule.minCredits }}分</span>
                    <span v-if="rule.maxCredits">最大：{{ rule.maxCredits }}分</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { creditApi, type CreditConversionRule } from '../../api/credit'

// 用户认证
const authStore = useAuthStore()

// 响应式数据
const converting = ref(false)
const rulesLoading = ref(false)
const rules = ref<CreditConversionRule[]>([])
const currentRule = ref<CreditConversionRule | null>(null)
const predictedCredits = ref(0)

const conversionFormRef = ref<FormInstance>()

// 转换表单
const conversionForm = reactive({
  sourceType: '',
  targetType: '',
  sourceCredits: 1.0
})

// 表单验证规则
const conversionRules: FormRules = {
  sourceType: [
    { required: true, message: '请选择源学分类型', trigger: 'change' }
  ],
  targetType: [
    { required: true, message: '请选择目标学分类型', trigger: 'change' }
  ],
  sourceCredits: [
    { required: true, message: '请输入转换学分', trigger: 'blur' },
    { type: 'number', min: 0.1, message: '转换学分必须大于0.1', trigger: 'blur' }
  ]
}

// 计算属性：是否可以转换
const canConvert = computed(() => {
  return conversionForm.sourceType && 
         conversionForm.targetType && 
         conversionForm.sourceCredits > 0 && 
         currentRule.value &&
         predictedCredits.value > 0
})

// 加载转换规则
const loadRules = async () => {
  rulesLoading.value = true
  try {
    const response = await creditApi.conversion.getRules()
    rules.value = response
  } catch (error) {
    ElMessage.error('获取转换规则失败')
  } finally {
    rulesLoading.value = false
  }
}

// 获取特定转换规则
const getRuleByTypes = async () => {
  if (!conversionForm.sourceType || !conversionForm.targetType) {
    currentRule.value = null
    predictedCredits.value = 0
    return
  }
  
  if (conversionForm.sourceType === conversionForm.targetType) {
    ElMessage.warning('源类型和目标类型不能相同')
    currentRule.value = null
    predictedCredits.value = 0
    return
  }

  try {
    const response = await creditApi.conversion.getRule(
      conversionForm.sourceType,
      conversionForm.targetType
    )
    currentRule.value = response
    calculateConversion()
  } catch (error) {
    currentRule.value = null
    predictedCredits.value = 0
    ElMessage.warning('未找到对应的转换规则')
  }
}

// 计算转换后的学分
const calculateConversion = async () => {
  if (!currentRule.value || !conversionForm.sourceCredits) {
    predictedCredits.value = 0
    return
  }

  try {
    const response = await creditApi.conversion.calculate({
      sourceType: conversionForm.sourceType,
      targetType: conversionForm.targetType,
      sourceCredits: conversionForm.sourceCredits
    })
    predictedCredits.value = response
  } catch (error: any) {
    predictedCredits.value = 0
    if (error.message) {
      ElMessage.warning(error.message)
    }
  }
}

// 源类型变化
const onSourceTypeChange = () => {
  getRuleByTypes()
}

// 目标类型变化
const onTargetTypeChange = () => {
  getRuleByTypes()
}

// 执行转换
const handleConversion = async () => {
  if (!conversionFormRef.value) return

  try {
    await conversionFormRef.value.validate()
    converting.value = true
    
    // 确保用户已登录
    if (!authStore.user?.userId) {
      ElMessage.error('请先登录')
      converting.value = false
      return
    }

    await creditApi.conversion.convert({
      userId: authStore.user.userId,
      sourceType: conversionForm.sourceType,
      targetType: conversionForm.targetType,
      sourceCredits: conversionForm.sourceCredits
    })

    ElMessage.success('转换成功')
    resetForm()
    loadRules()
  } catch (error) {
    ElMessage.error('转换失败')
  } finally {
    converting.value = false
  }
}

// 重置表单
const resetForm = () => {
  conversionFormRef.value?.resetFields()
  currentRule.value = null
  predictedCredits.value = 0
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
  
  loadRules()
})
</script>

<style scoped>
.credit-conversion {
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

.conversion-card, .rules-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 600px;
}

.rule-info {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.rule-info p {
  margin: 4px 0;
  color: #606266;
}

.predicted-credits {
  display: flex;
  align-items: center;
}

.rules-list {
  max-height: 500px;
  overflow-y: auto;
}

.no-rules {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.rule-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.rule-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.rule-item.active {
  border-color: #409eff;
  background: #f0f9ff;
}

.rule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.rule-title {
  font-weight: 600;
  color: #303133;
  font-size: 16px;
}

.rule-content p {
  margin: 4px 0;
  color: #606266;
}

.rule-limits {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #909399;
}

.rule-limits span {
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
}
</style> 