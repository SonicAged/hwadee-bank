package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.UserLearningTrack;

import java.util.List;
import java.util.Map;

/**
 * 用户学习轨迹Mapper接口
 */
@Mapper
public interface UserLearningTrackMapper {

    /**
     * 插入学习轨迹
     */
    int insert(UserLearningTrack track);

    /**
     * 根据轨迹ID查询学习轨迹
     */
    UserLearningTrack selectById(@Param("trackId") Long trackId);

    /**
     * 更新学习轨迹
     */
    int update(UserLearningTrack track);

    /**
     * 根据轨迹ID删除学习轨迹
     */
    int deleteById(@Param("trackId") Long trackId);

    /**
     * 根据用户ID查询学习轨迹
     */
    List<UserLearningTrack> selectByUserId(@Param("userId") Long userId, 
                                          @Param("offset") int offset, 
                                          @Param("limit") int limit);

    /**
     * 根据资源ID查询学习轨迹
     */
    List<UserLearningTrack> selectByResourceId(@Param("resourceId") Long resourceId, 
                                              @Param("offset") int offset, 
                                              @Param("limit") int limit);

    /**
     * 根据用户和资源查询学习轨迹
     */
    List<UserLearningTrack> selectByUserAndResource(@Param("userId") Long userId, 
                                                   @Param("resourceId") Long resourceId);

    /**
     * 根据行为类型查询学习轨迹
     */
    List<UserLearningTrack> selectByActionType(@Param("userId") Long userId, 
                                              @Param("actionType") String actionType,
                                              @Param("offset") int offset, 
                                              @Param("limit") int limit);

    /**
     * 统计用户学习时长
     */
    int getTotalLearningTime(@Param("userId") Long userId);

    /**
     * 统计用户学习资源数量
     */
    int countResourcesByUser(@Param("userId") Long userId);

    /**
     * 获取用户学习偏好分析
     */
    List<Map<String, Object>> getUserLearningPreferences(@Param("userId") Long userId);

    /**
     * 获取最近学习轨迹
     */
    List<UserLearningTrack> selectRecentTracks(@Param("userId") Long userId, 
                                              @Param("limit") int limit);

    /**
     * 统计用户行为类型分布
     */
    List<Map<String, Object>> getActionTypeStatistics(@Param("userId") Long userId);
} 