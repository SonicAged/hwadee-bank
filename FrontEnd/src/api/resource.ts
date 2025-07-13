import request from '../utils/request'

// 学习资源接口
export interface LearningResource {
  resourceId: number
  resourceName: string
  resourceType: string
  categoryId: number
  subject?: string
  keywords?: string
  description?: string
  contentUrl?: string
  thumbnailUrl?: string
  fileSize?: number
  duration?: number
  difficultyLevel: number
  creditValue: number
  viewCount: number
  downloadCount: number
  favoriteCount: number
  rating: number
  ratingCount: number
  uploaderId: number
  tags?: string
  prerequisites?: string
  learningObjectives?: string
  status: number
  createTime: string
  updateTime: string
  // 扩展属性
  isFavorited?: boolean
}

// 资源分类接口
export interface ResourceCategory {
  categoryId: number
  categoryName: string
  parentId: number
  categoryPath?: string
  level: number
  sortOrder: number
  icon?: string
  description?: string
  status: number
  createTime: string
  updateTime: string
  children?: ResourceCategory[]
  resourceCount?: number
}

// 资源标签接口
export interface ResourceTag {
  tagId: number
  tagName: string
  tagColor: string
  useCount: number
  status: number
  createTime: string
  updateTime: string
}

// 资源评价接口
export interface ResourceReview {
  reviewId: number
  resourceId: number
  userId: number
  rating: number
  reviewContent?: string
  helpfulCount: number
  status: number
  createTime: string
  updateTime: string
  username?: string
  realName?: string
  resourceName?: string
}

// 分页结果接口
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
}

// 资源搜索参数接口
export interface ResourceSearchParams {
  keyword?: string
  resourceType?: string
  categoryId?: number
  difficultyLevel?: number
  minRating?: number
  tags?: string
  page?: number
  size?: number
}

// 资源管理 API
export const resourceApi = {
  // 基础CRUD操作
  base: {
    // 获取资源列表
    getList(params: {
      page?: number
      size?: number
      resourceName?: string
      resourceType?: string
      categoryId?: number
      difficultyLevel?: number
      status?: number
    }): Promise<PageResult<LearningResource>> {
      console.log('正在请求资源列表，参数:', JSON.stringify(params))
      let url = '/resources/page?'
      if (params.page !== undefined) url += `page=${params.page}&`
      if (params.size !== undefined) url += `size=${params.size}&`
      if (params.resourceName !== undefined) url += `resourceName=${encodeURIComponent(params.resourceName || '')}&`
      if (params.resourceType !== undefined) url += `resourceType=${encodeURIComponent(params.resourceType || '')}&`
      if (params.categoryId !== undefined) url += `categoryId=${params.categoryId}&`
      if (params.difficultyLevel !== undefined) url += `difficultyLevel=${params.difficultyLevel}&`
      if (params.status !== undefined) url += `status=${params.status}&`
      // 移除末尾可能的&
      url = url.endsWith('&') ? url.slice(0, -1) : url
      return request.get(url)
    },

    // 获取资源详情
    getById(resourceId: number): Promise<LearningResource> {
      return request.get(`/resources/${resourceId}`)
    },

    // 创建资源
    create(data: Partial<LearningResource>): Promise<LearningResource> {
      return request.post('/resources/create', data)
    },

    // 更新资源
    update(resourceId: number, data: Partial<LearningResource>): Promise<LearningResource> {
      return request.put(`/resources/${resourceId}`, data)
    },

    // 删除资源
    delete(resourceId: number): Promise<void> {
      return request.delete(`/resources/${resourceId}`)
    },

    // 批量删除资源
    batchDelete(resourceIds: number[]): Promise<void> {
      return request.delete('/resources/batch', { data: resourceIds })
    },
  },

  // 搜索相关
  search: {
    // 关键词搜索
    searchByKeyword(params: {
      keyword: string
      page?: number
      size?: number
    }): Promise<PageResult<LearningResource>> {
      return request.get('/resources/search', { params })
    },

    // 高级搜索
    advancedSearch(params: ResourceSearchParams): Promise<PageResult<LearningResource>> {
      return request.post('/resources/advanced-search', params)
    },

    // 根据分类搜索
    searchByCategory(categoryId: number, params: {
      page?: number
      size?: number
    }): Promise<LearningResource[]> {
      return request.get(`/resources/category/${categoryId}`, { params })
    },

    // 根据类型搜索
    searchByType(resourceType: string, params: {
      page?: number
      size?: number
    }): Promise<LearningResource[]> {
      return request.get(`/resources/type/${resourceType}`, { params })
    }
  },

  // 统计与推荐
  statistics: {
    // 获取热门资源
    getPopular(limit?: number): Promise<LearningResource[]> {
      return request.get('/resources/popular', { params: { limit } })
    },

    // 获取最新资源
    getLatest(limit?: number): Promise<LearningResource[]> {
      return request.get('/resources/latest', { params: { limit } })
    },

    // 获取高评分资源
    getHighRating(limit?: number): Promise<LearningResource[]> {
      return request.get('/resources/high-rating', { params: { limit } })
    },

    // 获取资源统计信息
    getStatistics(): Promise<{
      totalResources: number
      typeCount: Record<string, number>
    }> {
      return request.get('/resources/statistics')
    }
  },

  // 用户交互
  interaction: {
    // 收藏资源
    favorite(resourceId: number): Promise<void> {
      return request.post(`/resources/${resourceId}/favorite`)
    },

    // 取消收藏
    unfavorite(resourceId: number): Promise<void> {
      return request.delete(`/resources/${resourceId}/favorite`)
    },

    // 检查是否已收藏
    isFavorited(resourceId: number): Promise<boolean> {
      return request.get(`/resources/${resourceId}/favorite/status`)
    },

    // 获取用户收藏
    getFavorites(params: {
      page?: number
      size?: number
    }): Promise<LearningResource[]> {
      return request.get('/resources/favorites', { params })
    },

    // 下载资源
    download(resourceId: number): Promise<void> {
      return request.post(`/resources/${resourceId}/download`)
    },

    // 获取资源评价列表
    getReviews(resourceId: number, page: number = 1, size: number = 10): Promise<any> {
      return request.get(`/resource-reviews/resource/${resourceId}`, { params: { page, size } })
    },

    // 获取当前用户对资源的评价
    getUserReview(resourceId: number, userId: number): Promise<any> {
      return request.get(`/resource-reviews/${resourceId}/user/${userId}`)
    },

    // 添加或更新评价
    rateResource(data: {
      resourceId: number,
      rating: number,
      reviewContent?: string
    }): Promise<any> {
      return request.post('/resource-reviews/rate', data)
    },

    // 删除评价
    deleteReview(reviewId: number): Promise<any> {
      return request.delete(`/resource-reviews/${reviewId}`)
    },

    // 获取资源平均评分
    getAverageRating(resourceId: number): Promise<any> {
      return request.get(`/resource-reviews/${resourceId}/average`)
    }
  },

  // 分类管理
  category: {
    // 获取分类树
    getTree(): Promise<ResourceCategory[]> {
      return request.get('/resource-categories/tree')
    },

    // 获取子分类
    getChildren(parentId: number): Promise<ResourceCategory[]> {
      return request.get(`/resource-categories/${parentId}/children`)
    },
    
    // 获取所有分类（平铺结构）
    getAll(): Promise<ResourceCategory[]> {
      return request.get('/resource-categories/all')
    },
    
    // 获取分类详情
    getById(categoryId: number): Promise<ResourceCategory> {
      return request.get(`/resource-categories/${categoryId}`)
    },
    
    // 创建分类
    create(data: Partial<ResourceCategory>): Promise<ResourceCategory> {
      return request.post('/resource-categories/create', data)
    },
    
    // 更新分类
    update(categoryId: number, data: Partial<ResourceCategory>): Promise<ResourceCategory> {
      return request.put(`/resource-categories/${categoryId}`, data)
    },
    
    // 删除分类
    delete(categoryId: number): Promise<void> {
      return request.delete(`/resource-categories/${categoryId}`)
    }
  },

  // 标签管理
  tag: {
    // 获取所有标签
    getAll(): Promise<ResourceTag[]> {
      return request.get('/resources/tags')
    },

    // 获取热门标签
    getPopular(limit?: number): Promise<ResourceTag[]> {
      return request.get(`/resources/tags/popular?limit=${limit || 10}`)
    },

    // 获取资源标签
    getResourceTags(resourceId: number): Promise<ResourceTag[]> {
      return request.get(`/resources/${resourceId}/tags`)
    }
  },

  // 推荐系统
  recommendation: {
    // 获取推荐资源
    getRecommendations(limit?: number): Promise<LearningResource[]> {
      return request.get('/resources/recommendations', { params: { limit } })
    },

    // 获取相似资源
    getSimilar(resourceId: number, limit?: number): Promise<LearningResource[]> {
      return request.get(`/resources/${resourceId}/similar`, { params: { limit } })
    },

    // 获取个性化推荐
    getPersonalized(limit?: number): Promise<LearningResource[]> {
      return request.get('/resources/personalized', { params: { limit } })
    }
  }
}