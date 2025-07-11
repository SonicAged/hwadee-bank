package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.CreditRecord;

import java.util.List;

/**
 * 学分记录Mapper接口
 */
@Mapper
public interface CreditRecordMapper {

    /**
     * 插入学分记录
     */
    int insert(CreditRecord record);

    /**
     * 根据用户ID查询学分记录
     */
    List<CreditRecord> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据用户ID统计学分记录数量
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 根据记录ID查询学分记录
     */
    CreditRecord selectByRecordId(@Param("recordId") Long recordId);

    /**
     * 更新学分记录状态
     */
    int updateStatus(@Param("recordId") Long recordId, @Param("status") Integer status);

    /**
     * 根据条件查询学分记录
     */
    List<CreditRecord> selectByCondition(@Param("userId") Long userId, @Param("creditType") String creditType, @Param("operationType") Integer operationType, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件统计学分记录数量
     */
    long countByCondition(@Param("userId") Long userId, @Param("creditType") String creditType, @Param("operationType") Integer operationType, @Param("status") Integer status);

    /**
     * 统计所有记录数量
     */
    long countAllRecords();

    /**
     * 根据用户ID和操作类型统计记录数量
     */
    long countByUserIdAndOperationType(@Param("userId") Long userId, @Param("operationType") Integer operationType);
} 