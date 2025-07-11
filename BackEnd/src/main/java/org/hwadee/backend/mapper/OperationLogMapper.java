package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.OperationLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志Mapper接口
 */
@Mapper
public interface OperationLogMapper {

    /**
     * 插入操作日志
     */
    int insert(OperationLog log);

    /**
     * 根据ID查询
     */
    OperationLog selectById(Long logId);

    /**
     * 根据用户ID查询日志
     */
    List<OperationLog> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据操作类型查询
     */
    List<OperationLog> selectByOperationType(@Param("operationType") String operationType, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据时间范围查询
     */
    List<OperationLog> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询所有日志（分页）
     */
    List<OperationLog> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件查询日志
     */
    List<OperationLog> selectByConditions(@Param("userId") Long userId, 
                                         @Param("operationType") String operationType,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime,
                                         @Param("offset") int offset, 
                                         @Param("limit") int limit);

    /**
     * 统计日志数量
     */
    int countAll();

    /**
     * 根据条件统计
     */
    int countByConditions(@Param("userId") Long userId, 
                         @Param("operationType") String operationType,
                         @Param("startTime") LocalDateTime startTime,
                         @Param("endTime") LocalDateTime endTime);

    /**
     * 删除指定时间之前的日志
     */
    int deleteBeforeTime(@Param("time") LocalDateTime time);
} 