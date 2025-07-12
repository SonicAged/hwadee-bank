package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.UserFavorite;

import java.util.List;

/**
 * 用户收藏Mapper接口
 */
@Mapper
public interface UserFavoriteMapper {

    /**
     * 插入用户收藏
     */
    int insert(UserFavorite favorite);

    /**
     * 根据收藏ID查询用户收藏
     */
    UserFavorite selectById(@Param("favoriteId") Long favoriteId);

    /**
     * 根据收藏ID删除用户收藏
     */
    int deleteById(@Param("favoriteId") Long favoriteId);

    /**
     * 根据用户和资源删除收藏
     */
    int deleteByUserAndResource(@Param("userId") Long userId, 
                               @Param("resourceId") Long resourceId);

    /**
     * 根据用户ID查询收藏列表
     */
    List<UserFavorite> selectByUserId(@Param("userId") Long userId, 
                                     @Param("offset") int offset, 
                                     @Param("limit") int limit);

    /**
     * 检查用户是否已收藏资源
     */
    UserFavorite selectByUserAndResource(@Param("userId") Long userId, 
                                        @Param("resourceId") Long resourceId);

    /**
     * 统计用户收藏数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 统计资源被收藏数量
     */
    int countByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 获取用户收藏的资源类型分布
     */
    List<java.util.Map<String, Object>> getFavoriteTypeDistribution(@Param("userId") Long userId);

    /**
     * 获取最近收藏的资源
     */
    List<UserFavorite> selectRecentFavorites(@Param("userId") Long userId, 
                                           @Param("limit") int limit);
} 