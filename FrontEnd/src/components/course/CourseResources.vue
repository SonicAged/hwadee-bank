<template>
  <div class="course-resources">
    <el-card shadow="never" class="resource-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">课程资源</span>
          <el-input
            v-model="searchKeyword"
            placeholder="搜索资源"
            prefix-icon="Search"
            clearable
            @input="handleSearch"
            class="search-input"
          />
        </div>
      </template>

      <el-tabs v-model="activeResourceType" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane label="视频" name="video"></el-tab-pane>
        <el-tab-pane label="文档" name="document"></el-tab-pane>
        <el-tab-pane label="PPT" name="ppt"></el-tab-pane>
        <el-tab-pane label="其他" name="other"></el-tab-pane>
      </el-tabs>

      <div v-if="loading" class="resource-loading">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="filteredResources.length === 0" class="empty-resources">
        <el-empty description="暂无资源" />
      </div>

      <div v-else class="resource-list">
        <el-card 
          v-for="resource in filteredResources" 
          :key="resource.resourceId"
          shadow="hover" 
          class="resource-item"
        >
          <div class="resource-content">
            <div class="resource-icon">
              <el-icon :size="32" :class="getResourceIconClass(resource.resourceType)">
                <component :is="getResourceIcon(resource.resourceType)" />
              </el-icon>
            </div>
            <div class="resource-info">
              <div class="resource-name">{{ resource.resourceName }}</div>
              <div class="resource-meta">
                <span class="resource-type">{{ getResourceTypeName(resource.resourceType) }}</span>
                <span class="resource-size" v-if="resource.fileSize">{{ formatFileSize(resource.fileSize) }}</span>
              </div>
            </div>
            <div class="resource-actions">
              <el-tooltip content="查看" placement="top">
                <el-button circle @click="viewResource(resource)" type="primary">
                  <el-icon><View /></el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="下载" placement="top">
                <el-button circle @click="downloadResource(resource)" type="success">
                  <el-icon><Download /></el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </el-card>
      </div>

      <el-pagination
        v-if="totalResources > 0"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalResources"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Document, VideoPlay, Picture, Files, Box, 
  View, Download, Search 
} from '@element-plus/icons-vue'
import type { CourseResource } from '../../types/course'

// 定义组件props
const props = defineProps({
  courseId: {
    type: Number,
    required: true
  }
})

// 资源类型
const resourceTypes: Record<string, string> = {
  video: '视频',
  document: '文档',
  ppt: 'PPT',
  image: '图片',
  audio: '音频',
  other: '其他'
}

// 响应式状态
const loading = ref(true)
const resources = ref<CourseResource[]>([])
const activeResourceType = ref('all')
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalResources = ref(0)

// 模拟API调用
const fetchResources = async () => {
  loading.value = true
  
  // 这里应该是实际的API调用
  // const response = await courseApi.getCourseResources(props.courseId, {
  //   page: currentPage.value,
  //   size: pageSize.value,
  //   type: activeResourceType.value === 'all' ? null : activeResourceType.value,
  //   keyword: searchKeyword.value
  // })
  
  // 模拟API响应
  setTimeout(() => {
    const mockResources = [
      {
        resourceId: 1,
        resourceName: 'Java基础入门视频',
        resourceType: 'video',
        fileSize: 256 * 1024 * 1024,
        duration: 45,
        downloadCount: 120,
        courseId: props.courseId
      },
      {
        resourceId: 2,
        resourceName: '面向对象编程指南',
        resourceType: 'document',
        fileSize: 2.5 * 1024 * 1024,
        downloadCount: 89,
        courseId: props.courseId
      },
      {
        resourceId: 3,
        resourceName: 'Java高级特性课件',
        resourceType: 'ppt',
        fileSize: 5.8 * 1024 * 1024,
        downloadCount: 65,
        courseId: props.courseId
      }
    ]
    
    resources.value = mockResources as CourseResource[]
    totalResources.value = 3
    loading.value = false
  }, 800)
}

// 计算属性：筛选后的资源
const filteredResources = computed(() => {
  return resources.value
})

// 生命周期钩子
onMounted(() => {
  fetchResources()
})

// 监听筛选条件变化
watch([activeResourceType, currentPage, pageSize], () => {
  fetchResources()
})

// 方法
const handleTabChange = () => {
  currentPage.value = 1
  fetchResources()
}

const handleSearch = () => {
  currentPage.value = 1
  fetchResources()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchResources()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchResources()
}

const getResourceIcon = (type: string) => {
  switch (type) {
    case 'video': return VideoPlay
    case 'document': return Document
    case 'ppt': return Picture
    case 'image': return Picture
    case 'audio': return 'Headset'
    default: return Files
  }
}

const getResourceIconClass = (type: string) => {
  return `icon-${type}`
}

const getResourceTypeName = (type: string) => {
  return resourceTypes[type] || '未知类型'
}

const formatFileSize = (size: number) => {
  if (size < 1024) {
    return size + 'B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(1) + 'KB'
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(1) + 'MB'
  } else {
    return (size / (1024 * 1024 * 1024)).toFixed(1) + 'GB'
  }
}

const viewResource = (resource: CourseResource) => {
  ElMessage.success(`查看资源：${resource.resourceName}`)
  // 根据资源类型进行不同处理
  if (resource.resourceType === 'video') {
    // 打开视频播放器
  } else if (resource.resourceType === 'document' || resource.resourceType === 'ppt') {
    // 打开文档预览
  } else {
    // 其他类型处理
  }
}

const downloadResource = (resource: CourseResource) => {
  ElMessage.success(`下载资源：${resource.resourceName}`)
  // 调用下载API
}
</script>

<style scoped>
.course-resources {
  margin-top: 20px;
}

.resource-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.search-input {
  width: 250px;
}

.resource-loading, .empty-resources {
  padding: 40px 0;
  text-align: center;
}

.resource-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 20px;
}

.resource-item {
  border-radius: 8px;
  transition: transform 0.2s;
}

.resource-item:hover {
  transform: translateY(-2px);
}

.resource-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.resource-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 8px;
  background-color: #f5f7fa;
}

.icon-video { color: #409eff; }
.icon-document { color: #67c23a; }
.icon-ppt { color: #e6a23c; }
.icon-image { color: #409eff; }
.icon-audio { color: #f56c6c; }

.resource-info {
  flex: 1;
}

.resource-name {
  font-weight: 500;
  margin-bottom: 4px;
}

.resource-meta {
  display: flex;
  gap: 12px;
  color: #909399;
  font-size: 12px;
}

.resource-actions {
  display: flex;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 