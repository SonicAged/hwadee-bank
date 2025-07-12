<template>
  <div class="user-profile">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人资料</span>
          <el-button type="primary" @click="handleEdit" :disabled="isEditing">
            {{ isEditing ? '编辑中...' : '编辑资料' }}
          </el-button>
        </div>
      </template>
      
      <div class="profile-content">
        <div class="avatar-section">
          <el-avatar :size="120" :src="getAvatarUrl(userInfo.avatar)" class="avatar">
            <span>{{ userInfo.realName?.charAt(0) || '用' }}</span>
          </el-avatar>
          <div class="avatar-actions" v-if="isEditing">
            <el-upload
              :show-file-list="false"
              :before-upload="handleAvatarUpload"
              accept="image/*"
              :disabled="uploading"
            >
              <el-button size="small" :loading="uploading">
                {{ uploading ? '上传中...' : '更换头像' }}
              </el-button>
            </el-upload>
          </div>
        </div>
        
        <div class="info-section">
          <el-form :model="formData" :rules="rules" ref="profileForm" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="userInfo.username" disabled />
            </el-form-item>
            
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="formData.realName" :disabled="!isEditing" />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" :disabled="!isEditing" />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" :disabled="!isEditing" />
            </el-form-item>
            
            <el-form-item label="性别" prop="gender">
              <el-select v-model="formData.gender" :disabled="!isEditing" placeholder="请选择性别">
                <el-option label="未知" :value="0" />
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="formData.birthDate"
                type="date"
                placeholder="请选择出生日期"
                :disabled="!isEditing"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            
            <el-form-item label="注册时间">
              <el-input :value="formatDate(userInfo.createTime)" disabled />
            </el-form-item>
            
            <el-form-item label="最后登录">
              <el-input :value="formatDate(userInfo.lastLoginTime)" disabled />
            </el-form-item>
            
            <el-form-item v-if="isEditing">
              <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
              <el-button @click="handleCancel">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../../stores/auth'
import { userApi, type UserInfo, type UpdateProfileDTO } from '../../api/user'

const authStore = useAuthStore()

// 响应式数据
const isEditing = ref(false)
const saving = ref(false)
const uploading = ref(false)
const userInfo = ref<UserInfo>({
  userId: 0,
  username: '',
  realName: '',
  email: '',
  phone: '',
  avatar: '',
  gender: 0,
  birthDate: '',
  createTime: '',
  lastLoginTime: '',
  status: 1
})

const formData = reactive<UpdateProfileDTO>({
  realName: '',
  email: '',
  phone: '',
  avatar: '',
  gender: 0,
  birthDate: ''
})

// 表单验证规则
const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2到20个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ]
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await userApi.getCurrentUser()
    userInfo.value = response
    
    // 更新表单数据
    formData.realName = userInfo.value.realName || ''
    formData.email = userInfo.value.email || ''
    formData.phone = userInfo.value.phone || ''
    formData.avatar = userInfo.value.avatar || ''
    formData.gender = userInfo.value.gender || 0
    formData.birthDate = userInfo.value.birthDate || ''
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return '未知'
  try {
    return new Date(dateString).toLocaleString('zh-CN')
  } catch {
    return dateString
  }
}

// 获取头像完整URL
const getAvatarUrl = (avatar: string) => {
  if (!avatar) return ''
  
  // 如果已经是完整URL，直接返回
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  
  // 如果是相对路径，构造完整的头像URL
  return `http://localhost:8080${avatar}`
}

// 编辑资料
const handleEdit = () => {
  isEditing.value = true
}

// 保存资料
const handleSave = async () => {
  // const profileFormRef = document.querySelector('.el-form')
  
  try {
    // 简单验证
    if (!formData.realName || !formData.email || !formData.phone) {
      ElMessage.error('请填写完整的个人信息')
      return
    }
    
    saving.value = true
    
    console.log('提交的个人资料数据:', formData)
    
    await userApi.updateProfile(formData)
    
    ElMessage.success('个人资料更新成功')
    isEditing.value = false
    
    // 重新获取用户信息
    await getUserInfo()
    
    // 更新store中的用户信息
    await authStore.getUserInfo()
    
  } catch (error) {
    console.error('更新个人资料失败:', error)
    ElMessage.error('更新个人资料失败: ' + ((error as any).response?.data?.message || (error as any).message))
  } finally {
    saving.value = false
  }
}

// 取消编辑
const handleCancel = () => {
  isEditing.value = false
  
  // 重置表单数据
  formData.realName = userInfo.value.realName || ''
  formData.email = userInfo.value.email || ''
  formData.phone = userInfo.value.phone || ''
  formData.avatar = userInfo.value.avatar || ''
  formData.gender = userInfo.value.gender || 0
  formData.birthDate = userInfo.value.birthDate || ''
}

// 上传头像
const handleAvatarUpload = async (file: File) => {
  try {
    // 文件大小检查（5MB）
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.error('文件大小不能超过5MB')
      return false
    }

    // 文件类型检查
    if (!file.type.startsWith('image/')) {
      ElMessage.error('只支持图片文件')
      return false
    }

    uploading.value = true
    
    // 上传头像
    const avatarUrl = await userApi.uploadAvatar(file)
    
    // 更新头像URL
    formData.avatar = avatarUrl
    userInfo.value.avatar = avatarUrl
    
    ElMessage.success('头像上传成功')
    
    return false // 阻止默认上传行为
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败')
    return false
  } finally {
    uploading.value = false
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.user-profile {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.profile-content {
  display: flex;
  gap: 40px;
}

.avatar-section {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.avatar {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.info-section {
  flex: 1;
}

.info-section .el-form .el-form-item {
  margin-bottom: 24px;
}

@media (max-width: 768px) {
  .profile-content {
    flex-direction: column;
    gap: 20px;
  }
}
</style> 