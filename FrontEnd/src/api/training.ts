import request from '../utils/request'
import type { PageResult } from './types'
import type { TrainingProgram, TrainingParticipant, TrainingStatistics } from '../types/training'

// 培训项目相关的API
export const trainingApi = {
  // 获取培训项目列表
  getProgramList(params: {
    page?: number
    size?: number
    programName?: string
    programType?: number
    status?: number
  }): Promise<PageResult<TrainingProgram>> {
    // 使用URLSearchParams对象构建查询字符串
    const searchParams = new URLSearchParams();
    
    // 只添加已定义的参数
    if (params.page !== undefined && params.page !== null) {
      searchParams.append('page', params.page.toString());
    }
    
    if (params.size !== undefined && params.size !== null) {
      searchParams.append('size', params.size.toString());
    }
    
    if (params.programName !== undefined && params.programName !== null && params.programName !== '') {
      searchParams.append('programName', params.programName);
    }
    
    if (params.programType !== undefined && params.programType !== null) {
      searchParams.append('programType', params.programType.toString());
    }
    
    if (params.status !== undefined && params.status !== null) {
      searchParams.append('status', params.status.toString());
    }
    
    // 构建完整的URL
    const queryString = searchParams.toString();
    const url = queryString ? `/training/programs?${queryString}` : '/training/programs';
    
    console.log('培训项目API调用URL:', url);
    return request.get(url);
  },

  // 获取培训项目详情
  getProgramById(programId: number): Promise<TrainingProgram> {
    return request.get(`/training/programs/${programId}`)
  },

  // 创建培训项目
  createProgram(program: Partial<TrainingProgram>): Promise<TrainingProgram> {
    return request.post('/training/programs', program)
  },

  // 更新培训项目
  updateProgram(programId: number, program: Partial<TrainingProgram>): Promise<TrainingProgram> {
    return request.put(`/training/programs/${programId}`, program)
  },

  // 删除培训项目
  deleteProgram(programId: number): Promise<void> {
    return request.delete(`/training/programs/${programId}`)
  },

  // 报名参加培训项目
  enrollProgram(programId: number): Promise<void> {
    return request.post(`/training/programs/${programId}/enroll`)
  },

  // 取消报名
  cancelEnrollment(programId: number): Promise<void> {
    return request.post(`/training/programs/${programId}/cancel`)
  },

  // 确认参与培训
  confirmParticipation(participantId: number): Promise<void> {
    return request.post(`/training/participants/${participantId}/confirm`)
  },

  // 完成培训
  completeTraining(participantId: number): Promise<void> {
    return request.post(`/training/participants/${participantId}/complete`)
  },

  // 获取用户参与的培训项目
  getUserPrograms(params: {
    page?: number
    size?: number
    status?: number
  }): Promise<PageResult<any>> {
    let url = '/training/user/programs?';
    if (params.page !== undefined) url += `page=${params.page}&`;
    if (params.size !== undefined) url += `size=${params.size}&`;
    if (params.status !== undefined) url += `status=${params.status}&`;
    // 移除末尾可能的&
    url = url.endsWith('&') ? url.slice(0, -1) : url;
    return request.get(url);
  },

  // 获取培训项目参与者
  getProgramParticipants(programId: number, params: {
    page?: number
    size?: number
    status?: number
  }): Promise<PageResult<TrainingParticipant>> {
    let url = `/training/programs/${programId}/participants?`;
    if (params.page !== undefined) url += `page=${params.page}&`;
    if (params.size !== undefined) url += `size=${params.size}&`;
    if (params.status !== undefined) url += `status=${params.status}&`;
    // 移除末尾可能的&
    url = url.endsWith('&') ? url.slice(0, -1) : url;
    return request.get(url);
  },

  // 检查用户是否参与培训项目
  checkUserInProgram(programId: number): Promise<boolean> {
    // 添加时间戳参数避免缓存问题
    const timestamp = new Date().getTime()
    return request.get(`/training/programs/${programId}/check?_t=${timestamp}`)
  },

  // 获取培训项目统计信息
  getProgramStatistics(programId: number): Promise<TrainingStatistics> {
    return request.get(`/training/programs/${programId}/statistics`)
  },

  // 获取最新培训项目
  getLatestPrograms(limit: number = 5): Promise<TrainingProgram[]> {
    return request.get(`/training/programs/latest?limit=${limit}`)
  }
} 