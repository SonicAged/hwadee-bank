package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 资源评价实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceReview {
    /**
     * 评价ID
     */
    private Long reviewId;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评分(1-5)
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String reviewContent;

    /**
     * 有用数
     */
    private Integer helpfulCount;

    /**
     * 状态：0-隐藏，1-显示
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

    /**
     * 用户名（非数据库字段）
     */
    private String username;

    /**
     * 用户真实姓名（非数据库字段）
     */
    private String realName;

    /**
     * 资源名称（非数据库字段）
     */
    private String resourceName;

    // 状态常量
    public static final int STATUS_HIDDEN = 0;
    public static final int STATUS_VISIBLE = 1;
} 