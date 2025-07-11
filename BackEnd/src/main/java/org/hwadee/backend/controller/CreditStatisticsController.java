package org.hwadee.backend.controller;

import org.hwadee.backend.mapper.CreditAccountMapper;
import org.hwadee.backend.mapper.CreditRecordMapper;
import org.hwadee.backend.mapper.CreditApplicationMapper;
import org.hwadee.backend.utils.JwtUtil;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    private CreditAccountMapper accountMapper;

    @Autowired
    private CreditRecordMapper recordMapper;

    @Autowired
    private CreditApplicationMapper applicationMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户的学分统计概览
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getUserOverview(HttpServletRequest request) {
        try {
            // 从token获取用户ID
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            Map<String, Object> overview = new HashMap<>();
            
            // 学分账户信息
            org.hwadee.backend.entity.CreditAccount account = accountMapper.selectByUserId(userId);
            if (account != null) {
                overview.put("totalCredits", account.getTotalCredits());
                overview.put("availableCredits", account.getAvailableCredits());
                overview.put("frozenCredits", account.getFrozenCredits());
            } else {
                overview.put("totalCredits", BigDecimal.ZERO);
                overview.put("availableCredits", BigDecimal.ZERO);
                overview.put("frozenCredits", BigDecimal.ZERO);
            }

            // 学分记录统计
            long totalRecords = recordMapper.countByUserId(userId);
            overview.put("totalRecords", totalRecords);

            // 申请统计
            int totalApplications = applicationMapper.countByUserId(userId);
            int pendingApplications = applicationMapper.countByUserIdAndStatus(userId, 1); // 待审核
            int approvedApplications = applicationMapper.countByUserIdAndStatus(userId, 3); // 已通过
            int rejectedApplications = applicationMapper.countByUserIdAndStatus(userId, 4); // 已拒绝
            
            overview.put("totalApplications", totalApplications);
            overview.put("pendingApplications", pendingApplications);
            overview.put("approvedApplications", approvedApplications);
            overview.put("rejectedApplications", rejectedApplications);

            return Result.success(overview);
        } catch (Exception e) {
            logger.error("获取用户统计概览时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取学分记录趋势统计
     */
    @GetMapping("/trend")
    public Result<Map<String, Object>> getCreditTrend(
            HttpServletRequest request,
            @RequestParam(defaultValue = "7") int days) {
        try {
            // 从token获取用户ID
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            Map<String, Object> trend = new HashMap<>();
            
            // 这里可以实现更复杂的趋势统计逻辑
            // 简化实现，返回基本统计
            long gainRecords = recordMapper.countByUserIdAndOperationType(userId, 1); // 获得
            long consumeRecords = recordMapper.countByUserIdAndOperationType(userId, 2); // 消费
            long convertRecords = recordMapper.countByUserIdAndOperationType(userId, 3); // 转换
            
            trend.put("gainRecords", gainRecords);
            trend.put("consumeRecords", consumeRecords);
            trend.put("convertRecords", convertRecords);
            trend.put("period", days + "天");

            return Result.success(trend);
        } catch (Exception e) {
            logger.error("获取学分趋势统计时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取学分类型分布统计
     */
    @GetMapping("/distribution")
    public Result<Map<String, Object>> getCreditDistribution(HttpServletRequest request) {
        try {
            // 从token获取用户ID
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            Map<String, Object> distribution = new HashMap<>();
            
            // 这里可以实现学分类型分布统计
            // 简化实现，返回模拟数据
            Map<String, Integer> typeDistribution = new HashMap<>();
            typeDistribution.put("学历教育", 60);
            typeDistribution.put("职业培训", 25);
            typeDistribution.put("技能证书", 10);
            typeDistribution.put("其他", 5);
            
            distribution.put("typeDistribution", typeDistribution);

            return Result.success(distribution);
        } catch (Exception e) {
            logger.error("获取学分分布统计时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取系统整体统计（管理员功能）
     */
    @GetMapping("/system")
    public Result<Map<String, Object>> getSystemStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 用户账户统计
            long totalAccounts = accountMapper.countAllAccounts();
            stats.put("totalAccounts", totalAccounts);

            // 学分记录统计
            long totalRecords = recordMapper.countAllRecords();
            stats.put("totalRecords", totalRecords);

            // 申请统计
            int totalApplications = applicationMapper.countAll();
            int pendingApplications = applicationMapper.countByStatus(1);
            int approvedApplications = applicationMapper.countByStatus(3);
            int rejectedApplications = applicationMapper.countByStatus(4);
            
            stats.put("totalApplications", totalApplications);
            stats.put("pendingApplications", pendingApplications);
            stats.put("approvedApplications", approvedApplications);
            stats.put("rejectedApplications", rejectedApplications);

            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取系统统计时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取用户排行榜
     */
    @GetMapping("/ranking")
    public Result<Map<String, Object>> getUserRanking(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Object> ranking = new HashMap<>();
            
            // 获取学分排行榜
            ranking.put("topUsers", accountMapper.selectTopUsersByCredits(limit));

            return Result.success(ranking);
        } catch (Exception e) {
            logger.error("获取用户排行榜时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 