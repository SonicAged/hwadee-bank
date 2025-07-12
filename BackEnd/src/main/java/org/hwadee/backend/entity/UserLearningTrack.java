package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户学习轨迹实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLearningTrack {
    /**
     * 轨迹ID
     */
    private Long trackId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 行为类型：view-浏览，download-下载，favorite-收藏，complete-完成
     */
    private String actionType;

    /**
     * 学习进度百分比
     */
    private Integer progress;

    /**
     * 学习时长(分钟)
     */
    private Integer durationMinutes;

    /**
     * 学习得分
     */
    private BigDecimal score;

    /**
     * 状态：0-删除，1-正常
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
     * 资源名称（非数据库字段）
     */
    private String resourceName;

    /**
     * 资源类型（非数据库字段）
     */
    private String resourceType;

    // 行为类型常量
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_DOWNLOAD = "download";
    public static final String ACTION_FAVORITE = "favorite";
    public static final String ACTION_COMPLETE = "complete";

    // 状态常量
    public static final int STATUS_DELETED = 0;
    public static final int STATUS_NORMAL = 1;
} 