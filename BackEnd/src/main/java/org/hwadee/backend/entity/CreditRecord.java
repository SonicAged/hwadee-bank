package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学分记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditRecord {
    /**
     * 记录ID
     */
    private Long recordId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 学分类型：学历教育、职业培训、技能证书等
     */
    private String creditType;

    /**
     * 学分来源
     */
    private String creditSource;

    /**
     * 学分数量
     */
    private BigDecimal creditAmount;

    /**
     * 操作类型：1-获得，2-消费，3-转换
     */
    private Integer operationType;

    /**
     * 状态：0-无效，1-有效，2-待审核
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 