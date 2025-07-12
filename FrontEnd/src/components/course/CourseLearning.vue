<template>
  <div class="course-learning">
    <el-card v-if="loading" shadow="never" class="learning-card">
      <el-skeleton :rows="10" animated />
    </el-card>

    <template v-else>
      <!-- 章节列表 -->
      <el-card shadow="never" class="chapter-list">
        <template #header>
          <div class="card-header">
            <span class="header-title">课程章节</span>
            <div class="progress-info">
              <el-progress 
                :percentage="learningProgress" 
                :format="progressFormat"
                :stroke-width="18" 
                status="success"
              />
            </div>
          </div>
        </template>

        <el-collapse v-model="activeChapters" accordion>
          <el-collapse-item 
            v-for="chapter in chapters" 
            :key="chapter.chapterId" 
            :title="chapter.chapterName" 
            :name="chapter.chapterId"
          >
            <div class="section-list">
              <div 
                v-for="section in chapter.sections" 
                :key="section.sectionId" 
                class="section-item"
                :class="{ 'active': activeSection && activeSection.sectionId === section.sectionId }"
                @click="selectSection(section)"
              >
                <div class="section-info">
                  <el-icon class="section-icon">
                    <component :is="getSectionIcon(section.resourceType)" />
                  </el-icon>
                  <span class="section-name">{{ section.sectionName }}</span>
                  <span class="section-duration" v-if="section.duration">
                    {{ formatDuration(section.duration) }}
                  </span>
                </div>
                <el-tag 
                  size="small" 
                  :type="getSectionStatusType(section.status)" 
                  effect="plain"
                >
                  {{ getSectionStatusText(section.status) }}
                </el-tag>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-card>

      <!-- 学习内容展示区 -->
      <div class="learning-content">
        <div v-if="!activeSection" class="no-content">
          <el-empty description="请选择章节开始学习" />
        </div>
        <template v-else>
          <el-card shadow="never" class="content-card">
            <template #header>
              <div class="content-header">
                <h3 class="content-title">{{ activeSection.sectionName }}</h3>
                <div class="content-actions">
                  <el-button 
                    type="success" 
                    size="small" 
                    :disabled="activeSection.status === 'completed'"
                    @click="markAsCompleted(activeSection)"
                  >
                    {{ activeSection.status === 'completed' ? '已完成' : '标记为已完成' }}
                  </el-button>
                </div>
              </div>
            </template>

            <!-- 视频播放器 -->
            <div v-if="activeSection.resourceType === 'video'" class="video-player">
              <video 
                controls 
                class="player" 
                :src="activeSection.contentUrl"
                @timeupdate="handleVideoTimeUpdate"
                @ended="handleVideoEnded"
                ref="videoRef"
              ></video>
            </div>

            <!-- 文档预览 -->
            <div v-else-if="activeSection.resourceType === 'document'" class="document-viewer">
              <div class="document-placeholder">
                <el-empty description="文档预览功能开发中">
                  <template #image>
                    <Document style="width: 80px; height: 80px;" />
                  </template>
                  <template #description>
                    <span>文档预览功能开发中，请下载后查看</span>
                  </template>
                  <el-button type="primary" @click="downloadContent(activeSection)">
                    下载文档
                  </el-button>
                </el-empty>
              </div>
            </div>

            <!-- 测验展示 -->
            <div v-else-if="activeSection.resourceType === 'quiz'" class="quiz-container">
              <div class="quiz-placeholder">
                <el-empty description="测验功能开发中">
                  <template #image>
                    <Edit style="width: 80px; height: 80px;" />
                  </template>
                </el-empty>
              </div>
            </div>

            <!-- 其他类型内容 -->
            <div v-else class="generic-content">
              <el-empty :description="`${activeSection.resourceType}类型内容`">
                <el-button type="primary" @click="downloadContent(activeSection)">
                  下载内容
                </el-button>
              </el-empty>
            </div>

            <!-- 学习笔记 -->
            <div class="learning-notes">
              <h4>学习笔记</h4>
              <el-input
                v-model="learningNote"
                type="textarea"
                :rows="4"
                placeholder="记录您的学习笔记..."
              />
              <div class="notes-actions">
                <el-button type="primary" size="small" @click="saveNote">保存笔记</el-button>
              </div>
            </div>
          </el-card>
        </template>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Document, VideoPlay, Edit, Link, PieChart, 
  Check, View, Download 
} from '@element-plus/icons-vue'

// 章节和小节接口定义
interface Section {
  sectionId: number
  chapterId: number
  sectionName: string
  resourceType: string
  contentUrl?: string
  duration?: number
  status: string
  progress: number
}

interface Chapter {
  chapterId: number
  courseId?: number
  chapterName: string
  chapterOrder?: number
  description?: string
  sections: Section[]
}

// 定义组件props
const props = defineProps({
  courseId: {
    type: Number,
    required: true
  }
})

// 响应式状态
const loading = ref(true)
const chapters = ref<Chapter[]>([])
const activeChapters = ref<number[]>([])
const activeSection = ref<Section | null>(null)
const videoRef = ref<HTMLVideoElement | null>(null)
const learningNote = ref('')
const learningProgress = ref(0)

// 模拟获取课程章节数据
const fetchCourseChapters = async () => {
  loading.value = true
  
  // 模拟API响应
  setTimeout(() => {
    const mockChapters: Chapter[] = [
      {
        chapterId: 1,
        chapterName: '第一章：Java 基础语法',
        sections: [
          {
            sectionId: 101,
            chapterId: 1,
            sectionName: '1.1 Java简介与环境配置',
            resourceType: 'video',
            contentUrl: 'https://example.com/videos/java-intro.mp4',
            duration: 15 * 60, // 15分钟
            status: 'completed',
            progress: 100
          },
          {
            sectionId: 102,
            chapterId: 1,
            sectionName: '1.2 变量与数据类型',
            resourceType: 'video',
            contentUrl: 'https://example.com/videos/java-variables.mp4',
            duration: 20 * 60, // 20分钟
            status: 'in_progress',
            progress: 60
          },
          {
            sectionId: 103,
            chapterId: 1,
            sectionName: '1.3 Java语法练习题',
            resourceType: 'quiz',
            contentUrl: 'https://example.com/quizzes/java-syntax.json',
            status: 'not_started',
            progress: 0
          }
        ]
      },
      {
        chapterId: 2,
        chapterName: '第二章：面向对象编程',
        sections: [
          {
            sectionId: 201,
            chapterId: 2,
            sectionName: '2.1 类与对象',
            resourceType: 'video',
            contentUrl: 'https://example.com/videos/oop-classes.mp4',
            duration: 25 * 60, // 25分钟
            status: 'not_started',
            progress: 0
          },
          {
            sectionId: 202,
            chapterId: 2,
            sectionName: '2.2 面向对象编程指南',
            resourceType: 'document',
            contentUrl: 'https://example.com/docs/oop-guide.pdf',
            status: 'not_started',
            progress: 0
          }
        ]
      }
    ]
    
    chapters.value = mockChapters
    
    // 默认展开第一章
    activeChapters.value = [1]
    
    // 计算学习进度
    calculateLearningProgress()
    
    loading.value = false
  }, 800)
}

// 计算学习进度
const calculateLearningProgress = () => {
  let totalSections = 0
  let completedSections = 0
  
  chapters.value.forEach(chapter => {
    totalSections += chapter.sections.length
    completedSections += chapter.sections.filter(section => section.status === 'completed').length
  })
  
  learningProgress.value = totalSections > 0 ? Math.round((completedSections / totalSections) * 100) : 0
}

// 格式化学习进度
const progressFormat = (percentage: number): string => {
  return `${percentage}%完成`
}

// 格式化时长
const formatDuration = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`
}

// 获取章节图标
const getSectionIcon = (type: string) => {
  switch (type) {
    case 'video': return VideoPlay
    case 'document': return Document
    case 'quiz': return Edit
    case 'assignment': return PieChart
    default: return Link
  }
}

// 获取章节状态
const getSectionStatusText = (status: string): string => {
  switch (status) {
    case 'completed': return '已完成'
    case 'in_progress': return '学习中'
    case 'not_started': return '未开始'
    default: return '未知状态'
  }
}

// 获取章节状态样式
const getSectionStatusType = (status: string): string => {
  switch (status) {
    case 'completed': return 'success'
    case 'in_progress': return 'warning'
    case 'not_started': return 'info'
    default: return ''
  }
}

// 选择章节
const selectSection = (section: Section) => {
  activeSection.value = section
  
  // 如果是视频且为未开始状态，更新为学习中
  if (section.resourceType === 'video' && section.status === 'not_started') {
    section.status = 'in_progress'
    // 这里应该调用API更新学习状态
  }
  
  // 加载该章节的笔记
  loadSectionNote(section.sectionId)
}

// 视频时间更新
const handleVideoTimeUpdate = (event: Event) => {
  if (!activeSection.value) return
  
  const video = event.target as HTMLVideoElement
  const currentTime = Math.floor(video.currentTime)
  const duration = Math.floor(video.duration)
  
  // 计算进度百分比
  const progress = Math.floor((currentTime / duration) * 100)
  
  // 每30秒或进度变化超过10%时保存进度
  if (progress !== activeSection.value.progress && 
     (currentTime % 30 === 0 || Math.abs(progress - activeSection.value.progress) >= 10)) {
    activeSection.value.progress = progress
    // 这里应该调用API保存学习进度
    console.log(`保存进度: ${progress}%`)
  }
}

// 视频播放结束
const handleVideoEnded = () => {
  if (!activeSection.value) return
  
  // 更新为已完成状态
  activeSection.value.status = 'completed'
  activeSection.value.progress = 100
  
  // 重新计算总体学习进度
  calculateLearningProgress()
  
  // 这里应该调用API更新学习状态
  ElMessage.success('恭喜您完成本节学习！')
}

// 标记为已完成
const markAsCompleted = (section: Section) => {
  section.status = 'completed'
  section.progress = 100
  
  // 重新计算总体学习进度
  calculateLearningProgress()
  
  // 这里应该调用API更新学习状态
  ElMessage.success('已标记为完成')
}

// 下载内容
const downloadContent = (section: Section) => {
  // 这里应该调用API下载内容
  ElMessage.success(`正在下载: ${section.sectionName}`)
}

// 加载章节笔记
const loadSectionNote = (sectionId: number) => {
  // 这里应该调用API获取笔记
  // 模拟API响应
  setTimeout(() => {
    learningNote.value = '这是我的学习笔记示例...'
  }, 300)
}

// 保存笔记
const saveNote = () => {
  if (!activeSection.value) return
  
  // 这里应该调用API保存笔记
  ElMessage.success('笔记已保存')
}

// 生命周期钩子
onMounted(() => {
  fetchCourseChapters()
})

// 组件销毁时清理
onUnmounted(() => {
  // 清理可能的计时器或其他资源
})

// 监听课程ID变化
watch(() => props.courseId, () => {
  fetchCourseChapters()
})
</script>

<style scoped>
.course-learning {
  margin-top: 20px;
  display: flex;
  gap: 20px;
}

@media (max-width: 768px) {
  .course-learning {
    flex-direction: column;
  }
}

.chapter-list {
  flex: 0 0 300px;
}

.card-header {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.progress-info {
  margin-top: 8px;
  width: 100%;
}

.section-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.section-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.section-item:hover {
  background-color: #f5f7fa;
}

.section-item.active {
  background-color: #ecf5ff;
}

.section-info {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.section-icon {
  color: #409eff;
}

.section-name {
  flex: 1;
}

.section-duration {
  color: #909399;
  font-size: 12px;
}

.learning-content {
  flex: 1;
}

.no-content {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.content-card {
  min-height: 500px;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-title {
  margin: 0;
  font-size: 18px;
}

.video-player {
  margin-bottom: 20px;
}

.player {
  width: 100%;
  max-height: 450px;
  background-color: #000;
}

.learning-notes {
  margin-top: 24px;
}

.learning-notes h4 {
  margin-bottom: 12px;
  font-size: 16px;
}

.notes-actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.document-viewer,
.quiz-container,
.generic-content {
  min-height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.document-placeholder,
.quiz-placeholder {
  width: 100%;
  text-align: center;
}
</style> 