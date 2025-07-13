import request from '@/utils/request'
import type { ResponseData } from '@/utils/request'

// 学分账户接口
export interface CreditAccount {
  accountId: number
  userId: number
  totalCredits: number
  availableCredits: number
  frozenCredits: number
  createTime: string
  updateTime: string
}

// 学分申请接口
export interface CreditApplication {
  applicationId: number
  userId: number
  applicationType: string
  achievementName: string
  achievementDescription: string
  appliedCredits: number
  evidenceFiles: string
  creditType: string
  creditSource: string
  creditAmount: number
  description: string
  evidenceUrl: string
  status: number
  applyTime: string
  reviewTime?: string
  reviewComment?: string
  rejectionReason?: string
}

// 学分转换规则接口
export interface CreditConversionRule {
  ruleId: number
  sourceType: string
  targetType: string
  conversionRate: number
  minCredits?: number
  maxCredits?: number
  status: number
  createTime: string
  updateTime: string
}

// 学分记录接口
export interface CreditRecord {
  recordId: number
  userId: number
  creditType: string
  creditSource: string
  creditAmount: number
  operationType: number
  status: number
  createTime: string
  remark?: string
}

// 学分统计概览接口
export interface CreditStatisticsOverview {
  totalCredits: number
  availableCredits: number
  frozenCredits: number
  totalRecords: number
  totalApplications: number
  pendingApplications: number
  approvedApplications: number
  rejectedApplications: number
}

// 学分操作趋势接口
export interface CreditTrendStatistics {
  gainRecords: number
  consumeRecords: number
  convertRecords: number
}

// 学分分布统计接口
export interface CreditDistributionStatistics {
  typeDistribution: Record<string, number>
}

// 分页结果接口
interface PageResult<T> {
  list: T[];
  total: number;
}

// 学分管理 API
export const creditApi = {
  // 学分账户相关 API
  account: {
    // 获取用户学分账户
    getUserAccount(userId: number): Promise<CreditAccount> {
      return request.get(`/credit/account/user/${userId}`) as unknown as Promise<CreditAccount>
    },

    // 创建学分账户
    createAccount(userId: number): Promise<void> {
      return request.post(`/credit/account/create?userId=${userId}`) as unknown as Promise<void>
    }
  },

  // 学分申请相关 API
  application: {
    // 提交学分申请
    submit(data: {
      userId: number
      applicationType: string
      achievementName: string
      achievementDescription: string
      appliedCredits: number
      evidenceFiles: string
    }): Promise<void> {
      return request.post('/credit/application/submit', data) as unknown as Promise<void>
    },

    // 获取申请列表
    getList(params: {
      page: number
      size: number
      status?: number
      applicationType?: string
      achievementName?: string
    }): Promise<PageResult<CreditApplication>> {
      return request.get('/credit/application/list', params) as unknown as Promise<PageResult<CreditApplication>>
    },

    // 搜索申请
    search(params: {
      page: number
      size: number
      status?: number
      applicationType?: string
      achievementName?: string
    }): Promise<PageResult<CreditApplication>> {
      return request.get('/credit/application/search', params) as unknown as Promise<PageResult<CreditApplication>>
    },

    // 审核申请
    review(applicationId: number, status: number, reviewComment: string, reviewerId: number): Promise<void> {
      return request.post(`/credit/application/review`, {
        applicationId,
        status,
        reviewComment,
        reviewerId
      }) as unknown as Promise<void>
    }
  },

  // 学分转换相关 API
  conversion: {
    // 获取所有转换规则
    getRules(): Promise<CreditConversionRule[]> {
      return request.get('/credit/conversion/rules') as unknown as Promise<CreditConversionRule[]>
    },

    // 获取特定转换规则
    getRule(sourceType: string, targetType: string): Promise<CreditConversionRule> {
      return request.get('/credit/conversion/rule', {
        sourceType, 
        targetType
      }) as unknown as Promise<CreditConversionRule>
    },

    // 计算转换结果
    calculate(params: {
      sourceType: string
      targetType: string
      sourceCredits: number
    }): Promise<number> {
      return request.get('/credit/conversion/calculate', params) as unknown as Promise<number>
    },

    // 执行转换
    convert(params: {
      userId: number
      sourceType: string
      targetType: string
      sourceCredits: number
    }): Promise<void> {
      return request.post('/credit/conversion/execute', params) as unknown as Promise<void>
    }
  },

  // 学分记录相关 API
  record: {
    // 获取用户学分记录
    getMyRecords(params: {
      page: number
      size: number
    }): Promise<PageResult<CreditRecord>> {
      return request.get('/credit/record/my', params) as unknown as Promise<PageResult<CreditRecord>>
    },

    // 搜索学分记录
    search(params: {
      page: number
      size: number
      creditType?: string
      operationType?: number
      status?: number
    }): Promise<PageResult<CreditRecord>> {
      return request.get('/credit/record/search', params) as unknown as Promise<PageResult<CreditRecord>>
    },

    // 获取记录总数
    getCount(): Promise<number> {
      return request.get('/credit/record/count') as unknown as Promise<number>
    }
  },

  // 学分统计相关 API
  statistics: {
    // 获取统计概览
    getOverview(userId?: number): Promise<CreditStatisticsOverview> {
      return request.get('/credit/statistics/overview', 
        userId ? { userId } : undefined
      ) as unknown as Promise<CreditStatisticsOverview>
    },

    // 获取操作趋势
    getTrend(userId?: number): Promise<CreditTrendStatistics> {
      return request.get('/credit/statistics/trend', 
        userId ? { userId } : undefined
      ) as unknown as Promise<CreditTrendStatistics>
    },

    // 获取类型分布
    getDistribution(userId?: number): Promise<CreditDistributionStatistics> {
      return request.get('/credit/statistics/distribution', 
        userId ? { userId } : undefined
      ) as unknown as Promise<CreditDistributionStatistics>
    }
  }
} 