package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.ResourceRecommendation;

import java.util.List;

/**
 * 资源推荐Mapper接口
 */
@Mapper
public interface ResourceRecommendationMapper {

    /**
     * 插入资源推荐
     */
    int insert(ResourceRecommendation recommendation);

    /**
     * 根据推荐ID查询资源推荐
     */
    ResourceRecommendation selectById(@Param("recommendationId") Long recommendationId);

    /**
     * 更新资源推荐
     */
    int update(ResourceRecommendation recommendation);

    /**
     * 根据推荐ID删除资源推荐
     */
    int deleteById(@Param("recommendationId") Long recommendationId);

    /**
     * 根据用户ID查询推荐列表
     */
    List<ResourceRecommendation> selectByUserId(@Param("userId") Long userId, 
                                               @Param("offset") int offset, 
                                               @Param("limit") int limit);

    /**
     * 根据推荐类型查询推荐列表
     */
    List<ResourceRecommendation> selectByType(@Param("userId") Long userId, 
                                             @Param("recommendationType") String recommendationType,
                                             @Param("offset") int offset, 
                                             @Param("limit") int limit);

    /**
     * 批量插入推荐
     */
    int batchInsert(@Param("recommendations") List<ResourceRecommendation> recommendations);

    /**
     * 删除用户的所有推荐
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 删除过期推荐
     */
    int deleteExpiredRecommendations(@Param("days") int days);

    /**
     * 检查是否已推荐
     */
    ResourceRecommendation selectByUserAndResource(@Param("userId") Long userId, 
                                                  @Param("resourceId") Long resourceId);

    /**
     * 获取热门推荐
     */
    List<ResourceRecommendation> selectPopularRecommendations(@Param("limit") int limit);

    /**
     * 更新推荐状态
     */
    int updateStatus(@Param("recommendationId") Long recommendationId, 
                    @Param("status") Integer status);
} 