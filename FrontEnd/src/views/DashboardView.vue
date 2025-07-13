<template>
  <el-container class="dashboard-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
      <div class="logo-container">
        <img v-if="!isCollapse" src="../assets/logo.png" alt="Logo" class="logo" />
        <span v-if="!isCollapse" class="logo-text">学分银行</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="false"
        class="el-menu-vertical"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        
        <el-sub-menu index="system" v-if="hasPermission('system')">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/dashboard/users" v-if="hasPermission('system:user')">
            <el-icon><User /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="credit">
          <template #title>
            <el-icon><Coin /></el-icon>
            <span>学分管理</span>
          </template>
          <el-menu-item index="/dashboard/credit/account" v-if="hasPermission('credit:account')">
            <el-icon><Wallet /></el-icon>
            <template #title>学分账户</template>
          </el-menu-item>
          <el-menu-item index="/dashboard/credit/records" v-if="hasPermission('credit:record')">
            <el-icon><Document /></el-icon>
            <template #title>学分记录</template>
          </el-menu-item>
          <el-menu-item index="/dashboard/credit/application" v-if="hasPermission('credit:application')">
            <el-icon><DocumentAdd /></el-icon>
            <template #title>学分申请</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="resources">
          <template #title>
            <el-icon><Files /></el-icon>
            <span>资源管理</span>
          </template>
          <el-menu-item index="/dashboard/resources/library" v-if="hasPermission('resource:library')">
            <el-icon><FolderOpened /></el-icon>
            <template #title>资源库</template>
          </el-menu-item>
          <el-menu-item index="/dashboard/resources/category" v-if="hasPermission('resource:category')">
            <el-icon><Menu /></el-icon>
            <template #title>资源分类</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="courses">
          <template #title>
            <el-icon><Reading /></el-icon>
            <span>课程管理</span>
          </template>
          <el-menu-item index="/dashboard/courses/list" v-if="hasPermission('course:list')">
            <el-icon><List /></el-icon>
            <template #title>课程列表</template>
          </el-menu-item>
          <el-menu-item index="/dashboard/courses/training" v-if="hasPermission('course:training')">
            <el-icon><School /></el-icon>
            <template #title>培训项目</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="audit" v-if="hasPermission('audit')">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>审计管理</span>
          </template>
          <el-menu-item index="/dashboard/audit/operation" v-if="hasPermission('audit:operation')">
            <el-icon><Document /></el-icon>
            <template #title>操作日志</template>
          </el-menu-item>
          <el-menu-item index="/dashboard/audit/system" v-if="hasPermission('audit:system')">
            <el-icon><Warning /></el-icon>
            <template #title>系统日志</template>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-button 
            type="text" 
            @click="toggleCollapse"
            class="collapse-btn"
          >
            <el-icon><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
          </el-button>
          
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
              {{ item.meta?.title || item.name }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleUserCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" :src="getAvatarUrl(user?.avatar)">
                {{ user?.realName?.charAt(0) }}
              </el-avatar>
              <span class="username">{{ user?.realName || user?.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 主体内容 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const isCollapse = ref(false)

// 获取当前激活的菜单
const activeMenu = computed(() => route.path)

// 获取用户信息
const user = computed(() => authStore.user)

// 面包屑导航
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched
})

// 权限检查
const hasPermission = (permission: string) => {
  return authStore.hasPermission(permission)
}

// 获取头像完整URL
const getAvatarUrl = (avatar?: string) => {
  if (!avatar) return ''
  
  // 如果已经是完整URL，直接返回
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  
  // 如果是相对路径，构造完整的头像URL
  return `http://localhost:8080${avatar}`
}

// 切换侧边栏折叠状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 用户操作
const handleUserCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      // 跳转到个人资料页面
      router.push('/dashboard/profile')
      break
    case 'password':
      // 跳转到修改密码页面
      router.push('/dashboard/change-password')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        authStore.logout()
        ElMessage.success('退出成功')
        router.push('/login')
      } catch {
        // 用户取消退出
      }
      break
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  if (authStore.token && !authStore.user) {
    authStore.getUserInfo()
  }
})
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
  transition: width 0.28s;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b2f3a;
  padding: 0 16px;
}

.logo {
  height: 32px;
  margin-right: 8px;
}

.logo-text {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

.el-menu-vertical {
  border-right: none;
  background-color: #304156;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 240px;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  color: #bfcbd9;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: #263445;
}

:deep(.el-menu-item.is-active) {
  background-color: #409eff;
  color: #fff;
}

/* 添加子菜单背景色样式 */
:deep(.el-sub-menu .el-menu) {
  background-color: #263445 !important;
}

:deep(.el-sub-menu .el-menu-item) {
  background-color: #263445;
}

:deep(.el-sub-menu .el-menu-item:hover) {
  background-color: #1f2d3d;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  margin-right: 16px;
  font-size: 18px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f5f5;
}

.username {
  margin-left: 8px;
  margin-right: 4px;
  color: #333;
}

.main-content {
  background-color: #f0f2f5;
  padding: 24px;
}
</style> 