package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学分转换规则实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditConversionRule {
    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 源学分类型
     */
    private String sourceType;

    /**
     * 目标学分类型
     */
    private String targetType;

    /**
     * 转换比例
     */
    private BigDecimal conversionRate;

    /**
     * 最小转换学分
     */
    private BigDecimal minCredits;

    /**
     * 最大转换学分
     */
    private BigDecimal maxCredits;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 