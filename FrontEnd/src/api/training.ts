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
    return request.get('/training/programs', { params })
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
    return request.get('/training/user/programs', { params })
  },

  // 获取培训项目参与者
  getProgramParticipants(programId: number, params: {
    page?: number
    size?: number
    status?: number
  }): Promise<PageResult<TrainingParticipant>> {
    return request.get(`/training/programs/${programId}/participants`, { params })
  },

  // 检查用户是否参与培训项目
  checkUserInProgram(programId: number): Promise<boolean> {
    // 添加时间戳参数避免缓存问题
    const timestamp = new Date().getTime()
    return request.get(`/training/programs/${programId}/check`, {
      params: { _t: timestamp }
    })
  },

  // 获取培训项目统计信息
  getProgramStatistics(programId: number): Promise<TrainingStatistics> {
    return request.get(`/training/programs/${programId}/statistics`)
  },

  // 获取最新培训项目
  getLatestPrograms(limit: number = 5): Promise<TrainingProgram[]> {
    return request.get('/training/programs/latest', { params: { limit } })
  }
} 