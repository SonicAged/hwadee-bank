package org.hwadee.backend.service;

import org.hwadee.backend.entity.LearningResource;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.entity.ResourceCategory;
import org.hwadee.backend.entity.ResourceTag;

import java.util.List;
import java.util.Map;

/**
 * 资源管理服务接口
 */
public interface ResourceService {

    // ========== 资源CRUD操作 ==========
    
    /**
     * 创建学习资源
     */
    LearningResource createResource(LearningResource resource);

    /**
     * 根据ID获取学习资源
     */
    LearningResource getResourceById(Long resourceId);

    /**
     * 更新学习资源
     */
    LearningResource updateResource(LearningResource resource);

    /**
     * 删除学习资源
     */
    boolean deleteResource(Long resourceId);

    /**
     * 批量删除学习资源
     */
    boolean batchDeleteResources(List<Long> resourceIds);

    // ========== 资源查询与分页 ==========

    /**
     * 分页查询学习资源
     */
    PageResult<LearningResource> getResourcesByPage(int page, int size, String resourceName, 
                                                   String resourceType, Long categoryId, 
                                                   Integer difficultyLevel, Integer status);

    /**
     * 根据分类查询资源
     */
    List<LearningResource> getResourcesByCategory(Long categoryId, int page, int size);

    /**
     * 根据资源类型查询资源
     */
    List<LearningResource> getResourcesByType(String resourceType, int page, int size);

    /**
     * 根据难度级别查询资源
     */
    List<LearningResource> getResourcesByDifficulty(Integer difficultyLevel, int page, int size);

    // ========== 资源搜索功能 ==========

    /**
     * 关键词搜索资源
     */
    PageResult<LearningResource> searchResources(String keyword, int page, int size);

    /**
     * 高级搜索资源
     */
    PageResult<LearningResource> advancedSearchResources(String keyword, String resourceType, 
                                                       Long categoryId, Integer difficultyLevel, 
                                                       Double minRating, String tags, int page, int size);

    /**
     * 根据标签搜索资源
     */
    List<LearningResource> getResourcesByTags(List<String> tags, int page, int size);

    // ========== 资源统计与分析 ==========

    /**
     * 获取热门资源
     */
    List<LearningResource> getPopularResources(int limit);

    /**
     * 获取最新资源
     */
    List<LearningResource> getLatestResources(int limit);

    /**
     * 获取高评分资源
     */
    List<LearningResource> getHighRatingResources(int limit);

    /**
     * 获取资源统计信息
     */
    Map<String, Object> getResourceStatistics();

    /**
     * 获取分类统计信息
     */
    List<Map<String, Object>> getCategoryStatistics();

    // ========== 资源交互操作 ==========

    /**
     * 增加资源浏览次数
     */
    void incrementViewCount(Long resourceId);

    /**
     * 增加资源下载次数
     */
    void incrementDownloadCount(Long resourceId);

    /**
     * 收藏资源
     */
    boolean favoriteResource(Long userId, Long resourceId);

    /**
     * 取消收藏资源
     */
    boolean unfavoriteResource(Long userId, Long resourceId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Long resourceId);

    /**
     * 获取用户收藏的资源
     */
    List<LearningResource> getUserFavorites(Long userId, int page, int size);

    // ========== 资源分类管理 ==========

    /**
     * 获取分类树结构
     */
    List<ResourceCategory> getCategoryTree();

    /**
     * 根据父级ID获取子分类
     */
    List<ResourceCategory> getSubCategories(Long parentId);

    /**
     * 创建资源分类
     */
    ResourceCategory createCategory(ResourceCategory category);

    /**
     * 更新资源分类
     */
    ResourceCategory updateCategory(ResourceCategory category);

    /**
     * 删除资源分类
     */
    boolean deleteCategory(Long categoryId);

    // ========== 资源标签管理 ==========

    /**
     * 获取所有标签
     */
    List<ResourceTag> getAllTags();

    /**
     * 获取热门标签
     */
    List<ResourceTag> getPopularTags(int limit);

    /**
     * 为资源添加标签
     */
    void addResourceTags(Long resourceId, List<Long> tagIds);

    /**
     * 移除资源标签
     */
    void removeResourceTags(Long resourceId, List<Long> tagIds);

    /**
     * 获取资源的标签
     */
    List<ResourceTag> getResourceTags(Long resourceId);

    // ========== 资源推荐功能 ==========

    /**
     * 获取用户推荐资源
     */
    List<LearningResource> getRecommendedResources(Long userId, int limit);

    /**
     * 获取相似资源
     */
    List<LearningResource> getSimilarResources(Long resourceId, int limit);

    /**
     * 根据用户学习历史推荐资源
     */
    List<LearningResource> getPersonalizedRecommendations(Long userId, int limit);
} 