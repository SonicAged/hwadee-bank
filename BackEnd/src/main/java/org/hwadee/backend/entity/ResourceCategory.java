package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源分类实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceCategory {
    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父级分类ID，0表示顶级分类
     */
    private Long parentId;

    /**
     * 分类路径，用逗号分隔
     */
    private String categoryPath;

    /**
     * 分类层级
     */
    private Integer level;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 分类描述
     */
    private String description;

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

    /**
     * 子分类列表（非数据库字段）
     */
    private List<ResourceCategory> children;

    /**
     * 资源数量（非数据库字段）
     */
    private Integer resourceCount;

    // 状态常量
    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ENABLED = 1;
} 