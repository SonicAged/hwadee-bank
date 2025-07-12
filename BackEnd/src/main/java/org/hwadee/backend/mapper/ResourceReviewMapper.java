package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.ResourceReview;

import java.util.List;

/**
 * 资源评价Mapper接口
 */
@Mapper
public interface ResourceReviewMapper {

    /**
     * 插入资源评价
     */
    int insert(ResourceReview review);

    /**
     * 根据评价ID查询资源评价
     */
    ResourceReview selectById(@Param("reviewId") Long reviewId);

    /**
     * 更新资源评价
     */
    int update(ResourceReview review);

    /**
     * 根据评价ID删除资源评价
     */
    int deleteById(@Param("reviewId") Long reviewId);

    /**
     * 根据资源ID查询评价列表
     */
    List<ResourceReview> selectByResourceId(@Param("resourceId") Long resourceId, 
                                           @Param("offset") int offset, 
                                           @Param("limit") int limit);

    /**
     * 根据用户ID查询评价列表
     */
    List<ResourceReview> selectByUserId(@Param("userId") Long userId, 
                                       @Param("offset") int offset, 
                                       @Param("limit") int limit);

    /**
     * 检查用户是否已评价资源
     */
    ResourceReview selectByUserAndResource(@Param("userId") Long userId, 
                                          @Param("resourceId") Long resourceId);

    /**
     * 统计资源评价数量
     */
    int countByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 计算资源平均评分
     */
    Double calculateAverageRating(@Param("resourceId") Long resourceId);

    /**
     * 更新评价有用数
     */
    int updateHelpfulCount(@Param("reviewId") Long reviewId);

    /**
     * 根据评分查询评价
     */
    List<ResourceReview> selectByRating(@Param("resourceId") Long resourceId, 
                                       @Param("rating") Integer rating,
                                       @Param("offset") int offset, 
                                       @Param("limit") int limit);
} 