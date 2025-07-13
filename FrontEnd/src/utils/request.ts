import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
  withCredentials: true // 支持跨域请求携带凭证
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 添加token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 设置请求头以确保UTF-8编码
    if (config.headers && config.method === 'post') {
      config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    }
    
    // 打印请求信息，便于调试
    console.log(`[请求] ${config.method?.toUpperCase()} ${config.url}`, {
      params: config.params,
      data: config.data,
      headers: config.headers
    })
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 打印完整的响应信息，便于调试
    console.log(`[完整响应] ${response.config.method?.toUpperCase()} ${response.config.url}`, {
      status: response.status,
      data: response.data,
      headers: response.headers
    })
    
    if (!response.data) {
      return Promise.reject(new Error('返回数据为空'))
    }
    
    const { code, message, data, success, error } = response.data
    
    if (code === 200 || success === true) {
      // 响应成功，直接返回数据
      return data
    } else if (code === 401) {
      ElMessageBox.alert('登录已过期，请重新登录', '提示', {
        confirmButtonText: '确定',
        callback: () => {
          localStorage.removeItem('token')
          window.location.href = '/login'
        }
      })
      return Promise.reject(new Error(message || '认证失败，请重新登录'))
    } else {
      // 不在这里显示错误消息，让调用的地方处理
      console.error(`[响应错误] 代码: ${code}, 消息: ${message}`)
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  error => {
    console.error('响应错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      // 打印详细错误信息
      console.error(`[HTTP错误] 状态: ${status}`, {
        url: error.config?.url,
        method: error.config?.method?.toUpperCase(),
        data: error.config?.data,
        response: data
      })
      
      switch (status) {
        case 401:
          ElMessage.error('未授权，请重新登录')
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          // 不在这里显示错误消息，让调用的地方处理
          if (data && data.message) {
            return Promise.reject(new Error(data.message))
          }
          break
        default:
          if (data && data.message) {
            return Promise.reject(new Error(data.message))
          }
      }
    } else if (error.request) {
      console.error('[网络错误] 没有收到响应', {
        url: error.config?.url,
        method: error.config?.method?.toUpperCase()
      })
      ElMessage.error('网络连接失败')
    } else {
      console.error('[请求配置错误]', error.message)
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

export default request 