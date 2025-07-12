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
      const tokenString = (response as unknown) as string
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
      
      // TODO: 后续可以添加获取用户权限和角色的接口
      // const [userPermissions, userRoles] = await Promise.all([
      //   request.get('/auth/permissions'),
      //   request.get('/auth/roles')
      // ])
      // permissions.value = userPermissions as string[]
      // roles.value = userRoles as string[]
      
      // 临时设置一些默认权限
      permissions.value = ['system', 'system:user', 'credit:account', 'credit:record', 'credit:application', 'resource:library', 'resource:category', 'course:list', 'course:training', 'audit:operation', 'audit:system']
      roles.value = ['user']
      
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