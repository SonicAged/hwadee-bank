package org.hwadee.backend.controller;

import org.hwadee.backend.entity.*;
import org.hwadee.backend.service.ResourceService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 资源管理控制器
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    // ========== 资源CRUD操作 ==========

    /**
     * 创建学习资源
     */
    @PostMapping("/create")
    public Result<LearningResource> createResource(@RequestBody LearningResource resource, 
                                                  HttpServletRequest request) {
        try {
            // 从请求中获取用户ID（通常从JWT或Session中获取）
            Long userId = getCurrentUserId(request);
            resource.setUploaderId(userId);
            
            LearningResource created = resourceService.createResource(resource);
            return Result.success("资源创建成功", created);
        } catch (Exception e) {
            return Result.error("资源创建失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取学习资源
     */
    @GetMapping("/{resourceId}")
    public Result<LearningResource> getResourceById(@PathVariable Long resourceId) {
        try {
            LearningResource resource = resourceService.getResourceById(resourceId);
            if (resource == null) {
                return Result.error("资源不存在");
            }
            
            // 增加浏览次数
            resourceService.incrementViewCount(resourceId);
            
            return Result.success("获取成功", resource);
        } catch (Exception e) {
            return Result.error("获取资源失败：" + e.getMessage());
        }
    }

    /**
     * 更新学习资源
     */
    @PutMapping("/{resourceId}")
    public Result<LearningResource> updateResource(@PathVariable Long resourceId, 
                                                  @RequestBody LearningResource resource) {
        try {
            resource.setResourceId(resourceId);
            LearningResource updated = resourceService.updateResource(resource);
            return Result.success("资源更新成功", updated);
        } catch (Exception e) {
            return Result.error("资源更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除学习资源
     */
    @DeleteMapping("/{resourceId}")
    public Result<String> deleteResource(@PathVariable Long resourceId) {
        try {
            boolean deleted = resourceService.deleteResource(resourceId);
            if (deleted) {
                return Result.success("资源删除成功");
            } else {
                return Result.error("资源删除失败");
            }
        } catch (Exception e) {
            return Result.error("资源删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除学习资源
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteResources(@RequestBody List<Long> resourceIds) {
        try {
            boolean deleted = resourceService.batchDeleteResources(resourceIds);
            if (deleted) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    // ========== 资源查询与分页 ==========

    /**
     * 分页查询学习资源
     */
    @GetMapping("/page")
    public Result<PageResult<LearningResource>> getResourcesByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String resourceName,
            @RequestParam(required = false) String resourceType,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer difficultyLevel,
            @RequestParam(required = false) Integer status) {
        try {
            // 调试信息
            System.out.println("接收到分页查询请求: page=" + page + ", size=" + size);
            PageResult<LearningResource> result = resourceService.getResourcesByPage(
                page, size, resourceName, resourceType, categoryId, difficultyLevel, status);
            System.out.println("查询结果: 总记录数=" + result.getTotal() + ", 返回记录数=" + result.getList().size());
            return Result.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据分类查询资源
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<LearningResource>> getResourcesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<LearningResource> resources = resourceService.getResourcesByCategory(categoryId, page, size);
            return Result.success("查询成功", resources);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据资源类型查询资源
     */
    @GetMapping("/type/{resourceType}")
    public Result<List<LearningResource>> getResourcesByType(
            @PathVariable String resourceType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<LearningResource> resources = resourceService.getResourcesByType(resourceType, page, size);
            return Result.success("查询成功", resources);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // ========== 资源搜索功能 ==========

    /**
     * 关键词搜索资源
     */
    @GetMapping("/search")
    public Result<PageResult<LearningResource>> searchResources(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResult<LearningResource> result = resourceService.searchResources(keyword, page, size);
            return Result.success("搜索成功", result);
        } catch (Exception e) {
            return Result.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 高级搜索资源
     */
    @PostMapping("/advanced-search")
    public Result<PageResult<LearningResource>> advancedSearchResources(
            @RequestBody Map<String, Object> searchParams) {
        try {
            String keyword = (String) searchParams.get("keyword");
            String resourceType = (String) searchParams.get("resourceType");
            Long categoryId = searchParams.get("categoryId") != null ? 
                Long.valueOf(searchParams.get("categoryId").toString()) : null;
            Integer difficultyLevel = searchParams.get("difficultyLevel") != null ? 
                Integer.valueOf(searchParams.get("difficultyLevel").toString()) : null;
            Double minRating = searchParams.get("minRating") != null ? 
                Double.valueOf(searchParams.get("minRating").toString()) : null;
            String tags = (String) searchParams.get("tags");
            int page = searchParams.get("page") != null ? 
                Integer.valueOf(searchParams.get("page").toString()) : 1;
            int size = searchParams.get("size") != null ? 
                Integer.valueOf(searchParams.get("size").toString()) : 10;

            PageResult<LearningResource> result = resourceService.advancedSearchResources(
                keyword, resourceType, categoryId, difficultyLevel, minRating, tags, page, size);
            return Result.success("高级搜索成功", result);
        } catch (Exception e) {
            return Result.error("高级搜索失败：" + e.getMessage());
        }
    }

    // ========== 资源统计与分析 ==========

    /**
     * 获取热门资源
     */
    @GetMapping("/popular")
    public Result<List<LearningResource>> getPopularResources(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<LearningResource> resources = resourceService.getPopularResources(limit);
            return Result.success("获取成功", resources);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取最新资源
     */
    @GetMapping("/latest")
    public Result<List<LearningResource>> getLatestResources(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<LearningResource> resources = resourceService.getLatestResources(limit);
            return Result.success("获取成功", resources);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取高评分资源
     */
    @GetMapping("/high-rating")
    public Result<List<LearningResource>> getHighRatingResources(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<LearningResource> resources = resourceService.getHighRatingResources(limit);
            return Result.success("获取成功", resources);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取资源统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getResourceStatistics() {
        try {
            Map<String, Object> statistics = resourceService.getResourceStatistics();
            return Result.success("获取成功", statistics);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    // ========== 资源交互操作 ==========

    /**
     * 收藏资源
     */
    @PostMapping("/{resourceId}/favorite")
    public Result<String> favoriteResource(@PathVariable Long resourceId, 
                                          HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean favorited = resourceService.favoriteResource(userId, resourceId);
            if (favorited) {
                return Result.success("收藏成功");
            } else {
                return Result.error("已收藏过该资源");
            }
        } catch (Exception e) {
            return Result.error("收藏失败：" + e.getMessage());
        }
    }

    /**
     * 取消收藏资源
     */
    @DeleteMapping("/{resourceId}/favorite")
    public Result<String> unfavoriteResource(@PathVariable Long resourceId, 
                                            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean unfavorited = resourceService.unfavoriteResource(userId, resourceId);
            if (unfavorited) {
                return Result.success("取消收藏成功");
            } else {
                return Result.error("取消收藏失败");
            }
        } catch (Exception e) {
            return Result.error("取消收藏失败：" + e.getMessage());
        }
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/{resourceId}/favorite/status")
    public Result<Boolean> isFavorited(@PathVariable Long resourceId, 
                                      HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            boolean favorited = resourceService.isFavorited(userId, resourceId);
            return Result.success("查询成功", favorited);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户收藏的资源
     */
    @GetMapping("/favorites")
    public Result<List<LearningResource>> getUserFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            List<LearningResource> favorites = resourceService.getUserFavorites(userId, page, size);
            return Result.success("获取成功", favorites);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 下载资源
     */
    @PostMapping("/{resourceId}/download")
    public Result<String> downloadResource(@PathVariable Long resourceId) {
        try {
            // 增加下载次数
            resourceService.incrementDownloadCount(resourceId);
            
            // 这里可以添加实际的文件下载逻辑
            
            return Result.success("下载成功");
        } catch (Exception e) {
            return Result.error("下载失败：" + e.getMessage());
        }
    }

    // ========== 资源分类管理 ==========

    /**
     * 获取分类树结构
     */
    @GetMapping("/categories/tree")
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
    @GetMapping("/categories/{parentId}/children")
    public Result<List<ResourceCategory>> getSubCategories(@PathVariable Long parentId) {
        try {
            List<ResourceCategory> categories = resourceService.getSubCategories(parentId);
            return Result.success("获取成功", categories);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    // ========== 资源标签管理 ==========

    /**
     * 获取所有标签
     */
    @GetMapping("/tags")
    public Result<List<ResourceTag>> getAllTags() {
        try {
            List<ResourceTag> tags = resourceService.getAllTags();
            return Result.success("获取成功", tags);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取热门标签
     */
    @GetMapping("/tags/popular")
    public Result<List<ResourceTag>> getPopularTags(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<ResourceTag> tags = resourceService.getPopularTags(limit);
            return Result.success("获取成功", tags);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取资源的标签
     */
    @GetMapping("/{resourceId}/tags")
    public Result<List<ResourceTag>> getResourceTags(@PathVariable Long resourceId) {
        try {
            List<ResourceTag> tags = resourceService.getResourceTags(resourceId);
            return Result.success("获取成功", tags);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    // ========== 资源推荐功能 ==========

    /**
     * 获取用户推荐资源
     */
    @GetMapping("/recommendations")
    public Result<List<LearningResource>> getRecommendedResources(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            List<LearningResource> recommendations = resourceService.getRecommendedResources(userId, limit);
            return Result.success("获取成功", recommendations);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取相似资源
     */
    @GetMapping("/{resourceId}/similar")
    public Result<List<LearningResource>> getSimilarResources(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<LearningResource> similar = resourceService.getSimilarResources(resourceId, limit);
            return Result.success("获取成功", similar);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取个性化推荐
     */
    @GetMapping("/personalized")
    public Result<List<LearningResource>> getPersonalizedRecommendations(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            List<LearningResource> recommendations = resourceService.getPersonalizedRecommendations(userId, limit);
            return Result.success("获取成功", recommendations);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========

    /**
     * 从请求中获取当前用户ID
     * 这里需要根据实际的认证机制进行实现
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        // 从JWT token或Session中获取用户ID
        // 这里简单返回一个固定值，实际应用中需要从认证信息中获取
        String userId = request.getHeader("X-User-Id");
        return userId != null ? Long.valueOf(userId) : 1L;
    }
} 