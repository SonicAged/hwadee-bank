import request from '../utils/request'

// 课程接口
export interface Course {
  courseId: number
  courseName: string
  courseCode: string
  categoryId: number
  instructorId: number
  description: string
  syllabus: string
  creditHours: number
  creditValue: number
  maxStudents: number
  currentStudents: number
  startDate: string
  endDate: string
  status: number
  createTime?: string
  updateTime?: string
}

// 课程章节接口
export interface CourseChapter {
  chapterId: number
  courseId: number
  chapterName: string
  chapterOrder: number
  description?: string
  sections: CourseSection[]
}

// 课程小节接口
export interface CourseSection {
  sectionId: number
  chapterId: number
  sectionName: string
  resourceType: string
  contentUrl?: string
  duration?: number
  status: string
  progress: number
}

// 课程资源接口
export interface CourseResource {
  resourceId: number
  courseId: number
  resourceName: string
  resourceType: string
  fileUrl?: string
  fileSize?: number
  duration?: number
  downloadCount: number
  createTime?: string
}

// 学习记录接口
export interface LearningRecord {
  recordId: number
  userId: number
  courseId: number
  sectionId: number
  progress: number
  duration: number
  lastPosition: number
  createTime: string
  updateTime: string
}

// 测试接口
export interface CourseTest {
  testId: number
  testName: string
  testType: string
  courseId: number
  startTime: string
  endTime: string
  duration: number
  questionCount: number
  totalScore: number
  status: string
  score?: number
  questions?: TestQuestion[]
}

// 测试问题接口
export interface TestQuestion {
  questionId: number
  questionText: string
  type: string
  options?: TestQuestionOption[]
  answer?: string | string[] | boolean
}

// 测试选项接口
export interface TestQuestionOption {
  value: string
  text: string
}

// 分页结果接口
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
}

// 课程相关的API
export const courseApi = {
  // 课程管理
  getCourseList(params: {
    page?: number
    size?: number
    courseName?: string
    categoryId?: number
    status?: number
  }): Promise<PageResult<Course>> {
    // 使用URLSearchParams对象构建查询字符串
    const searchParams = new URLSearchParams();
    
    // 只添加已定义的参数
    if (params.page !== undefined && params.page !== null) {
      searchParams.append('page', params.page.toString());
    }
    
    if (params.size !== undefined && params.size !== null) {
      searchParams.append('size', params.size.toString());
    }
    
    if (params.courseName !== undefined && params.courseName !== null && params.courseName !== '') {
      searchParams.append('courseName', params.courseName);
    }
    
    if (params.categoryId !== undefined && params.categoryId !== null) {
      searchParams.append('categoryId', params.categoryId.toString());
    }
    
    if (params.status !== undefined && params.status !== null) {
      searchParams.append('status', params.status.toString());
    }
    
    // 构建完整的URL
    const queryString = searchParams.toString();
    const url = queryString ? `/courses/page?${queryString}` : '/courses/page';
    
    console.log('课程列表API调用URL:', url);
    return request.get(url);
  },

  getCourseById(courseId: number): Promise<Course> {
    return request.get(`/courses/${courseId}`)
  },

  createCourse(course: Partial<Course>): Promise<Course> {
    return request.post('/courses/create', course)
  },

  updateCourse(courseId: number, course: Partial<Course>): Promise<Course> {
    return request.put(`/courses/${courseId}`, course)
  },

  deleteCourse(courseId: number): Promise<void> {
    return request.delete(`/courses/${courseId}`)
  },

  // 课程报名
  enrollCourse(courseId: number, userId: any): Promise<void> {
    // 添加报名日期参数，使用当前日期
    const enrollmentDate = new Date().toISOString().split('T')[0]; // 格式化为YYYY-MM-DD
    
    // 尝试将报名日期作为URL查询参数传递
    return request.post(`/courses/${courseId}/enroll/${userId}?enrollmentDate=${enrollmentDate}`);
  },

  // 退出课程
  withdrawCourse(courseId: number, userId: any): Promise<void> {
    return request.post(`/courses/${courseId}/withdraw/${userId}`)
  },

  // 课程章节
  getCourseChapters(courseId: number): Promise<CourseChapter[]> {
    return request.get(`/courses/${courseId}/chapters`)
  },

  // 课程资源
  getCourseResources(courseId: number, params: {
    page?: number
    size?: number
    type?: string
    keyword?: string
  }): Promise<PageResult<CourseResource>> {
    let url = `/courses/${courseId}/resources?`;
    if (params.page !== undefined) url += `page=${params.page}&`;
    if (params.size !== undefined) url += `size=${params.size}&`;
    if (params.type !== undefined) url += `type=${encodeURIComponent(params.type || '')}&`;
    if (params.keyword !== undefined) url += `keyword=${encodeURIComponent(params.keyword || '')}&`;
    // 移除末尾可能的&
    url = url.endsWith('&') ? url.slice(0, -1) : url;
    return request.get(url);
  },

  downloadResource(resourceId: number): Promise<Blob> {
    return request.get(`/courses/resources/${resourceId}/download`, { 
      responseType: 'blob'
    })
  },

  // 学习记录
  saveLearningProgress(data: {
    courseId: number
    sectionId: number
    progress: number
    duration: number
    lastPosition: number
  }): Promise<void> {
    return request.post('/courses/learning/progress', data)
  },

  getLearningRecord(courseId: number): Promise<LearningRecord[]> {
    return request.get(`/courses/${courseId}/learning/records`)
  },

  // 课程测试
  getCourseTests(courseId: number): Promise<CourseTest[]> {
    return request.get(`/courses/${courseId}/tests`)
  },

  getCourseTestDetail(testId: number): Promise<CourseTest> {
    return request.get(`/courses/tests/${testId}`)
  },

  submitTest(testId: number, answers: any[]): Promise<{
    score: number
    totalScore: number
    correctCount: number
    totalCount: number
  }> {
    return request.post(`/courses/tests/${testId}/submit`, { answers })
  },

  // 课程笔记
  saveNote(data: {
    courseId: number
    sectionId: number
    content: string
  }): Promise<void> {
    return request.post('/courses/notes/save', data)
  },

  getNotes(params: {
    courseId?: number
    sectionId?: number
  }): Promise<any[]> {
    return request.get('/courses/notes', { params })
  },

  // 课程统计
  getCourseStatistics(courseId: number): Promise<{
    enrollCount: number
    completeCount: number
    averageProgress: number
    averageScore: number
  }> {
    return request.get(`/courses/${courseId}/statistics`)
  },
  
  // 培训项目
  getTrainingPrograms(params: {
    page?: number
    size?: number
    programType?: number
    status?: number
  }): Promise<PageResult<any>> {
    let url = '/training/programs?';
    if (params.page !== undefined) url += `page=${params.page}&`;
    if (params.size !== undefined) url += `size=${params.size}&`;
    if (params.programType !== undefined) url += `programType=${params.programType}&`;
    if (params.status !== undefined) url += `status=${params.status}&`;
    // 移除末尾可能的&
    url = url.endsWith('&') ? url.slice(0, -1) : url;
    return request.get(url);
  },

  getTrainingProgramDetail(programId: number): Promise<any> {
    return request.get(`/training/programs/${programId}`)
  },

  enrollTrainingProgram(programId: number): Promise<void> {
    return request.post(`/training/programs/${programId}/enroll`)
  }
} 