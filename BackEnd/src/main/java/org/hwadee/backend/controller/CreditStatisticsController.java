package org.hwadee.backend.controller;

import org.hwadee.backend.service.CreditAccountService;
import org.hwadee.backend.service.CreditApplicationService;
import org.hwadee.backend.service.CreditRecordService;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 学分统计控制器
 */
@RestController
@RequestMapping("/credit/statistics")
public class CreditStatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(CreditStatisticsController.class);

    @Autowired
    private CreditAccountService accountService;

    @Autowired
    private CreditApplicationService applicationService;

    @Autowired
    private CreditRecordService recordService;

    /**
     * 获取统计概览
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview(@RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> overview = new HashMap<>();

            // 1. 账户统计数据
            if (userId != null) {
                // 获取指定用户的账户信息
                Result<?> accountResult = accountService.getAccountByUserId(userId);
                if (accountResult.isSuccess() && accountResult.getData() != null) {
                    org.hwadee.backend.entity.CreditAccount account = 
                        (org.hwadee.backend.entity.CreditAccount) accountResult.getData();
                    overview.put("totalCredits", account.getTotalCredits());
                    overview.put("availableCredits", account.getAvailableCredits());
                    overview.put("frozenCredits", account.getFrozenCredits());
                } else {
                    overview.put("totalCredits", 0);
                    overview.put("availableCredits", 0);
                    overview.put("frozenCredits", 0);
                }
                
                // 获取指定用户的记录数量
                Result<?> recordCountResult = recordService.getRecordCountByUserId(userId);
                if (recordCountResult.isSuccess()) {
                    overview.put("totalRecords", recordCountResult.getData());
                } else {
                    overview.put("totalRecords", 0);
                }
                
                // 获取指定用户的申请统计
                Result<?> totalApplicationResult = applicationService.getApplicationCountByUserId(userId);
                if (totalApplicationResult.isSuccess()) {
                    overview.put("totalApplications", totalApplicationResult.getData());
                } else {
                    overview.put("totalApplications", 0);
                }

                Result<?> pendingApplicationResult = 
                    applicationService.getApplicationCountByUserIdAndStatus(userId, 1); // 1=待审核
                if (pendingApplicationResult.isSuccess()) {
                    overview.put("pendingApplications", pendingApplicationResult.getData());
                } else {
                    overview.put("pendingApplications", 0);
                }

                Result<?> approvedApplicationResult = 
                    applicationService.getApplicationCountByUserIdAndStatus(userId, 3); // 3=通过
                if (approvedApplicationResult.isSuccess()) {
                    overview.put("approvedApplications", approvedApplicationResult.getData());
                } else {
                    overview.put("approvedApplications", 0);
                }

                Result<?> rejectedApplicationResult = 
                    applicationService.getApplicationCountByUserIdAndStatus(userId, 4); // 4=拒绝
                if (rejectedApplicationResult.isSuccess()) {
                    overview.put("rejectedApplications", rejectedApplicationResult.getData());
                } else {
                    overview.put("rejectedApplications", 0);
                }
                
            } else {
                // 获取系统整体统计数据（仅管理员可用）
                overview.put("totalCredits", accountService.getTotalCreditsInSystem().getData());
                overview.put("availableCredits", accountService.getTotalAvailableCredits().getData());
                overview.put("frozenCredits", accountService.getTotalFrozenCredits().getData());
                overview.put("totalRecords", recordService.getRecordCount().getData());
                overview.put("totalApplications", applicationService.getApplicationCount().getData());
                overview.put("pendingApplications", applicationService.getApplicationCountByStatus(1).getData()); // 1=待审核
                overview.put("approvedApplications", applicationService.getApplicationCountByStatus(3).getData()); // 3=通过
                overview.put("rejectedApplications", applicationService.getApplicationCountByStatus(4).getData()); // 4=拒绝
            }

            return Result.success(overview);
        } catch (Exception e) {
            logger.error("获取统计概览时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取操作趋势
     */
    @GetMapping("/trend")
    public Result<Map<String, Integer>> getTrend(@RequestParam(required = false) Long userId) {
        try {
            Map<String, Integer> trend = new HashMap<>();
            
            if (userId != null) {
                // 指定用户的学分记录统计
                Result<?> gainResult = recordService.getRecordCountByUserIdAndOperationType(userId, 1); // 1=获得
                if (gainResult.isSuccess()) {
                    trend.put("gainRecords", (Integer) gainResult.getData());
                } else {
                    trend.put("gainRecords", 0);
                }

                Result<?> consumeResult = recordService.getRecordCountByUserIdAndOperationType(userId, 2); // 2=消费
                if (consumeResult.isSuccess()) {
                    trend.put("consumeRecords", (Integer) consumeResult.getData());
                } else {
                    trend.put("consumeRecords", 0);
                }

                Result<?> convertResult = recordService.getRecordCountByUserIdAndOperationType(userId, 3); // 3=转换
                if (convertResult.isSuccess()) {
                    trend.put("convertRecords", (Integer) convertResult.getData());
                } else {
                    trend.put("convertRecords", 0);
                }
            } else {
                // 系统整体统计（管理员）
                trend.put("gainRecords", (Integer) recordService.getRecordCountByOperationType(1).getData()); // 1=获得
                trend.put("consumeRecords", (Integer) recordService.getRecordCountByOperationType(2).getData()); // 2=消费
                trend.put("convertRecords", (Integer) recordService.getRecordCountByOperationType(3).getData()); // 3=转换
            }

            return Result.success(trend);
        } catch (Exception e) {
            logger.error("获取操作趋势时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取学分分布
     */
    @GetMapping("/distribution")
    public Result<Map<String, Object>> getDistribution(@RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> distribution = new HashMap<>();
            
            Map<String, Object> typeDist = recordService.getCreditTypeDistribution(userId).getData();
            distribution.put("typeDistribution", typeDist);

            return Result.success(distribution);
        } catch (Exception e) {
            logger.error("获取学分分布时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 