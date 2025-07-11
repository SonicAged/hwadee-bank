package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 证书实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    /**
     * 证书ID
     */
    private Long certificateId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 证书类型
     */
    private String certificateType;

    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 证书编号
     */
    private String certificateNumber;

    /**
     * 学分值
     */
    private BigDecimal creditValue;

    /**
     * 颁发日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    /**
     * 过期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    /**
     * 证书文件URL
     */
    private String certificateUrl;

    /**
     * 状态：0-已撤销，1-有效，2-已过期
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 