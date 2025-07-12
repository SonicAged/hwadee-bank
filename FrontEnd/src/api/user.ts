import request from '@/utils/request'

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
    return request.get(`/user/${userId}`)
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
  }): Promise<UserInfo[]> {
    return request.get('/user/list', { params })
  },

  // 获取用户权限
  getUserPermissions(userId: number): Promise<string[]> {
    return request.get(`/user/${userId}/permissions`)
  },

  // 获取用户角色
  getUserRoles(userId: number): Promise<string[]> {
    return request.get(`/user/${userId}/roles`)
  }
} 