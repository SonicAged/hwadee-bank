package org.hwadee.backend.service;

import org.hwadee.backend.entity.CreditApplication;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;

import java.util.List;

/**
 * 学分申请服务接口
 */
public interface CreditApplicationService {

    /**
     * 提交学分申请
     */
    Result<String> submitApplication(CreditApplication application);

    /**
     * 获取申请详情
     */
    Result<CreditApplication> getApplicationById(Long applicationId);

    /**
     * 获取用户的申请列表
     */
    Result<List<CreditApplication>> getUserApplications(Long userId, int page, int size);

    /**
     * 获取所有申请（管理员）
     */
    Result<List<CreditApplication>> getAllApplications(int page, int size);

    /**
     * 根据状态获取申请列表
     */
    Result<List<CreditApplication>> getApplicationsByStatus(Integer status, int page, int size);

    /**
     * 审核申请
     */
    Result<String> reviewApplication(Long applicationId, Integer status, String reviewComment, Long reviewerId);

    /**
     * 更新申请
     */
    Result<String> updateApplication(CreditApplication application);

    /**
     * 删除申请
     */
    Result<String> deleteApplication(Long applicationId);

    /**
     * 搜索申请
     */
    Result<List<CreditApplication>> searchApplications(Long userId, String applicationType, String achievementName, Integer status, int page, int size);

    /**
     * 分页获取申请列表
     */
    Result<PageResult<CreditApplication>> getApplicationListWithPaging(Integer status, String applicationType, String achievementName, int page, int size);
    
    /**
     * 获取申请总数
     */
    Result<Integer> getApplicationCount();

    /**
     * 根据状态获取申请总数
     */
    Result<Integer> getApplicationCountByStatus(Integer status);
    
    /**
     * 获取用户申请总数
     */
    Result<Integer> getApplicationCountByUserId(Long userId);
    
    /**
     * 获取用户指定状态的申请总数
     */
    Result<Integer> getApplicationCountByUserIdAndStatus(Long userId, Integer status);
} 