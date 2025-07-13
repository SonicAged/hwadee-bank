import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export interface User {
  userId: number
  username: string
  realName: string
  email: string
  phone: string
  avatar: string
  status: number
}

export interface LoginData {
  username: string
  password: string
  captcha?: string
  rememberMe?: boolean
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const user = ref<User | null>(null)
  const permissions = ref<string[]>([])
  const roles = ref<string[]>([])

  // 登录
  const login = async (loginData: LoginData) => {
    try {
      const response = await request.post('/auth/login', loginData)
      const tokenString = (response.token as unknown) as string
      token.value = tokenString
      localStorage.setItem('token', tokenString)

      // 获取用户信息
      await getUserInfo()
      
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 注册
  const register = async (userData: any) => {
    try {
      await request.post('/auth/register', userData)
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 获取用户信息
  const getUserInfo = async () => {
    try {
      // 调用后端接口获取当前用户信息
      const userInfo: any = await request.get('/auth/userinfo')
      user.value = userInfo as User
      
      // 获取用户的权限和角色
      try {
        const [userPermissions, userRoles] = await Promise.all([
          request.get('/auth/permissions'),
          request.get('/auth/roles')
        ])
        permissions.value = userPermissions as string[]
        roles.value = userRoles as string[]
      } catch (error) {
        console.warn('获取权限和角色失败，使用默认权限', error)
        // 临时使用硬编码权限
        permissions.value = [
          // 系统管理权限
          'system', 'system:user', 'system:role', 'system:permission',
          'system:user:query', 'system:role:query', 'system:permission:query',
          
          // 学分管理权限
          'credit', 'credit:account', 'credit:record', 'credit:application',
          'credit:account:query', 'credit:record:query', 'credit:application:query',
          'credit:conversion', 'credit:statistics',
          
          // 资源管理权限
          'resource', 'resource:library', 'resource:category',
          'resource:library:query', 'resource:library:download',
          'resource:category:query',
          
          // 课程管理权限
          'course', 'course:list', 'course:detail', 'course:progress',
          'course:list:query',
          
          // 培训项目权限
          'training', 'training:program', 'training:program:query',
          
          // 日志审计权限
          'log', 'log:operation', 'log:system',
          
          // 旧版菜单权限(为了兼容当前菜单)
          'course:training',
          'audit', 'audit:operation', 'audit:system'
        ]
        roles.value = ['admin']
      }
      
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 如果获取用户信息失败，清除token并跳转到登录页
      logout()
      window.location.href = '/login'
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    user.value = null
    permissions.value = []
    roles.value = []
    localStorage.removeItem('token')
  }

  // 检查权限
  const hasPermission = (permission: string) => {
    return permissions.value.includes(permission)
  }

  // 检查角色
  const hasRole = (role: string) => {
    return roles.value.includes(role)
  }

  return {
    token,
    user,
    permissions,
    roles,
    login,
    register,
    getUserInfo,
    logout,
    hasPermission,
    hasRole
  }
}) 