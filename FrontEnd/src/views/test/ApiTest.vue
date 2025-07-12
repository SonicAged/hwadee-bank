<template>
  <div class="api-test">
    <el-card>
      <template #header>
        <h3>API 功能测试</h3>
      </template>
      
      <div class="test-section">
        <h4>1. 获取用户信息测试</h4>
        <div class="test-controls">
          <el-input 
            v-model="userId" 
            placeholder="输入用户ID" 
            style="width: 200px; margin-right: 10px;"
          />
          <el-button type="primary" @click="testGetUser">获取用户信息</el-button>
        </div>
        <div v-if="userInfo" class="test-result">
          <h5>用户信息：</h5>
          <pre>{{ JSON.stringify(userInfo, null, 2) }}</pre>
        </div>
      </div>
      
      <div class="test-section">
        <h4>2. 更新个人资料测试</h4>
        <div class="test-controls">
          <el-input 
            v-model="profileUserId" 
            placeholder="输入用户ID" 
            style="width: 200px; margin-right: 10px;"
          />
          <el-button type="primary" @click="testUpdateProfile">更新个人资料</el-button>
        </div>
        <div v-if="profileResult" class="test-result">
          <h5>更新结果：</h5>
          <pre>{{ profileResult }}</pre>
        </div>
      </div>
      
      <div class="test-section">
        <h4>3. 修改密码测试</h4>
        <div class="test-controls">
          <el-input 
            v-model="passwordUserId" 
            placeholder="输入用户ID" 
            style="width: 150px; margin-right: 10px;"
          />
          <el-input 
            v-model="oldPassword" 
            type="password"
            placeholder="原密码" 
            style="width: 150px; margin-right: 10px;"
          />
          <el-input 
            v-model="newPassword" 
            type="password"
            placeholder="新密码" 
            style="width: 150px; margin-right: 10px;"
          />
          <el-button type="primary" @click="testChangePassword">修改密码</el-button>
        </div>
        <div v-if="passwordResult" class="test-result">
          <h5>修改结果：</h5>
          <pre>{{ passwordResult }}</pre>
        </div>
      </div>
      
      <div class="test-section">
        <h4>4. 头像上传测试</h4>
        <div class="test-controls">
          <el-upload
            :show-file-list="false"
            :before-upload="testUploadAvatar"
            accept="image/*"
            :disabled="uploading"
          >
            <el-button type="primary" :loading="uploading">
              {{ uploading ? '上传中...' : '选择头像文件' }}
            </el-button>
          </el-upload>
        </div>
        <div v-if="avatarResult" class="test-result">
          <h5>上传结果：</h5>
          <pre>{{ avatarResult }}</pre>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 响应式数据
const userId = ref('1')
const userInfo = ref(null)

const profileUserId = ref('1')
const profileResult = ref('')

const passwordUserId = ref('1')
const oldPassword = ref('')
const newPassword = ref('')
const passwordResult = ref('')

const uploading = ref(false)
const avatarResult = ref('')

// API 基础URL
const API_BASE = 'http://localhost:8080/api'

// 创建 axios 实例
const api = axios.create({
  baseURL: API_BASE,
  timeout: 5000,
})

// 获取用户信息测试
const testGetUser = async () => {
  try {
    const response = await api.get(`/test/user/${userId.value}`)
    userInfo.value = response.data
    ElMessage.success('获取用户信息成功')
  } catch (error: any) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败: ' + (error.response?.data?.message || error.message))
  }
}

// 更新个人资料测试
const testUpdateProfile = async () => {
  try {
    const response = await api.post(`/test/profile/${profileUserId.value}`)
    profileResult.value = JSON.stringify(response.data, null, 2)
    ElMessage.success('更新个人资料成功')
  } catch (error: any) {
    console.error('更新个人资料失败:', error)
    profileResult.value = JSON.stringify(error.response?.data || error.message, null, 2)
    ElMessage.error('更新个人资料失败: ' + (error.response?.data?.message || error.message))
  }
}

// 修改密码测试
const testChangePassword = async () => {
  try {
    const response = await api.post(`/test/password/${passwordUserId.value}`, null, {
      params: {
        oldPassword: oldPassword.value,
        newPassword: newPassword.value
      }
    })
    passwordResult.value = JSON.stringify(response.data, null, 2)
    ElMessage.success('修改密码成功')
  } catch (error: any) {
    console.error('修改密码失败:', error)
    passwordResult.value = JSON.stringify(error.response?.data || error.message, null, 2)
    ElMessage.error('修改密码失败: ' + (error.response?.data?.message || error.message))
  }
}

// 头像上传测试
const testUploadAvatar = async (file: File) => {
  try {
    uploading.value = true
    
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await api.post('/file/upload/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    avatarResult.value = JSON.stringify(response.data, null, 2)
    ElMessage.success('头像上传成功')
    
    return false
  } catch (error: any) {
    console.error('头像上传失败:', error)
    avatarResult.value = JSON.stringify(error.response?.data || error.message, null, 2)
    ElMessage.error('头像上传失败: ' + (error.response?.data?.message || error.message))
    return false
  } finally {
    uploading.value = false
  }
}
</script>

<style scoped>
.api-test {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.test-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.test-section h4 {
  margin-top: 0;
  color: #333;
}

.test-controls {
  margin-bottom: 15px;
}

.test-result {
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.test-result h5 {
  margin: 0 0 10px 0;
  color: #333;
}

.test-result pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-size: 12px;
  color: #666;
}
</style> 