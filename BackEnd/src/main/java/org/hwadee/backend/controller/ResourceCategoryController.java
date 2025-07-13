package org.hwadee.backend.controller;

import org.hwadee.backend.entity.ResourceCategory;
import org.hwadee.backend.service.ResourceService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源分类控制器
 */
@RestController
@RequestMapping("/resource-categories")
public class ResourceCategoryController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 获取分类树结构
     */
    @GetMapping("/tree")
    public Result<List<ResourceCategory>> getCategoryTree() {
        try {
            List<ResourceCategory> categories = resourceService.getCategoryTree();
            return Result.success("获取成功", categories);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取子分类
     */
    @GetMapping("/{parentId}/children")
    public Result<List<ResourceCategory>> getSubCategories(@PathVariable Long parentId) {
        try {
            List<ResourceCategory> categories = resourceService.getSubCategories(parentId);
            return Result.success("获取成功", categories);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有分类（平铺结构）
     */
    @GetMapping("/all")
    public Result<List<ResourceCategory>> getAllCategories() {
        try {
            List<ResourceCategory> categories = resourceService.getAllCategories();
            return Result.success("获取成功", categories);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{categoryId}")
    public Result<ResourceCategory> getCategoryById(@PathVariable Long categoryId) {
        try {
            ResourceCategory category = resourceService.getCategoryById(categoryId);
            if (category == null) {
                return Result.error("分类不存在");
            }
            return Result.success("获取成功", category);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 创建分类
     */
    @PostMapping("/create")
    public Result<ResourceCategory> createCategory(@RequestBody ResourceCategory category) {
        try {
            // 设置创建时间和更新时间
            category.setCreateTime(LocalDateTime.now());
            category.setUpdateTime(LocalDateTime.now());
            
            // 设置默认状态为启用
            if (category.getStatus() == null) {
                category.setStatus(ResourceCategory.STATUS_ENABLED);
            }
            
            // 如果是顶级分类
            if (category.getParentId() == null) {
                category.setParentId(0L);
                category.setLevel(1);
                category.setCategoryPath("0");
            } else {
                // 如果是子分类，获取父分类信息
                ResourceCategory parentCategory = resourceService.getCategoryById(category.getParentId());
                if (parentCategory == null) {
                    return Result.error("父分类不存在");
                }
                
                category.setLevel(parentCategory.getLevel() + 1);
                category.setCategoryPath(parentCategory.getCategoryPath() + "," + category.getParentId());
            }
            
            // 如果没有设置排序，默认为0
            if (category.getSortOrder() == null) {
                category.setSortOrder(0);
            }
            
            ResourceCategory created = resourceService.createCategory(category);
            return Result.success("创建成功", created);
        } catch (Exception e) {
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新分类
     */
    @PutMapping("/{categoryId}")
    public Result<ResourceCategory> updateCategory(@PathVariable Long categoryId, 
                                                  @RequestBody ResourceCategory category) {
        try {
            // 检查分类是否存在
            ResourceCategory existing = resourceService.getCategoryById(categoryId);
            if (existing == null) {
                return Result.error("分类不存在");
            }
            
            // 设置ID和更新时间
            category.setCategoryId(categoryId);
            category.setUpdateTime(LocalDateTime.now());
            
            // 如果更改了父分类，需要重新计算level和path
            if (!existing.getParentId().equals(category.getParentId())) {
                // 如果是顶级分类
                if (category.getParentId() == null || category.getParentId() == 0) {
                    category.setParentId(0L);
                    category.setLevel(1);
                    category.setCategoryPath("0");
                } else {
                    // 如果是子分类，获取父分类信息
                    ResourceCategory parentCategory = resourceService.getCategoryById(category.getParentId());
                    if (parentCategory == null) {
                        return Result.error("父分类不存在");
                    }
                    
                    category.setLevel(parentCategory.getLevel() + 1);
                    category.setCategoryPath(parentCategory.getCategoryPath() + "," + category.getParentId());
                }
            } else {
                // 如果没有更改父分类，保持原来的level和path
                category.setLevel(existing.getLevel());
                category.setCategoryPath(existing.getCategoryPath());
            }
            
            ResourceCategory updated = resourceService.updateCategory(category);
            return Result.success("更新成功", updated);
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{categoryId}")
    public Result<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            // 检查分类是否存在
            ResourceCategory existing = resourceService.getCategoryById(categoryId);
            if (existing == null) {
                return Result.error("分类不存在");
            }
            
            // 检查是否有子分类
            List<ResourceCategory> children = resourceService.getSubCategories(categoryId);
            if (!children.isEmpty()) {
                return Result.error("无法删除存在子分类的分类");
            }
            
            // 检查分类下是否有资源
            int resourceCount = resourceService.getCategoryResourceCount(categoryId);
            if (resourceCount > 0) {
                return Result.error("无法删除存在资源的分类，请先移动或删除分类下的资源");
            }
            
            boolean deleted = resourceService.deleteCategory(categoryId);
            if (deleted) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
} 