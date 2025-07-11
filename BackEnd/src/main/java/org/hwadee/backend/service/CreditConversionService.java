package org.hwadee.backend.service;

import org.hwadee.backend.entity.CreditConversionRule;
import org.hwadee.backend.utils.Result;

import java.math.BigDecimal;
import java.util.List;

/**
 * 学分转换服务接口
 */
public interface CreditConversionService {

    /**
     * 获取所有启用的转换规则
     */
    Result<List<CreditConversionRule>> getActiveRules();

    /**
     * 根据源类型和目标类型获取转换规则
     */
    Result<CreditConversionRule> getRuleByTypes(String sourceType, String targetType);

    /**
     * 计算转换后的学分
     */
    Result<BigDecimal> calculateConvertedCredits(String sourceType, String targetType, BigDecimal sourceCredits);

    /**
     * 执行学分转换
     */
    Result<String> convertCredits(Long userId, String sourceType, String targetType, BigDecimal sourceCredits);

    /**
     * 创建转换规则（管理员功能）
     */
    Result<String> createRule(CreditConversionRule rule);

    /**
     * 更新转换规则（管理员功能）
     */
    Result<String> updateRule(CreditConversionRule rule);

    /**
     * 删除转换规则（管理员功能）
     */
    Result<String> deleteRule(Long ruleId);

    /**
     * 获取所有转换规则（管理员功能）
     */
    Result<List<CreditConversionRule>> getAllRules(int page, int size);
} 