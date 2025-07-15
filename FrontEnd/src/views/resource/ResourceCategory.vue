<template>
  <div class="resource-category">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>资源分类管理</span>
          <el-button type="primary" @click="handleAddRootCategory">
            <el-icon><Plus /></el-icon>新增分类
          </el-button>
        </div>
      </template>

      <div class="category-container">
        <!-- 分类树 -->
        <div class="category-tree">
          <el-tree
            ref="treeRef"
            :data="treeData"
            node-key="categoryId"
            :props="{
              children: 'children',
              label: 'categoryName',
            }"
            :expand-on-click-node="false"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          >
            <template #default="{ data }">
              <div class="tree-node">
                <span class="node-label">
                  <el-icon v-if="data.icon" :size="16" :color="'#409EFF'">
                    <component :is="data.icon" />
                  </el-icon>
                  <span>{{ data.categoryName }}</span>
                  <el-tag
                    size="small"
                    class="black-tag"
                    v-if="data.resourceCount > 0"
                    type="info"
                  >
                    {{ data.resourceCount }}
                  </el-tag>
                </span>
                <div class="node-actions">
                  <el-button
                    type="primary"
                    link
                    @click.stop="handleAddSubCategory(data)"
                  >
                    <el-icon><Plus /></el-icon>
                  </el-button>
                  <el-button
                    type="primary"
                    link
                    @click.stop="handleEditCategory(data)"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    type="danger"
                    link
                    @click.stop="handleDeleteCategory(data)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
            </template>
          </el-tree>
        </div>

        <!-- 分类表格 -->
        <div class="category-table">
          <el-table :data="categories" border stripe>
            <el-table-column prop="categoryId" label="分类ID" width="40" />
            <el-table-column prop="categoryName" label="分类名称" width="120" />
            <el-table-column prop="level" label="级别" width="35" />
            <el-table-column prop="sortOrder" label="排序" width="35" />
            <el-table-column label="父分类" width="120">
              <template #default="{ row }">
                {{
                  row.parentId === 0
                    ? "顶级分类"
                    : getCategoryName(row.parentId)
                }}
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" width="80" />
            <el-table-column prop="status" label="状态" width="70">
              <template #default="{ row }">
                <el-tag
                  :type="row.status === 1 ? 'success' : 'danger'"
                  class="black-tag"
                >
                  {{ row.status === 1 ? "启用" : "禁用" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditCategory(row)">
                  <el-icon><Edit /></el-icon>编辑
                </el-button>
                <el-button
                  type="danger"
                  link
                  @click="handleDeleteCategory(row)"
                >
                  <el-icon><Delete /></el-icon>删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>

    <!-- 新增/编辑分类对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑分类' : '新增分类'"
      width="600px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父级分类" prop="parentId">
          <el-select
            v-model="form.parentId"
            placeholder="请选择父级分类"
            clearable
            class="wide-select"
            popper-class="wide-dropdown"
          >
            <el-option label="顶级分类" :value="0" />
            <el-option
              v-for="item in flatCategories"
              :key="item.categoryId"
              :label="item.categoryName"
              :value="item.categoryId"
              :disabled="isEdit && item.categoryId === form.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序权重" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            rows="3"
            placeholder="请输入分类描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Edit, Delete } from "@element-plus/icons-vue";
import { resourceApi, type ResourceCategory } from "../../api/resource";

// 数据定义
const categories = ref<ResourceCategory[]>([]);
const treeData = ref<ResourceCategory[]>([]);
const flatCategories = ref<ResourceCategory[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const currentCategory = ref<ResourceCategory | null>(null);

// 表单数据
const formRef = ref();
const form = reactive({
  categoryId: undefined as number | undefined,
  categoryName: "",
  parentId: 0 as number,
  icon: "",
  description: "",
  sortOrder: 0,
  status: 1,
});

// 表单校验规则
const rules = {
  categoryName: [
    { required: true, message: "请输入分类名称", trigger: "blur" },
    { min: 2, max: 50, message: "长度在2到50个字符", trigger: "blur" },
  ],
  parentId: [{ required: true, message: "请选择父级分类", trigger: "change" }],
  sortOrder: [{ required: true, message: "请输入排序权重", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
};

// 初始化
onMounted(async () => {
  await loadData();
});

// 加载数据
const loadData = async () => {
  loading.value = true;
  try {
    const [treeResult, flatResult] = await Promise.all([
      resourceApi.category.getTree(),
      resourceApi.category.getAll(),
    ]);

    treeData.value = treeResult;
    flatCategories.value = flatResult;
    categories.value = flatResult;
  } catch (error) {
    console.error("加载分类数据失败:", error);
    ElMessage.error("加载分类数据失败");
  } finally {
    loading.value = false;
  }
};

// 获取分类名称
const getCategoryName = (categoryId: number): string => {
  const category = flatCategories.value.find(
    (item) => item.categoryId === categoryId
  );
  return category ? category.categoryName : "-";
};

// 树节点点击
const handleNodeClick = (data: ResourceCategory) => {
  currentCategory.value = data;
};

// 添加根分类
const handleAddRootCategory = () => {
  isEdit.value = false;
  form.categoryId = undefined;
  form.categoryName = "";
  form.parentId = 0;
  form.icon = "";
  form.description = "";
  form.sortOrder = 0;
  form.status = 1;

  dialogVisible.value = true;
};

// 添加子分类
const handleAddSubCategory = (data: ResourceCategory) => {
  isEdit.value = false;
  form.categoryId = undefined;
  form.categoryName = "";
  form.parentId = data.categoryId;
  form.icon = "";
  form.description = "";
  form.sortOrder = 0;
  form.status = 1;

  dialogVisible.value = true;
};

// 编辑分类
const handleEditCategory = (data: ResourceCategory) => {
  isEdit.value = true;
  form.categoryId = data.categoryId;
  form.categoryName = data.categoryName;
  form.parentId = data.parentId;
  form.icon = data.icon || "";
  form.description = data.description || "";
  form.sortOrder = data.sortOrder;
  form.status = data.status;

  dialogVisible.value = true;
};

// 删除分类
const handleDeleteCategory = (data: ResourceCategory) => {
  ElMessageBox.confirm(
    `确定要删除分类 "${data.categoryName}" 吗？删除后无法恢复。`,
    "删除确认",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(async () => {
      try {
        await resourceApi.category.delete(data.categoryId);
        ElMessage.success("删除成功");
        loadData(); // 重新加载数据
      } catch (error: any) {
        ElMessage.error(`删除失败: ${error.message || error}`);
      }
    })
    .catch(() => {
      // 取消删除
    });
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;

    try {
      if (isEdit.value) {
        // 编辑
        await resourceApi.category.update(form.categoryId!, {
          categoryName: form.categoryName,
          parentId: form.parentId,
          icon: form.icon,
          description: form.description,
          sortOrder: form.sortOrder,
          status: form.status,
        });
        ElMessage.success("更新成功");
      } else {
        // 新增
        await resourceApi.category.create({
          categoryName: form.categoryName,
          parentId: form.parentId,
          icon: form.icon,
          description: form.description,
          sortOrder: form.sortOrder,
          status: form.status,
        });
        ElMessage.success("创建成功");
      }

      dialogVisible.value = false;
      loadData(); // 重新加载数据
    } catch (error: any) {
      ElMessage.error(`操作失败: ${error.message || error}`);
    }
  });
};
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

.category-container {
  display: flex;
  gap: 20px;
}

.category-tree {
  width: 300px;
  border-right: 1px solid #ebeef5;
  padding-right: 20px;
}

.category-table {
  flex: 1;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 2px 0;
}

.node-label {
  display: flex;
  align-items: center;
  gap: 5px;
}

.node-actions {
  display: none;
}

.tree-node:hover .node-actions {
  display: flex;
  gap: 2px;
}

:deep(.el-tree-node__content) {
  height: 36px;
}

:deep(.el-tag) {
  color: #000000 !important; /* 黑色标签文字 */
}

:deep(.black-tag) {
  color: #000000 !important; /* 黑色标签文字 */
}
</style>
