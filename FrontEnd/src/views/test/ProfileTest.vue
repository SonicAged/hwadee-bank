<template>
  <div class="profile-test">
    <h2>个人资料和头像上传测试</h2>
    
    <!-- 个人资料测试区域 -->
    <div class="test-section">
      <h3>1. 个人资料更新测试</h3>
      <div class="form-section">
        <div class="form-item">
          <label>真实姓名:</label>
          <input v-model="profileData.realName" type="text" placeholder="请输入真实姓名" />
        </div>
        <div class="form-item">
          <label>邮箱:</label>
          <input v-model="profileData.email" type="email" placeholder="请输入邮箱" />
        </div>
        <div class="form-item">
          <label>手机号:</label>
          <input v-model="profileData.phone" type="tel" placeholder="请输入手机号" />
        </div>
        <div class="form-item">
          <label>性别:</label>
          <select v-model="profileData.gender">
            <option value="0">未知</option>
            <option value="1">男</option>
            <option value="2">女</option>
          </select>
        </div>
        <div class="form-item">
          <label>出生日期:</label>
          <input v-model="profileData.birthDate" type="date" />
        </div>
        <div class="form-item">
          <label>头像URL:</label>
          <input v-model="profileData.avatar" type="text" placeholder="头像URL" readonly />
        </div>
        <button @click="updateProfile" :disabled="updating">
          {{ updating ? '更新中...' : '更新个人资料' }}
        </button>
      </div>
      <div v-if="profileResult" class="result">
        <h4>更新结果:</h4>
        <pre>{{ profileResult }}</pre>
      </div>
    </div>

    <!-- 头像上传测试区域 -->
    <div class="test-section">
      <h3>2. 头像上传测试</h3>
      <div class="upload-section">
        <input 
          type="file" 
          @change="handleFileSelect" 
          accept="image/*"
          ref="fileInput"
        />
        <button @click="uploadAvatar" :disabled="!selectedFile || uploading">
          {{ uploading ? '上传中...' : '上传头像' }}
        </button>
        <div v-if="selectedFile" class="file-info">
          <p>选中文件: {{ selectedFile.name }}</p>
          <p>文件大小: {{ (selectedFile.size / 1024 / 1024).toFixed(2) }}MB</p>
        </div>
      </div>
      <div v-if="uploadResult" class="result">
        <h4>上传结果:</h4>
        <pre>{{ uploadResult }}</pre>
      </div>
    </div>

    <!-- 当前用户信息 -->
    <div class="test-section">
      <h3>3. 当前用户信息</h3>
      <button @click="getCurrentUser">获取当前用户信息</button>
      <div v-if="currentUser" class="result">
        <h4>用户信息:</h4>
        <pre>{{ JSON.stringify(currentUser, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

// 响应式数据
const updating = ref(false)
const uploading = ref(false)
const selectedFile = ref<File | null>(null)
const fileInput = ref<HTMLInputElement>()

const profileData = reactive({
  realName: '测试用户',
  email: 'test@example.com',
  phone: '13800138000',
  avatar: '',
  gender: 1,
  birthDate: '1990-01-01'
})

const profileResult = ref('')
const uploadResult = ref('')
const currentUser = ref(null)

// API基础URL
const API_BASE = 'http://localhost:8080/api'

// 获取token（简单实现）
const getToken = () => {
  return localStorage.getItem('token') || 'test-token'
}

// 文件选择处理
const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0]
    console.log('选中文件:', selectedFile.value)
  }
}

// 更新个人资料
const updateProfile = async () => {
  try {
    updating.value = true
    profileResult.value = ''

    console.log('开始更新个人资料:', profileData)

    const response = await fetch(`${API_BASE}/user/profile`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${getToken()}`
      },
      body: JSON.stringify(profileData)
    })

    const result = await response.json()
    profileResult.value = JSON.stringify(result, null, 2)

    if (response.ok) {
      alert('个人资料更新成功!')
    } else {
      alert('个人资料更新失败: ' + result.message)
    }
  } catch (error) {
    console.error('更新个人资料失败:', error)
    profileResult.value = JSON.stringify(error, null, 2)
    alert('更新个人资料失败: ' + error)
  } finally {
    updating.value = false
  }
}

// 上传头像
const uploadAvatar = async () => {
  if (!selectedFile.value) {
    alert('请先选择文件')
    return
  }

  try {
    uploading.value = true
    uploadResult.value = ''

    console.log('开始上传头像:', selectedFile.value)

    const formData = new FormData()
    formData.append('file', selectedFile.value)

    const response = await fetch(`${API_BASE}/simple-file/upload-avatar`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${getToken()}`
      },
      body: formData
    })

    const result = await response.json()
    uploadResult.value = JSON.stringify(result, null, 2)

    if (response.ok) {
      profileData.avatar = result.data || result.url || result
      alert('头像上传成功!')
    } else {
      alert('头像上传失败: ' + result.message)
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    uploadResult.value = JSON.stringify(error, null, 2)
    alert('头像上传失败: ' + error)
  } finally {
    uploading.value = false
  }
}

// 获取当前用户信息
const getCurrentUser = async () => {
  try {
    const response = await fetch(`${API_BASE}/auth/userinfo`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    })

    const result = await response.json()
    currentUser.value = result

    if (response.ok && result.data) {
      // 如果获取到用户信息，自动填充表单
      const userData = result.data
      profileData.realName = userData.realName || ''
      profileData.email = userData.email || ''
      profileData.phone = userData.phone || ''
      profileData.avatar = userData.avatar || ''
      profileData.gender = userData.gender || 0
      profileData.birthDate = userData.birthDate || ''
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    alert('获取用户信息失败: ' + error)
  }
}
</script>

<style scoped>
.profile-test {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.test-section {
  margin-bottom: 40px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.test-section h3 {
  margin-top: 0;
  color: #333;
}

.form-section {
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.form-item label {
  width: 100px;
  font-weight: bold;
  margin-right: 10px;
}

.form-item input,
.form-item select {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 10px;
}

button:hover:not(:disabled) {
  background-color: #0056b3;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.upload-section {
  margin-bottom: 20px;
}

.file-info {
  margin-top: 10px;
  padding: 10px;
  background-color: #e9ecef;
  border-radius: 4px;
}

.file-info p {
  margin: 5px 0;
  font-size: 14px;
}

.result {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 15px;
  margin-top: 15px;
}

.result h4 {
  margin-top: 0;
  color: #333;
}

.result pre {
  background-color: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-size: 12px;
}
</style> 