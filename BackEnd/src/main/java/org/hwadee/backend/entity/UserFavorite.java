package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 用户收藏实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavorite {
    /**
     * 收藏ID
     */
    private Long favoriteId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 收藏类型
     */
    private String favoriteType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 资源信息（非数据库字段）
     */
    private LearningResource resource;

    // 收藏类型常量
    public static final String TYPE_RESOURCE = "resource";
} 