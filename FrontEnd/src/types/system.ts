// 角色相关类型
export interface SysRole {
  roleId: number
  roleName: string
  roleKey: string
  description?: string
  status: number
  createTime?: string
  updateTime?: string
}

// 权限相关类型
export interface SysPermission {
  permissionId: number
  permissionName: string
  permissionKey: string
  permissionType: number
  parentId: number
  path?: string
  component?: string
  icon?: string
  sortOrder: number
  status: number
  createTime?: string
  updateTime?: string
}

// 权限树节点
export interface TreeNode {
  id: number
  label: string
  children?: TreeNode[]
  [key: string]: any
}

// 分页查询参数
export interface QueryParams {
  pageNum: number
  pageSize: number
  [key: string]: any
}

// 分页结果
export interface PageResult<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
} 