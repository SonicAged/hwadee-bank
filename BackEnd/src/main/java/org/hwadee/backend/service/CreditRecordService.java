package org.hwadee.backend.service;

import org.hwadee.backend.entity.CreditRecord;
import org.hwadee.backend.utils.Result;

import java.util.List;
import java.util.Map;

/**
 * 学分记录服务接口
 */
public interface CreditRecordService {

    /**
     * 获取用户的学分记录
     */
    Result<List<CreditRecord>> getUserRecords(Long userId, int page, int size);

    /**
     * 搜索学分记录
     */
    Result<List<CreditRecord>> searchRecords(String creditType, Integer operationType, Integer status, int page, int size);

    /**
     * 创建学分记录
     */
    Result<String> createRecord(CreditRecord record);

    /**
     * 获取记录总数
     */
    Result<Integer> getRecordCount();

    /**
     * 获取用户记录总数
     */
    Result<Integer> getRecordCountByUserId(Long userId);

    /**
     * 获取指定类型操作的记录数
     */
    Result<Integer> getRecordCountByOperationType(Integer operationType);

    /**
     * 获取用户指定类型操作的记录数
     */
    Result<Integer> getRecordCountByUserIdAndOperationType(Long userId, Integer operationType);

    /**
     * 获取学分类型分布
     */
    Result<Map<String, Object>> getCreditTypeDistribution(Long userId);
} 