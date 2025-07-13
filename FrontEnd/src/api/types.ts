// 分页结果接口
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
} 