package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学分认证申请实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditApplication {
    /**
     * 申请ID
     */
    private Long applicationId;

    /**
     * 申请用户ID
     */
    private Long userId;

    /**
     * 申请类型
     */
    private String applicationType;

    /**
     * 成果名称
     */
    private String achievementName;

    /**
     * 成果描述
     */
    private String achievementDescription;

    /**
     * 申请学分
     */
    private BigDecimal appliedCredits;

    /**
     * 证明材料文件
     */
    private String evidenceFiles;

    /**
     * 当前审核步骤
     */
    private Integer currentStep;

    /**
     * 状态：1-待审核，2-审核中，3-通过，4-拒绝，5-撤回
     */
    private Integer status;

    /**
     * 拒绝原因
     */
    private String rejectionReason;

    /**
     * 批准学分
     */
    private BigDecimal approvedCredits;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    /**
     * 审核完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;
} 