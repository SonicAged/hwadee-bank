import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

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
      // 这里需要先从token中解析用户ID，或者后端提供获取当前用户信息的接口
      // 临时使用固定ID，实际应该从JWT token中获取
      const userId = 1 // 从token中解析
      
      const [userInfo, userPermissions, userRoles] = await Promise.all([
        request.get(`/user/${userId}`),
        request.get(`/user/${userId}/permissions`),
        request.get(`/user/${userId}/roles`)
      ])
      
      user.value = (userInfo as unknown) as User
      permissions.value = (userPermissions as unknown) as string[]
      roles.value = (userRoles as unknown) as string[]
    } catch (error) {
      console.error('获取用户信息失败:', error)
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