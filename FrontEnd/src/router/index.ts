import { createRouter, createWebHistory } from 'vue-router'
import CourseList from '../views/course/CourseList.vue'
import CourseDetail from '../views/course/CourseDetail.vue'
import TrainingProgram from '../views/course/TrainingProgram.vue'
import LearningProgress from '../views/course/LearningProgress.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/auth/LoginView.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/auth/RegisterView.vue')
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('../views/DashboardView.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'DashboardHome',
          component: () => import('../views/HomeView.vue')
        },
        // 个人资料管理
        {
          path: 'profile',
          name: 'UserProfile',
          component: () => import('../views/profile/UserProfile.vue')
        },
        {
          path: 'change-password',
          name: 'ChangePassword',
          component: () => import('../views/profile/ChangePassword.vue')
        },
        // API 测试页面
        {
          path: 'api-test',
          name: 'ApiTest',
          component: () => import('../views/test/ApiTest.vue')
        },
        // 个人资料测试页面
        {
          path: 'profile-test',
          name: 'ProfileTest',
          component: () => import('../views/test/ProfileTest.vue')
        },
        // 系统管理
        {
          path: 'users',
          name: 'UserManagement',
          component: () => import('../views/system/UserManagement.vue'),
          meta: { permission: 'system:user' }
        },
        {
          path: 'roles',
          name: 'RoleManagement',
          component: () => import('../views/system/RoleManagement.vue'),
          meta: { permission: 'system:role' }
        },
        {
          path: 'permissions',
          name: 'PermissionManagement',
          component: () => import('../views/system/PermissionManagement.vue'),
          meta: { permission: 'system:permission' }
        },
        // 学分管理
        {
          path: 'credit',
          children: [
            {
              path: 'account',
              name: 'CreditAccount',
              component: () => import('../views/credit/CreditAccount.vue'),
              meta: { permission: 'credit:account' }
            },
            {
              path: 'records',
              name: 'CreditRecords',
              component: () => import('../views/credit/CreditRecords.vue'),
              meta: { permission: 'credit:record' }
            },
            {
              path: 'application',
              name: 'CreditApplication',
              component: () => import('../views/credit/CreditApplication.vue'),
              meta: { permission: 'credit:application' }
            },
            {
              path: 'conversion',
              name: 'CreditConversion',
              component: () => import('../views/credit/CreditConversion.vue'),
              meta: { permission: 'credit:conversion' }
            },
            {
              path: 'statistics',
              name: 'CreditStatistics',
              component: () => import('../views/credit/CreditStatistics.vue'),
              meta: { permission: 'credit:statistics' }
            }
          ]
        },
        // 学习资源管理
        {
          path: 'resources',
          children: [
            {
              path: 'library',
              name: 'ResourceLibrary',
              component: () => import('../views/resource/ResourceLibrary.vue'),
              meta: { permission: 'resource:library' }
            },
            {
              path: 'category',
              name: 'ResourceCategory',
              component: () => import('../views/resource/ResourceCategory.vue'),
              meta: { permission: 'resource:category' }
            }
          ]
        },
        // 课程管理
        {
          path: 'courses',
          children: [
            {
              path: 'list',
              name: 'CourseList',
              component: CourseList,
              meta: { permission: 'course:list' }
            },
            {
              path: 'training',
              name: 'TrainingProgram',
              component: TrainingProgram,
              meta: { permission: 'course:training' }
            },
            {
              path: 'detail/:id',
              name: 'courseDetail',
              component: CourseDetail,
              meta: { title: '课程详情' }
            },
            {
              path: 'progress',
              name: 'learningProgress',
              component: LearningProgress,
              meta: { title: '学习进度' }
            }
          ]
        },
        // 审计管理
        {
          path: 'audit',
          children: [
            {
              path: 'operation',
              name: 'OperationLog',
              component: () => import('../views/audit/OperationLog.vue'),
              meta: { permission: 'audit:operation' }
            },
            {
              path: 'system',
              name: 'SystemLog',
              component: () => import('../views/audit/SystemLog.vue'),
              meta: { permission: 'audit:system' }
            }
          ]
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach(async (to, _from, next) => {
  const token = localStorage.getItem('token')
  
  // 检查是否需要登录
  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }
  
  // 检查权限
  if (to.meta.permission) {
    // 动态导入 auth store 避免循环依赖
    const { useAuthStore } = await import('../stores/auth')
    const authStore = useAuthStore()
    
    // 如果用户信息还没有加载，尝试获取
    if (!authStore.user && token) {
      try {
        await authStore.getUserInfo()
      } catch (error) {
        next('/login')
        return
      }
    }
    
    // 检查用户是否有所需权限
    if (!authStore.hasPermission(to.meta.permission as string)) {
      // 权限不足，跳转到首页或显示错误页面
      next('/dashboard')
      return
    }
  }
  
  next()
})

export default router
