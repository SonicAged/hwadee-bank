package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.CreditConversionRule;

import java.util.List;

/**
 * 学分转换规则Mapper接口
 */
@Mapper
public interface CreditConversionRuleMapper {

    /**
     * 插入转换规则
     */
    int insert(CreditConversionRule rule);

    /**
     * 根据ID查询转换规则
     */
    CreditConversionRule selectById(@Param("ruleId") Long ruleId);

    /**
     * 查询所有启用的转换规则
     */
    List<CreditConversionRule> selectActiveRules();

    /**
     * 根据源类型和目标类型查询转换规则
     */
    CreditConversionRule selectByTypes(@Param("sourceType") String sourceType, @Param("targetType") String targetType);

    /**
     * 查询所有转换规则
     */
    List<CreditConversionRule> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 更新转换规则
     */
    int update(CreditConversionRule rule);

    /**
     * 更新规则状态
     */
    int updateStatus(@Param("ruleId") Long ruleId, @Param("status") Integer status);

    /**
     * 删除转换规则
     */
    int deleteById(@Param("ruleId") Long ruleId);

    /**
     * 统计转换规则数量
     */
    long countAll();
} 