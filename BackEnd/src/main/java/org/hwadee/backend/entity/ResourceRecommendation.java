package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资源推荐实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceRecommendation {
    /**
     * 推荐ID
     */
    private Long recommendationId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 推荐类型：collaborative-协同过滤，content-内容推荐，popular-热门推荐
     */
    private String recommendationType;

    /**
     * 推荐分数
     */
    private BigDecimal score;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 状态：0-失效，1-有效
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
     * 资源信息（非数据库字段）
     */
    private LearningResource resource;

    // 推荐类型常量
    public static final String TYPE_COLLABORATIVE = "collaborative";
    public static final String TYPE_CONTENT = "content";
    public static final String TYPE_POPULAR = "popular";

    // 状态常量
    public static final int STATUS_INVALID = 0;
    public static final int STATUS_VALID = 1;
} 