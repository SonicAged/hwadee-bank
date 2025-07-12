/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// Extend existing ImportMetaEnv interface
interface ImportMetaEnv {
  readonly VITE_APP_TITLE?: string
  // Add more environment variables as needed
}

// 声明自定义模块路径
declare module '@/types/course' {
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

  export interface CourseChapter {
    chapterId: number
    courseId: number
    chapterName: string
    chapterOrder: number
    description?: string
    sections: CourseSection[]
  }

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

  export interface TestQuestionOption {
    value: string
    text: string
  }

  export interface TestQuestion {
    questionId: number
    questionText: string
    type: string
    options?: TestQuestionOption[]
    answer?: string | string[] | boolean
  }

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
}
