package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学习资源实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningResource {
    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型：课程、教材、课件、实训项目
     */
    private String resourceType;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 学科领域
     */
    private String subject;

    /**
     * 关键词，用逗号分隔
     */
    private String keywords;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源内容URL
     */
    private String contentUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 时长(分钟)
     */
    private Integer duration;

    /**
     * 难度级别：1-初级，2-中级，3-高级
     */
    private Integer difficultyLevel;

    /**
     * 学分价值
     */
    private BigDecimal creditValue;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 收藏次数
     */
    private Integer favoriteCount;

    /**
     * 评分(0-5)
     */
    private BigDecimal rating;

    /**
     * 评分人数
     */
    private Integer ratingCount;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 标签，用逗号分隔
     */
    private String tags;

    /**
     * 前置要求
     */
    private String prerequisites;

    /**
     * 学习目标
     */
    private String learningObjectives;

    /**
     * 状态：0-下架，1-上架，2-审核中
     */
    private Integer status;

    public static final int OFF = 0;
    public static final int ON = 1;
    public static final int UNDER_REVIEW = 2;

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