import request from '../utils/request'

// Role APIs
export function listRoles(query?: any) {
  // 扁平化处理查询参数，避免使用params[key]格式
  if (query && query.params) {
    const { params, ...rest } = query
    return request.get('/system/role/page', { ...params, ...rest })
  }
  return request.get('/system/role/page', query)
}

export function getRoleById(roleId: number) {
  return request.get(`/system/role/${roleId}`)
}

export function addRole(data: any) {
  return request.post('/system/role', data)
}

export function updateRole(data: any) {
  return request.put('/system/role', data)
}

export function deleteRole(roleId: number) {
  return request.delete(`/system/role/${roleId}`)
}

export function listRolePermissions(roleId: number) {
  return request.get(`/system/role/${roleId}/permissions`)
}

export function assignRolePermissions(roleId: number, permissionIds: number[]) {
  return request.post(`/system/role/${roleId}/permissions`, permissionIds)
}

// Permission APIs
export function listPermissions(query?: any) {
  // 扁平化处理查询参数，避免使用params[key]格式
  if (query && query.params) {
    const { params, ...rest } = query
    return request.get('/system/permission/page', { ...params, ...rest })
  }
  return request.get('/system/permission/page', query)
}

export function getPermissionById(permissionId: number) {
  return request.get(`/system/permission/${permissionId}`)
}

export function addPermission(data: any) {
  return request.post('/system/permission', data)
}

export function updatePermission(data: any) {
  return request.put('/system/permission', data)
}

export function deletePermission(permissionId: number) {
  return request.delete(`/system/permission/${permissionId}`)
}

export function getMenuTree() {
  return request.get('/system/permission/menu-tree')
}

export function getUserMenuTree(userId: number) {
  return request.get(`/system/permission/user/${userId}/menu-tree`)
} 