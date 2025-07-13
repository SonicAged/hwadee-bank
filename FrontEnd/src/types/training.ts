// 培训项目接口
export interface TrainingProgram {
  programId: number
  programName: string
  programCode: string
  programType: number
  managerId: number
  description: string
  content: string
  creditValue: number
  cost: number
  maxParticipants: number
  currentParticipants: number
  startTime: string
  endTime: string
  location?: string
  status: number
  enrollDeadline: string
  createTime?: string
  updateTime?: string
}

// 培训项目参与者接口
export interface TrainingParticipant {
  id: number
  programId: number
  userId: number
  status: number
  enrollTime: string
  confirmTime?: string
  completeTime?: string
  remark?: string
  createTime?: string
  updateTime?: string
  // 关联用户信息
  username?: string
  nickname?: string
  email?: string
  mobile?: string
  avatar?: string
}

// 培训项目资源接口
export interface TrainingResource {
  resourceId: number
  programId: number
  resourceName: string
  resourceType: string
  fileUrl?: string
  fileSize?: number
  createTime?: string
  updateTime?: string
}

// 培训项目反馈接口
export interface TrainingFeedback {
  feedbackId: number
  programId: number
  userId: number
  rating: number
  content?: string
  createTime?: string
  updateTime?: string
}

// 培训项目统计信息接口
export interface TrainingStatistics {
  programId: number
  programName: string
  currentParticipants: number
  maxParticipants: number
  enrolledCount: number
  confirmedCount: number
  completedCount: number
  cancelledCount: number
  completionRate: string
} 