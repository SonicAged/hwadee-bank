<template>
  <div class="course-list">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>课程列表</span>
          <el-button type="primary" icon="Plus" @click="showAddDialog">新增课程</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.courseName" placeholder="请输入课程名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="关闭" :value="0" />
            <el-option label="开放" :value="1" />
            <el-option label="满员" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="courses" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="courseId" label="课程ID" width="80" />
        <el-table-column prop="courseName" label="课程名称" min-width="200" />
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        <el-table-column prop="creditValue" label="学分值" width="80" />
        <el-table-column prop="creditHours" label="学时" width="80" />
        <el-table-column prop="currentStudents" label="当前学生数" width="100" />
        <el-table-column prop="maxStudents" label="最大学生数" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewCourse(row)">查看</el-button>
            <el-button type="success" link @click="enrollCourse(row)" 
              :disabled="row.status !== 1">报名</el-button>
            <el-button type="warning" link @click="editCourse(row)">编辑</el-button>
            <el-button type="danger" link @click="deleteCourse(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 新增/编辑课程对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
      <el-form ref="courseForm" :model="formData" :rules="rules" label-width="120px">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="formData.courseName" />
        </el-form-item>
        <el-form-item label="课程编码" prop="courseCode">
          <el-input v-model="formData.courseCode" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择分类">
            <el-option
              v-for="category in categories"
              :key="category.categoryId"
              :label="category.categoryName"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="授课教师" prop="instructorId">
          <el-select v-model="formData.instructorId" placeholder="请选择教师">
            <el-option
              v-for="teacher in teachers"
              :key="teacher.userId"
              :label="teacher.realName"
              :value="teacher.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="课程大纲" prop="syllabus">
          <el-input v-model="formData.syllabus" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item label="学时" prop="creditHours">
          <el-input-number v-model="formData.creditHours" :min="1" />
        </el-form-item>
        <el-form-item label="学分值" prop="creditValue">
          <el-input-number v-model="formData.creditValue" :min="0" :precision="1" :step="0.5" />
        </el-form-item>
        <el-form-item label="最大学生数" prop="maxStudents">
          <el-input-number v-model="formData.maxStudents" :min="1" />
        </el-form-item>
        <el-form-item label="开课日期" prop="startDate">
          <el-date-picker v-model="formData.startDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结课日期" prop="endDate">
          <el-date-picker v-model="formData.endDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="0">关闭</el-radio>
            <el-radio :label="1">开放</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看课程对话框 -->
    <el-dialog v-model="viewDialogVisible" title="课程详情" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="课程ID">{{ currentViewCourse.courseId }}</el-descriptions-item>
        <el-descriptions-item label="课程名称">{{ currentViewCourse.courseName }}</el-descriptions-item>
        <el-descriptions-item label="课程编码">{{ currentViewCourse.courseCode }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ getCategoryName(currentViewCourse.categoryId) }}</el-descriptions-item>
        <el-descriptions-item label="授课教师">{{ getTeacherName(currentViewCourse.instructorId) }}</el-descriptions-item>
        <el-descriptions-item label="课程描述">{{ currentViewCourse.description }}</el-descriptions-item>
        <el-descriptions-item label="课程大纲">{{ currentViewCourse.syllabus }}</el-descriptions-item>
        <el-descriptions-item label="学时">{{ currentViewCourse.creditHours }}</el-descriptions-item>
        <el-descriptions-item label="学分值">{{ currentViewCourse.creditValue }}</el-descriptions-item>
        <el-descriptions-item label="当前学生数">{{ currentViewCourse.currentStudents }}</el-descriptions-item>
        <el-descriptions-item label="最大学生数">{{ currentViewCourse.maxStudents }}</el-descriptions-item>
        <el-descriptions-item label="开课日期">{{ currentViewCourse.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结课日期">{{ currentViewCourse.endDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusColor(currentViewCourse.status)">
            {{ getStatusText(currentViewCourse.status) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="viewDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { storeToRefs } from 'pinia'
import { courseApi, type Course } from '../../api/course'
import { useAuthStore } from '../../stores/auth'
import { resourceApi } from '../../api/resource'
import request from '../../utils/request'

const router = useRouter()
const authStore = useAuthStore()
const { user } = storeToRefs(authStore)

const userId = user.value?.userId

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  courseName: '',
  status: null as number | null
})

// 课程列表数据
const courses = ref<Course[]>([])

// 分类和教师数据
const categories = ref<{categoryId: number, categoryName: string}[]>([])
const teachers = ref<{userId: number, realName: string}[]>([])

// 对话框相关
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('新增课程')
const formData = reactive<Partial<Course>>({
  courseName: '',
  courseCode: '',
  categoryId: 1,
  instructorId: 1,
  description: '',
  syllabus: '',
  creditHours: 36,
  creditValue: 2.0,
  maxStudents: 100,
  currentStudents: 0,
  startDate: '',
  endDate: '',
  status: 1
})
const currentViewCourse = ref<Partial<Course>>({})
const courseForm = ref<FormInstance>()
const rules = reactive<FormRules>({
  courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  courseCode: [{ required: true, message: '请输入课程编码', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'blur' }],
  instructorId: [{ required: true, message: '请选择教师', trigger: 'blur' }],
  creditHours: [{ required: true, message: '请输入学时', trigger: 'blur' }],
  creditValue: [{ required: true, message: '请输入学分值', trigger: 'blur' }],
  maxStudents: [{ required: true, message: '请输入最大学生数', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开课日期', trigger: 'blur' }],
  endDate: [{ required: true, message: '请选择结课日期', trigger: 'blur' }]
})

// 获取课程列表
const fetchCourseList = async () => {
  loading.value = true
  try {
    const result = await courseApi.getCourseList({
      page: currentPage.value,
      size: pageSize.value,
      courseName: searchForm.courseName,
      status: searchForm.status ?? undefined
    })

    courses.value = result.list
    total.value = result.total
  } catch (error) {
    ElMessage.error('获取课程列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const result = await resourceApi.category.getAll()
    categories.value = result
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    console.error(error)
  }
}

// 获取教师列表
const fetchTeachers = async () => {
  try {
    const response = await request.get('/users/teachers')
    teachers.value = response
  } catch (error) {
    ElMessage.error('获取教师列表失败')
    console.error(error)
  }
}

// 根据ID获取分类名称
const getCategoryName = (id: number) => {
  const category = categories.value.find(c => c.categoryId === id)
  return category ? category.categoryName : '未知分类'
}

// 根据ID获取教师姓名
const getTeacherName = (id: number) => {
  const teacher = teachers.value.find(t => t.userId === id)
  return teacher ? teacher.realName : '未知教师'
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchCourseList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.courseName = ''
  searchForm.status = null
  handleSearch()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchCourseList()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchCourseList()
}

// 查看课程
const viewCourse = (course: Course) => {
  currentViewCourse.value = course
  viewDialogVisible.value = true
}

// 报名课程
const enrollCourse = async (course: Course) => {
  try {
    if (course.status === 2) {
      ElMessage.warning('该课程已满员，无法报名')
      return
    }

    await ElMessageBox.confirm(`确定要报名课程 ${course.courseName} 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await courseApi.enrollCourse(course.courseId, userId)
    ElMessage.success('报名成功')
    fetchCourseList() // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('报名失败')
      console.error(error)
    }
  }
}

// 显示新增对话框
const showAddDialog = () => {
  dialogTitle.value = '新增课程'
  resetForm()
  dialogVisible.value = true
}

// 编辑课程
const editCourse = (course: Course) => {
  dialogTitle.value = '编辑课程'
  Object.assign(formData, course)
  dialogVisible.value = true
}

// 删除课程
const deleteCourse = async (course: Course) => {
  try {
    await ElMessageBox.confirm(`确定要删除课程 ${course.courseName} 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await courseApi.deleteCourse(course.courseId)
    ElMessage.success('删除成功')
    fetchCourseList() // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    courseId: undefined,
    courseName: '',
    courseCode: '',
    categoryId: 1,
    instructorId: 1,
    description: '',
    syllabus: '',
    creditHours: 36,
    creditValue: 2.0,
    maxStudents: 100,
    currentStudents: 0,
    startDate: '',
    endDate: '',
    status: 1
  })
  courseForm.value?.resetFields()
}

// 提交表单
const submitForm = async () => {
  try {
    await courseForm.value?.validate()
    
    if (formData.courseId) {
      // 更新课程
      await courseApi.updateCourse(formData.courseId, formData)
      ElMessage.success('更新成功')
    } else {
      // 新增课程
      await courseApi.createCourse(formData)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    fetchCourseList() // 刷新列表
  } catch (error) {
    console.error(error)
  }
}

// 状态相关方法
const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '关闭'
    case 1: return '开放'
    case 2: return '满员'
    default: return '未知'
  }
}

const getStatusColor = (status: number) => {
  switch (status) {
    case 0: return 'danger'
    case 1: return 'success'
    case 2: return 'warning'
    default: return ''
  }
}

// 组件挂载完成后加载数据
onMounted(() => {
  fetchCourseList()
  fetchCategories()
  fetchTeachers()
})
</script>

<style scoped>
.course-list {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-form {
  margin-bottom: 20px;
}
</style>