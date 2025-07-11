<template>
  <div class="resource-library">
    <!-- 搜索和筛选区域 -->
    <el-card class="search-card">
      <div class="search-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input
              v-model="searchParams.keyword"
              placeholder="搜索资源名称、关键词..."
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch" type="primary">
                  <el-icon><Search /></el-icon>
                </el-button>
              </template>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="searchParams.resourceType"
              placeholder="资源类型"
              clearable
              @change="handleSearch"
            >
              <el-option label="全部" value="" />
              <el-option label="课程" value="课程" />
              <el-option label="教材" value="教材" />
              <el-option label="课件" value="课件" />
              <el-option label="实训项目" value="实训项目" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="searchParams.categoryId"
              placeholder="选择分类"
              clearable
              @change="handleSearch"
            >
              <el-option
                v-for="category in categories"
                :key="category.categoryId"
                :label="category.categoryName"
                :value="category.categoryId"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="searchParams.difficultyLevel"
              placeholder="难度级别"
              clearable
              @change="handleSearch"
            >
              <el-option label="全部" value="" />
              <el-option label="初级" :value="1" />
              <el-option label="中级" :value="2" />
              <el-option label="高级" :value="3" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button @click="showAdvancedSearch = true" type="info">
              高级搜索
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 热门标签 -->
    <el-card class="tag-card">
      <template #header>
        <span>热门标签</span>
      </template>
      <div class="tag-list">
        <el-tag
          v-for="tag in popularTags"
          :key="tag.tagId"
          :color="tag.tagColor"
          class="tag-item"
          effect="plain"
          @click="handleTagClick(tag.tagName)"
        >
          {{ tag.tagName }}
        </el-tag>
      </div>
    </el-card>

    <!-- 资源列表 -->
    <el-card class="resource-card">
      <template #header>
        <div class="card-header">
          <span>学习资源库</span>
          <div class="header-actions">
            <el-button-group>
              <el-button 
                :type="viewMode === 'grid' ? 'primary' : 'default'"
                @click="viewMode = 'grid'"
              >
                <el-icon><Grid /></el-icon>
              </el-button>
              <el-button 
                :type="viewMode === 'list' ? 'primary' : 'default'"
                @click="viewMode = 'list'"
              >
                <el-icon><List /></el-icon>
              </el-button>
            </el-button-group>
            <el-button type="primary" @click="showUploadDialog = true">
              <el-icon><Plus /></el-icon>
              上传资源
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 网格视图 -->
      <div v-if="viewMode === 'grid'" class="resource-grid">
        <div v-for="resource in resources" :key="resource.resourceId" class="resource-item">
          <el-card :body-style="{ padding: '16px' }" shadow="hover" @click="handleResourceClick(resource)">
            <div class="resource-content">
              <div class="resource-header">
                <div class="resource-icon">
                  <el-icon size="40" :color="getResourceTypeColor(resource.resourceType)">
                    <component :is="getResourceTypeIcon(resource.resourceType)" />
                  </el-icon>
                </div>
                <div class="resource-actions">
                  <el-button 
                    :type="resource.isFavorited ? 'danger' : 'default'"
                    size="small"
                    circle
                    @click.stop="handleFavorite(resource)"
                  >
                    <el-icon><Star /></el-icon>
                  </el-button>
                </div>
              </div>
              <h4 class="resource-title">{{ resource.resourceName }}</h4>
              <p class="resource-type">{{ resource.resourceType }}</p>
              <p class="resource-description">{{ resource.description }}</p>
              <div class="resource-tags" v-if="resource.tags">
                <el-tag 
                  v-for="tag in resource.tags.split(',')" 
                  :key="tag"
                  size="small"
                  type="info"
                >
                  {{ tag }}
                </el-tag>
              </div>
              <div class="resource-meta">
                <span class="meta-item">
                  <el-icon><Star /></el-icon>
                  {{ resource.rating.toFixed(1) }}
                </span>
                <span class="meta-item">
                  <el-icon><CreditCard /></el-icon>
                  {{ resource.creditValue }}学分
                </span>
                <span class="meta-item difficulty" :class="'difficulty-' + resource.difficultyLevel">
                  {{ getDifficultyText(resource.difficultyLevel) }}
                </span>
              </div>
              <div class="resource-stats">
                <span class="stat-item">
                  <el-icon><View /></el-icon>
                  {{ resource.viewCount }}
                </span>
                <span class="stat-item">
                  <el-icon><Download /></el-icon>
                  {{ resource.downloadCount }}
                </span>
                <span class="stat-item">
                  <el-icon><CollectionTag /></el-icon>
                  {{ resource.favoriteCount }}
                </span>
              </div>
            </div>
          </el-card>
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-else class="resource-list">
        <el-table :data="resources" stripe>
          <el-table-column prop="resourceName" label="资源名称" min-width="200">
            <template #default="{ row }">
              <div class="resource-name-cell">
                <el-icon :color="getResourceTypeColor(row.resourceType)">
                  <component :is="getResourceTypeIcon(row.resourceType)" />
                </el-icon>
                <span>{{ row.resourceName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="resourceType" label="类型" width="100" />
          <el-table-column prop="difficultyLevel" label="难度" width="80">
            <template #default="{ row }">
              <el-tag 
                size="small" 
                :type="getDifficultyTagType(row.difficultyLevel)"
              >
                {{ getDifficultyText(row.difficultyLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="creditValue" label="学分" width="80" />
          <el-table-column prop="rating" label="评分" width="80">
            <template #default="{ row }">
              <el-rate 
                :model-value="row.rating" 
                disabled 
                show-score 
                text-color="#ff9900"
                score-template="{value}"
              />
            </template>
          </el-table-column>
          <el-table-column prop="viewCount" label="浏览" width="80" />
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small" 
                @click="handleResourceClick(row)"
              >
                详情
              </el-button>
              <el-button 
                :type="row.isFavorited ? 'danger' : 'default'"
                size="small"
                @click="handleFavorite(row)"
              >
                {{ row.isFavorited ? '取消收藏' : '收藏' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 高级搜索对话框 -->
    <el-dialog 
      v-model="showAdvancedSearch" 
      title="高级搜索" 
      width="600px"
    >
      <el-form :model="advancedSearchForm" label-width="100px">
        <el-form-item label="关键词">
          <el-input v-model="advancedSearchForm.keyword" placeholder="搜索关键词" />
        </el-form-item>
        <el-form-item label="资源类型">
          <el-select v-model="advancedSearchForm.resourceType" placeholder="选择资源类型">
            <el-option label="全部" value="" />
            <el-option label="课程" value="课程" />
            <el-option label="教材" value="教材" />
            <el-option label="课件" value="课件" />
            <el-option label="实训项目" value="实训项目" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="advancedSearchForm.categoryId" placeholder="选择分类">
            <el-option
              v-for="category in categories"
              :key="category.categoryId"
              :label="category.categoryName"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="难度级别">
          <el-select v-model="advancedSearchForm.difficultyLevel" placeholder="选择难度">
            <el-option label="全部" value="" />
            <el-option label="初级" :value="1" />
            <el-option label="中级" :value="2" />
            <el-option label="高级" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="最低评分">
          <el-rate v-model="advancedSearchForm.minRating" />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="advancedSearchForm.tags" placeholder="输入标签，多个标签用逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAdvancedSearch = false">取消</el-button>
        <el-button type="primary" @click="handleAdvancedSearch">搜索</el-button>
      </template>
    </el-dialog>

    <!-- 上传资源对话框 -->
    <el-dialog 
      v-model="showUploadDialog" 
      title="上传资源" 
      width="800px"
    >
      <el-form :model="uploadForm" label-width="100px">
        <el-form-item label="资源名称" required>
          <el-input v-model="uploadForm.resourceName" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源类型" required>
          <el-select v-model="uploadForm.resourceType" placeholder="选择资源类型">
            <el-option label="课程" value="课程" />
            <el-option label="教材" value="教材" />
            <el-option label="课件" value="课件" />
            <el-option label="实训项目" value="实训项目" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属分类" required>
          <el-select v-model="uploadForm.categoryId" placeholder="选择分类">
            <el-option
              v-for="category in categories"
              :key="category.categoryId"
              :label="category.categoryName"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="难度级别" required>
          <el-select v-model="uploadForm.difficultyLevel" placeholder="选择难度">
            <el-option label="初级" :value="1" />
            <el-option label="中级" :value="2" />
            <el-option label="高级" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="学分价值" required>
          <el-input-number v-model="uploadForm.creditValue" :min="0" :max="10" :step="0.5" />
        </el-form-item>
        <el-form-item label="资源描述">
          <el-input 
            v-model="uploadForm.description" 
            type="textarea" 
            rows="3" 
            placeholder="请输入资源描述"
          />
        </el-form-item>
        <el-form-item label="学习目标">
          <el-input 
            v-model="uploadForm.learningObjectives" 
            type="textarea" 
            rows="3" 
            placeholder="请输入学习目标"
          />
        </el-form-item>
        <el-form-item label="前置要求">
          <el-input 
            v-model="uploadForm.prerequisites" 
            type="textarea" 
            rows="2" 
            placeholder="请输入前置要求"
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-input 
            v-model="uploadForm.tags" 
            placeholder="输入标签，多个标签用逗号分隔"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showUploadDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpload">上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Search, Grid, List, Plus, Star, View, Download, CollectionTag,
  Document, VideoPlay, Reading, Monitor, CreditCard
} from '@element-plus/icons-vue'
import { resourceApi, type LearningResource, type ResourceCategory, type ResourceTag } from '../../api/resource'

// 扩展LearningResource接口添加收藏状态
interface ExtendedLearningResource extends LearningResource {
  isFavorited?: boolean
}

// 响应式数据
const resources = ref<ExtendedLearningResource[]>([])
const categories = ref<ResourceCategory[]>([])
const popularTags = ref<ResourceTag[]>([])
const loading = ref(false)
const viewMode = ref<'grid' | 'list'>('grid')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 搜索参数
const searchParams = reactive({
  keyword: '',
  resourceType: '',
  categoryId: null as number | null,
  difficultyLevel: null as number | null
})

// 高级搜索
const showAdvancedSearch = ref(false)
const advancedSearchForm = reactive({
  keyword: '',
  resourceType: '',
  categoryId: null as number | null,
  difficultyLevel: null as number | null,
  minRating: 0,
  tags: ''
})

// 上传资源
const showUploadDialog = ref(false)
const uploadForm = reactive({
  resourceName: '',
  resourceType: '',
  categoryId: undefined as number | undefined,
  difficultyLevel: 1,
  creditValue: 0,
  description: '',
  learningObjectives: '',
  prerequisites: '',
  tags: ''
})

// 生命周期
onMounted(async () => {
  await loadInitialData()
  await loadResources()
})

// 加载初始数据
const loadInitialData = async () => {
  try {
    const [categoryRes, tagRes] = await Promise.all([
      resourceApi.category.getTree(),
      resourceApi.tag.getPopular(20)
    ])
    categories.value = categoryRes
    popularTags.value = tagRes
  } catch (error) {
    console.error('加载初始数据失败:', error)
  }
}

// 加载资源列表
const loadResources = async () => {
  loading.value = true
  try {
    const response = await resourceApi.base.getList({
      page: currentPage.value,
      size: pageSize.value,
      resourceName: searchParams.keyword,
      resourceType: searchParams.resourceType,
      categoryId: searchParams.categoryId || undefined,
      difficultyLevel: searchParams.difficultyLevel || undefined,
      status: 1
    })
    
    console.log('返回的资源数据:', response)
    resources.value = response.list || [] // 修改为list
    total.value = response.total
    
    // 检查收藏状态
    await checkFavoriteStatus()
  } catch (error) {
    console.error('加载资源失败:', error)
    ElMessage.error('加载资源失败')
  } finally {
    loading.value = false
  }
}

// 检查收藏状态
const checkFavoriteStatus = async () => {
  try {
    const favoriteChecks = resources.value.map(async (resource) => {
      try {
        const isFavorited = await resourceApi.interaction.isFavorited(resource.resourceId)
        resource.isFavorited = isFavorited
      } catch (error) {
        resource.isFavorited = false
      }
    })
    await Promise.all(favoriteChecks)
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  loadResources()
}

// 高级搜索处理
const handleAdvancedSearch = () => {
  Object.assign(searchParams, advancedSearchForm)
  showAdvancedSearch.value = false
  handleSearch()
}

// 标签点击处理
const handleTagClick = (tagName: string) => {
  searchParams.keyword = tagName
  handleSearch()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  loadResources()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadResources()
}

// 资源点击处理
const handleResourceClick = (resource: LearningResource) => {
  // 跳转到资源详情页
  console.log('查看资源详情:', resource)
}

// 收藏处理
const handleFavorite = async (resource: LearningResource) => {
  try {
    if (resource.isFavorited) {
      await resourceApi.interaction.unfavorite(resource.resourceId)
      resource.isFavorited = false
      ElMessage.success('取消收藏成功')
    } else {
      await resourceApi.interaction.favorite(resource.resourceId)
      resource.isFavorited = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('收藏操作失败')
  }
}

// 上传处理
const handleUpload = async () => {
  try {
    await resourceApi.base.create(uploadForm)
    ElMessage.success('上传成功')
    showUploadDialog.value = false
    loadResources()
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败')
  }
}

// 工具函数
const getDifficultyText = (level: number) => {
  const levels = ['', '初级', '中级', '高级']
  return levels[level] || '未知'
}

const getDifficultyTagType = (level: number) => {
  const types = ['', 'success', 'warning', 'danger']
  return types[level] || 'info'
}

const getResourceTypeIcon = (type: string) => {
  const icons: Record<string, any> = {
    '课程': VideoPlay,
    '教材': Reading,
    '课件': Document,
    '实训项目': Monitor
  }
  return icons[type] || Document
}

const getResourceTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    '课程': '#409EFF',
    '教材': '#67C23A',
    '课件': '#E6A23C',
    '实训项目': '#F56C6C'
  }
  return colors[type] || '#909399'
}
</script>

<style scoped>
.resource-library {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.tag-card {
  margin-bottom: 20px;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  cursor: pointer;
  transition: all 0.3s;
}

.tag-item:hover {
  transform: translateY(-2px);
}

.resource-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.resource-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.resource-item {
  transition: transform 0.3s;
  cursor: pointer;
}

.resource-item:hover {
  transform: translateY(-2px);
}

.resource-content {
  text-align: left;
}

.resource-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.resource-actions {
  display: flex;
  gap: 8px;
}

.resource-title {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.resource-type {
  color: #909399;
  margin: 0 0 8px 0;
  font-size: 12px;
}

.resource-description {
  color: #606266;
  margin: 0 0 12px 0;
  font-size: 14px;
  line-height: 1.5;
  height: 42px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.resource-tags {
  margin-bottom: 12px;
}

.resource-tags .el-tag {
  margin-right: 4px;
  margin-bottom: 4px;
}

.resource-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
}

.difficulty {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.difficulty-1 {
  background: #f0f9ff;
  color: #1890ff;
}

.difficulty-2 {
  background: #fff7e6;
  color: #fa8c16;
}

.difficulty-3 {
  background: #fff2f0;
  color: #f5222d;
}

.resource-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.resource-list {
  margin-bottom: 20px;
}

.resource-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.el-dialog {
  border-radius: 8px;
}

.el-form-item {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .resource-grid {
    grid-template-columns: 1fr;
  }
  
  .search-form .el-row {
    flex-direction: column;
    gap: 12px;
  }
  
  .card-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style> 