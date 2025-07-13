 package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 培训项目实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgram {
    /**
     * 培训项目ID
     */
    private Long programId;

    /**
     * 培训项目名称
     */
    private String programName;

    /**
     * 培训项目编码
     */
    private String programCode;

    /**
     * 培训项目类型：1-线上培训，2-线下培训，3-混合培训
     */
    private Integer programType;

    /**
     * 培训负责人ID
     */
    private Long managerId;

    /**
     * 培训项目描述
     */
    private String description;

    /**
     * 培训内容大纲
     */
    private String content;

    /**
     * 学分值
     */
    private BigDecimal creditValue;

    /**
     * 培训费用
     */
    private BigDecimal cost;

    /**
     * 最大参与人数
     */
    private Integer maxParticipants;

    /**
     * 当前参与人数
     */
    private Integer currentParticipants;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 地点（线下或混合培训）
     */
    private String location;

    /**
     * 状态：0-未开始，1-进行中，2-已结束，3-已取消
     */
    private Integer status;

    /**
     * 报名截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollDeadline;

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
    
    // 状态常量
    public static final int STATUS_NOT_STARTED = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_COMPLETED = 2;
    public static final int STATUS_CANCELLED = 3;
    
    // 培训类型常量
    public static final int TYPE_ONLINE = 1;
    public static final int TYPE_OFFLINE = 2;
    public static final int TYPE_HYBRID = 3;
}