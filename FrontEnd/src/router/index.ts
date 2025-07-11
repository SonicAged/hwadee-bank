import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory('/'),
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
        // 用户管理
        {
          path: 'users',
          name: 'UserManagement',
          component: () => import('../views/system/UserManagement.vue'),
          meta: { permission: 'system:user' }
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
              component: () => import('../views/course/CourseList.vue'),
              meta: { permission: 'course:list' }
            },
            {
              path: 'training',
              name: 'TrainingProgram',
              component: () => import('../views/course/TrainingProgram.vue'),
              meta: { permission: 'course:training' }
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
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
