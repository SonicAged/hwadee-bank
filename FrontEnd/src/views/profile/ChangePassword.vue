<template>
  <div class="change-password">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>
      
      <div class="password-content">
        <el-form :model="formData" :rules="rules" ref="passwordForm" label-width="120px">
          <el-form-item label="原密码" prop="oldPassword">
            <el-input 
              v-model="formData.oldPassword" 
              type="password" 
              placeholder="请输入原密码"
              show-password
              clearable
            />
          </el-form-item>
          
          <el-form-item label="新密码" prop="newPassword">
            <el-input 
              v-model="formData.newPassword" 
              type="password" 
              placeholder="请输入新密码"
              show-password
              clearable
            />
            <div class="password-tips">
              <p>密码要求：</p>
              <ul>
                <li>包含至少一个数字，一个字母，建议包含特殊字符，长度至少8个字符</li>
              </ul>
            </div>
          </el-form-item>
          
          <el-form-item label="确认新密码" prop="confirmPassword">
            <el-input 
              v-model="formData.confirmPassword" 
              type="password" 
              placeholder="请再次输入新密码"
              show-password
              clearable
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSubmit" :loading="loading">
              {{ loading ? '修改中...' : '确认修改' }}
            </el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button @click="handleCancel">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { userApi, type ChangePasswordDTO } from '../../api/user'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const passwordForm = ref()

const formData = reactive<ChangePasswordDTO>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 自定义验证规则
const validatePassword = (_rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入新密码'))
    return
  }
  
  if (value.length < 8) {
    callback(new Error('密码长度至少8个字符'))
    return
  }
  
  if (!/\d/.test(value)) {
    callback(new Error('密码必须包含至少一个数字'))
    return
  }
  
  if (!/[a-zA-Z]/.test(value)) {
    callback(new Error('密码必须包含至少一个字母'))
    return
  }
  
  callback()
}

const validateConfirmPassword = (_rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请确认新密码'))
    return
  }
  
  if (value !== formData.newPassword) {
    callback(new Error('两次输入的密码不一致'))
    return
  }
  
  callback()
}

// 表单验证规则
const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 提交表单
const handleSubmit = async () => {
  try {
    await passwordForm.value.validate()
    
    loading.value = true
    
    await userApi.changePassword(formData)
    
    ElMessage.success('密码修改成功')
    
    // 提示用户重新登录
    await ElMessageBox.confirm(
      '密码修改成功，为了安全起见，请重新登录',
      '提示',
      {
        confirmButtonText: '重新登录',
        cancelButtonText: '稍后登录',
        type: 'success'
      }
    )
    
    // 清除token并跳转到登录页
    localStorage.removeItem('token')
    router.push('/login')
    
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error('修改密码失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const handleReset = () => {
  passwordForm.value.resetFields()
  formData.oldPassword = ''
  formData.newPassword = ''
  formData.confirmPassword = ''
}

// 取消修改
const handleCancel = () => {
  router.go(-1)
}
</script>

<style scoped>
.change-password {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.password-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.password-content .el-form .el-form-item {
  margin-bottom: 24px;
}

.password-tips {
  margin-top: 8px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
  color: #666;
  font-size: 14px;
}

.password-tips p {
  margin: 0 0 8px 0;
  font-weight: 500;
}

.password-tips ul {
  margin: 0;
  padding-left: 20px;
}

.password-tips ul li {
  margin-bottom: 4px;
}

@media (max-width: 768px) {
  .change-password {
    padding: 10px;
  }
  
  .password-card :deep(.el-card__body) {
    padding: 16px;
  }
}
</style> 