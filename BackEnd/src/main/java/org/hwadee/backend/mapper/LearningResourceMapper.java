package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.LearningResource;

import java.util.List;

/**
 * 学习资源Mapper接口
 */
@Mapper
public interface LearningResourceMapper {

    /**
     * 插入学习资源
     */
    int insert(LearningResource resource);

    /**
     * 根据资源ID查询学习资源
     */
    LearningResource selectByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 更新学习资源
     */
    int update(LearningResource resource);

    /**
     * 根据资源ID删除学习资源
     */
    int deleteByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 查询所有学习资源（分页）
     */
    List<LearningResource> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件查询学习资源
     */
    List<LearningResource> selectByCondition(@Param("resourceName") String resourceName, @Param("resourceType") String resourceType, @Param("categoryId") Long categoryId, @Param("difficultyLevel") Integer difficultyLevel, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件统计学习资源数量
     */
    long countByCondition(@Param("resourceName") String resourceName, @Param("resourceType") String resourceType, @Param("categoryId") Long categoryId, @Param("difficultyLevel") Integer difficultyLevel, @Param("status") Integer status);

    /**
     * 根据分类ID查询学习资源
     */
    List<LearningResource> selectByCategoryId(@Param("categoryId") Long categoryId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 更新浏览次数
     */
    int updateViewCount(@Param("resourceId") Long resourceId);

    /**
     * 更新下载次数
     */
    int updateDownloadCount(@Param("resourceId") Long resourceId);

    /**
     * 更新评分
     */
    int updateRating(@Param("resourceId") Long resourceId, @Param("rating") Double rating, @Param("ratingCount") Integer ratingCount);
} 