import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 自定义响应接口
export interface ResponseData<T = any> {
  code: number
  message: string
  data: T
  success: boolean
  error: boolean
}

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api', // API基础URL
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 从localStorage中获取token并添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 记录请求的URL和参数，方便调试
    console.log(`发送请求: ${config.method?.toUpperCase()} ${config.url}`, 
      config.params ? `参数: ${JSON.stringify(config.params)}` : '',
      config.data ? `数据: ${JSON.stringify(config.data)}` : '')
    
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data

    // 将后端返回的数据包装为我们定义的ResponseData格式
    if (res.code !== undefined) {
      // 检查返回状态
      if (res.code !== 200 && res.code !== 0) {
        ElMessage.error(res.message || '请求出错')
        return Promise.reject(new Error(res.message || '请求出错'))
      }
      return res.data
    }
    
    // 如果后端没有按照标准响应格式返回，则直接返回数据
    return res
  },
  (error) => {
    let message = '请求出错，请稍后重试'
    
    if (error.response) {
      const status = error.response.status
      
      switch (status) {
        case 400:
          message = '请求错误'
          break
        case 401:
          message = '未授权，请重新登录'
          // 清除token并跳转到登录页
          localStorage.removeItem('token')
          setTimeout(() => {
            window.location.href = '/login'
          }, 1500)
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = `请求失败: ${status}`
      }
      
      if (error.response.data && error.response.data.message) {
        message = error.response.data.message
      }
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// 封装GET请求
const get = <T = any>(url: string, params?: any, config?: AxiosRequestConfig): Promise<T> => {
  // 对params参数进行处理，将嵌套对象扁平化
  if (params) {
    // 处理嵌套参数，如params.params
    const flatParams = {...params};
    if (flatParams.params) {
      Object.entries(flatParams.params).forEach(([key, value]) => {
        flatParams[key] = value;
      });
      delete flatParams.params;
    }
    return service.get(url, { params: flatParams, ...config }) as unknown as Promise<T>;
  }
  return service.get(url, { params, ...config }) as unknown as Promise<T>
}

// 封装POST请求
const post = <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
  return service.post(url, data, config) as unknown as Promise<T>
}

// 封装PUT请求
const put = <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
  return service.put(url, data, config) as unknown as Promise<T>
}

// 封装DELETE请求
const del = <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
  return service.delete(url, config) as unknown as Promise<T>
}

// 导出请求方法
export default {
  get,
  post,
  put,
  delete: del,
  request: service.request
} 