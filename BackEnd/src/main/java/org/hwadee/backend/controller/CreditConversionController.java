package org.hwadee.backend.controller;

import org.hwadee.backend.entity.CreditConversionRule;
import org.hwadee.backend.service.CreditConversionService;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 学分转换控制器
 */
@RestController
@RequestMapping("/credit/conversion")
public class CreditConversionController {

    private static final Logger logger = LoggerFactory.getLogger(CreditConversionController.class);

    @Autowired
    private CreditConversionService conversionService;

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
    @PostMapping("/execute")
    public Result<String> convertCredits(@RequestBody Map<String, Object> params) {
        try {
            Long userId = ((Number) params.get("userId")).longValue();
            String sourceType = (String) params.get("sourceType");
            String targetType = (String) params.get("targetType");
            BigDecimal sourceCredits = new BigDecimal(params.get("sourceCredits").toString());

            logger.info("执行学分转换，用户ID: {}, 源类型: {}, 目标类型: {}, 源学分: {}", 
                       userId, sourceType, targetType, sourceCredits);
                       
            return conversionService.convertCredits(userId, sourceType, targetType, sourceCredits);
        } catch (Exception e) {
            logger.error("执行学分转换时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 创建转换规则（管理员功能）
     */
    @PostMapping("/rule/create")
    public Result<String> createRule(@RequestBody CreditConversionRule rule) {
        try {
            logger.info("创建转换规则，源类型: {}, 目标类型: {}, 转换比例: {}", 
                      rule.getSourceType(), rule.getTargetType(), rule.getConversionRate());
                      
            return conversionService.createRule(rule);
        } catch (Exception e) {
            logger.error("创建转换规则时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 更新转换规则（管理员功能）
     */
    @PutMapping("/rule/update")
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