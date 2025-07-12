package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.*;
import org.hwadee.backend.mapper.*;
import org.hwadee.backend.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源管理服务实现类
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private LearningResourceMapper learningResourceMapper;

    @Autowired
    private ResourceCategoryMapper resourceCategoryMapper;

    @Autowired
    private ResourceTagMapper resourceTagMapper;

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private ResourceRecommendationMapper resourceRecommendationMapper;

    @Autowired
    private UserLearningTrackMapper userLearningTrackMapper;

    // ========== 资源CRUD操作 ==========

    @Override
    public LearningResource createResource(LearningResource resource) {
        resource.setCreateTime(LocalDateTime.now());
        resource.setUpdateTime(LocalDateTime.now());
        resource.setStatus(LearningResource.UNDER_REVIEW); // 默认审核中
        resource.setViewCount(0);
        resource.setDownloadCount(0);
        resource.setFavoriteCount(0);
        resource.setRating(BigDecimal.ZERO);
        resource.setRatingCount(0);
        
        learningResourceMapper.insert(resource);
        return resource;
    }

    @Override
    public LearningResource getResourceById(Long resourceId) {
        return learningResourceMapper.selectByResourceId(resourceId);
    }

    @Override
    public LearningResource updateResource(LearningResource resource) {
        resource.setUpdateTime(LocalDateTime.now());
        learningResourceMapper.update(resource);
        return resource;
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        return learningResourceMapper.deleteByResourceId(resourceId) > 0;
    }

    @Override
    public boolean batchDeleteResources(List<Long> resourceIds) {
        int deletedCount = 0;
        for (Long resourceId : resourceIds) {
            deletedCount += learningResourceMapper.deleteByResourceId(resourceId);
        }
        return deletedCount == resourceIds.size();
    }

    // ========== 资源查询与分页 ==========

    @Override
    public PageResult<LearningResource> getResourcesByPage(int page, int size, String resourceName, 
                                                         String resourceType, Long categoryId, 
                                                         Integer difficultyLevel, Integer status) {
        int offset = (page - 1) * size;
        
        List<LearningResource> resources = learningResourceMapper.selectByCondition(
            resourceName, resourceType, categoryId, difficultyLevel, status, offset, size);
        
        long total = learningResourceMapper.countByCondition(
            resourceName, resourceType, categoryId, difficultyLevel, status);
        
        return new PageResult<>(resources, total, page, size);
    }

    @Override
    public List<LearningResource> getResourcesByCategory(Long categoryId, int page, int size) {
        int offset = (page - 1) * size;
        return learningResourceMapper.selectByCategoryId(categoryId, offset, size);
    }

    @Override
    public List<LearningResource> getResourcesByType(String resourceType, int page, int size) {
        int offset = (page - 1) * size;
        return learningResourceMapper.selectByCondition(null, resourceType, null, null, 
                                                       LearningResource.ON, offset, size);
    }

    @Override
    public List<LearningResource> getResourcesByDifficulty(Integer difficultyLevel, int page, int size) {
        int offset = (page - 1) * size;
        return learningResourceMapper.selectByCondition(null, null, null, difficultyLevel, 
                                                       LearningResource.ON, offset, size);
    }

    // ========== 资源搜索功能 ==========

    @Override
    public PageResult<LearningResource> searchResources(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        
        List<LearningResource> resources = learningResourceMapper.selectByCondition(
            keyword, null, null, null, LearningResource.ON, offset, size);
        
        long total = learningResourceMapper.countByCondition(
            keyword, null, null, null, LearningResource.ON);
        
        return new PageResult<>(resources, total, page, size);
    }

    @Override
    public PageResult<LearningResource> advancedSearchResources(String keyword, String resourceType, 
                                                              Long categoryId, Integer difficultyLevel, 
                                                              Double minRating, String tags, int page, int size) {
        int offset = (page - 1) * size;
        
        // 这里可以实现更复杂的搜索逻辑，包括标签过滤、评分过滤等
        List<LearningResource> resources = learningResourceMapper.selectByCondition(
            keyword, resourceType, categoryId, difficultyLevel, LearningResource.ON, offset, size);
        
        // 如果有评分要求，进行过滤
        if (minRating != null) {
            resources = resources.stream()
                .filter(r -> r.getRating().doubleValue() >= minRating)
                .collect(Collectors.toList());
        }
        
        long total = learningResourceMapper.countByCondition(
            keyword, resourceType, categoryId, difficultyLevel, LearningResource.ON);
        
        return new PageResult<>(resources, total, page, size);
    }

    @Override
    public List<LearningResource> getResourcesByTags(List<String> tags, int page, int size) {
        // 实现基于标签的资源查询
        // 这里需要根据标签进行复杂查询
        return new ArrayList<>();
    }

    // ========== 资源统计与分析 ==========

    @Override
    public List<LearningResource> getPopularResources(int limit) {
        return learningResourceMapper.selectAll(0, limit);
    }

    @Override
    public List<LearningResource> getLatestResources(int limit) {
        return learningResourceMapper.selectAll(0, limit);
    }

    @Override
    public List<LearningResource> getHighRatingResources(int limit) {
        return learningResourceMapper.selectAll(0, limit);
    }

    @Override
    public Map<String, Object> getResourceStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 统计总资源数
        long totalResources = learningResourceMapper.countByCondition(null, null, null, null, LearningResource.ON);
        statistics.put("totalResources", totalResources);
        
        // 统计各类型资源数量
        Map<String, Long> typeCount = new HashMap<>();
        typeCount.put("course", learningResourceMapper.countByCondition(null, "课程", null, null, LearningResource.ON));
        typeCount.put("material", learningResourceMapper.countByCondition(null, "教材", null, null, LearningResource.ON));
        typeCount.put("courseware", learningResourceMapper.countByCondition(null, "课件", null, null, LearningResource.ON));
        typeCount.put("project", learningResourceMapper.countByCondition(null, "实训项目", null, null, LearningResource.ON));
        statistics.put("typeCount", typeCount);
        
        return statistics;
    }

    @Override
    public List<Map<String, Object>> getCategoryStatistics() {
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        List<ResourceCategory> categories = resourceCategoryMapper.selectAll();
        
        for (ResourceCategory category : categories) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("categoryId", category.getCategoryId());
            stat.put("categoryName", category.getCategoryName());
            stat.put("resourceCount", resourceCategoryMapper.countResourcesByCategory(category.getCategoryId()));
            categoryStats.add(stat);
        }
        
        return categoryStats;
    }

    // ========== 资源交互操作 ==========

    @Override
    public void incrementViewCount(Long resourceId) {
        learningResourceMapper.updateViewCount(resourceId);
        
        // 记录用户学习轨迹
        // 这里可以添加学习轨迹记录逻辑
    }

    @Override
    public void incrementDownloadCount(Long resourceId) {
        learningResourceMapper.updateDownloadCount(resourceId);
    }

    @Override
    public boolean favoriteResource(Long userId, Long resourceId) {
        // 检查是否已收藏
        UserFavorite existing = userFavoriteMapper.selectByUserAndResource(userId, resourceId);
        if (existing != null) {
            return false;
        }
        
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setResourceId(resourceId);
        favorite.setFavoriteType(UserFavorite.TYPE_RESOURCE);
        favorite.setCreateTime(LocalDateTime.now());
        
        return userFavoriteMapper.insert(favorite) > 0;
    }

    @Override
    public boolean unfavoriteResource(Long userId, Long resourceId) {
        return userFavoriteMapper.deleteByUserAndResource(userId, resourceId) > 0;
    }

    @Override
    public boolean isFavorited(Long userId, Long resourceId) {
        return userFavoriteMapper.selectByUserAndResource(userId, resourceId) != null;
    }

    @Override
    public List<LearningResource> getUserFavorites(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        List<UserFavorite> favorites = userFavoriteMapper.selectByUserId(userId, offset, size);
        
        return favorites.stream()
            .map(favorite -> favorite.getResource())
            .collect(Collectors.toList());
    }

    // ========== 资源分类管理 ==========

    @Override
    public List<ResourceCategory> getCategoryTree() {
        return resourceCategoryMapper.selectTree();
    }

    @Override
    public List<ResourceCategory> getSubCategories(Long parentId) {
        return resourceCategoryMapper.selectByParentId(parentId);
    }

    @Override
    public ResourceCategory createCategory(ResourceCategory category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setStatus(ResourceCategory.STATUS_ENABLED);
        
        resourceCategoryMapper.insert(category);
        return category;
    }

    @Override
    public ResourceCategory updateCategory(ResourceCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        resourceCategoryMapper.update(category);
        return category;
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        return resourceCategoryMapper.deleteById(categoryId) > 0;
    }

    // ========== 资源标签管理 ==========

    @Override
    public List<ResourceTag> getAllTags() {
        return resourceTagMapper.selectAll();
    }

    @Override
    public List<ResourceTag> getPopularTags(int limit) {
        return resourceTagMapper.selectPopularTags(limit);
    }

    @Override
    public void addResourceTags(Long resourceId, List<Long> tagIds) {
        resourceTagMapper.batchAddResourceTags(resourceId, tagIds);
        
        // 更新标签使用次数
        for (Long tagId : tagIds) {
            resourceTagMapper.updateUseCount(tagId);
        }
    }

    @Override
    public void removeResourceTags(Long resourceId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            resourceTagMapper.removeResourceTag(resourceId, tagId);
        }
    }

    @Override
    public List<ResourceTag> getResourceTags(Long resourceId) {
        return resourceTagMapper.selectByResourceId(resourceId);
    }

    // ========== 资源推荐功能 ==========

    @Override
    public List<LearningResource> getRecommendedResources(Long userId, int limit) {
        List<ResourceRecommendation> recommendations = 
            resourceRecommendationMapper.selectByUserId(userId, 0, limit);
        
        return recommendations.stream()
            .map(rec -> rec.getResource())
            .collect(Collectors.toList());
    }

    @Override
    public List<LearningResource> getSimilarResources(Long resourceId, int limit) {
        LearningResource resource = learningResourceMapper.selectByResourceId(resourceId);
        if (resource == null) {
            return new ArrayList<>();
        }
        
        // 基于分类和标签查找相似资源
        return learningResourceMapper.selectByCondition(
            null, resource.getResourceType(), resource.getCategoryId(), 
            null, LearningResource.ON, 0, limit);
    }

    @Override
    public List<LearningResource> getPersonalizedRecommendations(Long userId, int limit) {
        // 基于用户学习历史的个性化推荐
        List<UserLearningTrack> tracks = userLearningTrackMapper.selectByUserId(userId, 0, 10);
        
        if (tracks.isEmpty()) {
            return getPopularResources(limit);
        }
        
        // 获取用户偏好的资源类型
        Map<String, Long> preferences = tracks.stream()
            .collect(Collectors.groupingBy(
                UserLearningTrack::getResourceType, 
                Collectors.counting()));
        
        // 根据偏好推荐资源
        String preferredType = preferences.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
        
        if (preferredType != null) {
            return learningResourceMapper.selectByCondition(
                null, preferredType, null, null, LearningResource.ON, 0, limit);
        }
        
        return getPopularResources(limit);
    }
} 