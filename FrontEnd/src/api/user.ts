import request from '../utils/request'

// 用户信息接口
export interface UserInfo {
  userId: number
  username: string
  realName: string
  email: string
  phone: string
  avatar: string
  gender: number
  birthDate: string
  createTime: string
  lastLoginTime: string
  status: number
}

// 更新个人资料DTO
export interface UpdateProfileDTO {
  realName: string
  email: string
  phone: string
  avatar: string
  gender: number
  birthDate: string
}

// 修改密码DTO
export interface ChangePasswordDTO {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

// 分页结果接口
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
}

// 用户API服务
export const userApi = {
  // 获取当前用户信息
  getCurrentUser(): Promise<UserInfo> {
    return request.get('/auth/userinfo')
  },

  // 更新个人资料
  updateProfile(profileData: UpdateProfileDTO): Promise<string> {
    return request.put('/user/profile', profileData)
  },

  // 修改密码
  changePassword(passwordData: ChangePasswordDTO): Promise<string> {
    return request.post('/user/change-password', passwordData)
  },

  // 上传头像
  uploadAvatar(file: File): Promise<string> {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/file/upload/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 获取用户信息（根据ID）
  getUserById(userId: number): Promise<UserInfo> {
    return request.get(`/users/${userId}`)
  },

  // 检查用户名是否存在
  checkUsernameExists(username: string): Promise<boolean> {
    return request.get('/auth/check-username', { params: { username } })
  },

  // 检查邮箱是否存在
  checkEmailExists(email: string): Promise<boolean> {
    return request.get('/auth/check-email', { params: { email } })
  },

  // 获取用户列表
  getUserList(params: {
    username?: string
    realName?: string
    status?: number
    page?: number
    size?: number
  }): Promise<PageResult<UserInfo>> {
    return request.get('/users/list', { params })
  },

  // 创建用户
  createUser(userData: {
    username: string
    password: string
    realName: string
    email?: string
    phone?: string
    gender?: number
    status?: number
  }): Promise<UserInfo> {
    return request.post('/users/create', userData)
  },

  // 更新用户
  updateUser(userId: number, userData: Partial<UserInfo>): Promise<string> {
    return request.put(`/users/${userId}`, userData)
  },

  // 删除用户
  deleteUser(userId: number): Promise<string> {
    return request.delete(`/users/${userId}`)
  },

  // 重置用户密码
  resetPassword(userId: number, newPassword: string): Promise<string> {
    return request.post(`/users/${userId}/reset-password`, null, {
      params: { newPassword }
    })
  },

  // 获取用户权限
  getUserPermissions(userId: number): Promise<string[]> {
    return request.get(`/users/${userId}/permissions`)
  },

  // 获取用户角色
  getUserRoles(userId: number): Promise<string[]> {
    return request.get(`/users/${userId}/roles`)
  }
} 