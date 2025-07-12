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

// 测试问题选项接口
export interface TestQuestionOption {
  value: string
  text: string
}

// 测试问题接口
export interface TestQuestion {
  questionId: number
  questionText: string
  type: string
  options?: TestQuestionOption[]
  answer?: string | string[] | boolean
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

// 分页结果接口
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
} 