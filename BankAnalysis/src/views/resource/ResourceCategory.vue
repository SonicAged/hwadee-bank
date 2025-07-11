<template>
  <div class="resource-category">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>资源分类管理</span>
          <el-button type="primary" icon="Plus">新增分类</el-button>
        </div>
      </template>
      
      <el-table :data="categories" border stripe>
        <el-table-column prop="categoryId" label="分类ID" width="100" />
        <el-table-column prop="categoryName" label="分类名称" min-width="200" />
        <el-table-column prop="level" label="级别" width="80" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default>
            <el-button type="primary" link>编辑</el-button>
            <el-button type="danger" link>删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const categories = ref([
  {
    categoryId: 1,
    categoryName: '计算机科学',
    level: 1,
    sortOrder: 1,
    status: 1,
    createTime: '2024-01-01 10:00:00'
  },
  {
    categoryId: 2,
    categoryName: '编程语言',
    level: 2,
    sortOrder: 1,
    status: 1,
    createTime: '2024-01-01 10:00:00'
  }
])
</script>

<style scoped>
.resource-category {
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
</style> 