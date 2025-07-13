/// <reference types="vite/client" />

declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 请求响应接口声明
interface ApiResponseData<T = any> {
  code: number
  message: string
  data: T
  success: boolean
  error: boolean
}

// 通用请求方法声明
interface RequestMethods {
  get<T = any>(url: string, config?: any): Promise<ApiResponseData<T>>
  post<T = any>(url: string, data?: any, config?: any): Promise<ApiResponseData<T>>
  put<T = any>(url: string, data?: any, config?: any): Promise<ApiResponseData<T>>
  delete<T = any>(url: string, config?: any): Promise<ApiResponseData<T>>
  request(config: any): Promise<ApiResponseData<any>>
}

// 声明request模块
declare module '@/utils/request' {
  export type ResponseData<T = any> = ApiResponseData<T>
  const request: RequestMethods
  export default request
}

// 系统管理相关类型
declare module '@/types/system' {
  export interface SysRole {
    roleId: number
    roleName: string
    roleKey: string
    description?: string
    status: number
    createTime?: string
    updateTime?: string
  }

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

  export interface TreeNode {
    id: number
    label: string
    children?: TreeNode[]
    [key: string]: any
  }

  export interface QueryParams {
    pageNum: number
    pageSize: number
    [key: string]: any
  }

  export interface PageResult<T> {
    list: T[]
    total: number
    pageNum: number
    pageSize: number
  }
} 