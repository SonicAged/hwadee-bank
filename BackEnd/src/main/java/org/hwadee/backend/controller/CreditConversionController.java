package org.hwadee.backend.controller;

import org.hwadee.backend.entity.CreditConversionRule;
import org.hwadee.backend.service.CreditConversionService;
import org.hwadee.backend.utils.JwtUtil;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 学分转换控制器
 */
@RestController
@RequestMapping("/credit/conversion")
public class CreditConversionController {

    private static final Logger logger = LoggerFactory.getLogger(CreditConversionController.class);

    @Autowired
    private CreditConversionService conversionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取所有启用的转换规则
     */
    @GetMapping("/rules")
    public Result<List<CreditConversionRule>> getActiveRules() {
        try {
            return conversionService.getActiveRules();
        } catch (Exception e) {
            logger.error("获取转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 根据源类型和目标类型获取转换规则
     */
    @GetMapping("/rule")
    public Result<CreditConversionRule> getRuleByTypes(
            @RequestParam String sourceType,
            @RequestParam String targetType) {
        try {
            return conversionService.getRuleByTypes(sourceType, targetType);
        } catch (Exception e) {
            logger.error("获取转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 计算转换后的学分
     */
    @GetMapping("/calculate")
    public Result<BigDecimal> calculateConvertedCredits(
            @RequestParam String sourceType,
            @RequestParam String targetType,
            @RequestParam BigDecimal sourceCredits) {
        try {
            return conversionService.calculateConvertedCredits(sourceType, targetType, sourceCredits);
        } catch (Exception e) {
            logger.error("计算转换学分时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 执行学分转换
     */
    @PostMapping("/convert")
    public Result<String> convertCredits(
            HttpServletRequest request,
            @RequestParam String sourceType,
            @RequestParam String targetType,
            @RequestParam BigDecimal sourceCredits) {
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

            logger.info("用户 {} 执行学分转换：{} {} -> {}", userId, sourceCredits, sourceType, targetType);
            return conversionService.convertCredits(userId, sourceType, targetType, sourceCredits);
        } catch (Exception e) {
            logger.error("执行学分转换时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 创建转换规则（管理员功能）
     */
    @PostMapping("/rule")
    public Result<String> createRule(@RequestBody CreditConversionRule rule) {
        try {
            logger.info("创建转换规则：{} -> {}, 比例: {}", rule.getSourceType(), rule.getTargetType(), rule.getConversionRate());
            return conversionService.createRule(rule);
        } catch (Exception e) {
            logger.error("创建转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 更新转换规则（管理员功能）
     */
    @PutMapping("/rule")
    public Result<String> updateRule(@RequestBody CreditConversionRule rule) {
        try {
            logger.info("更新转换规则，规则ID: {}", rule.getRuleId());
            return conversionService.updateRule(rule);
        } catch (Exception e) {
            logger.error("更新转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 删除转换规则（管理员功能）
     */
    @DeleteMapping("/rule/{ruleId}")
    public Result<String> deleteRule(@PathVariable Long ruleId) {
        try {
            logger.info("删除转换规则，规则ID: {}", ruleId);
            return conversionService.deleteRule(ruleId);
        } catch (Exception e) {
            logger.error("删除转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取所有转换规则（管理员功能）
     */
    @GetMapping("/rules/all")
    public Result<List<CreditConversionRule>> getAllRules(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return conversionService.getAllRules(page, size);
        } catch (Exception e) {
            logger.error("获取所有转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 