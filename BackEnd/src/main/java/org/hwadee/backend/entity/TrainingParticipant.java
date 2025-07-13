package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 培训项目参与者实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingParticipant {
    /**
     * 参与记录ID
     */
    private Long id;

    /**
     * 培训项目ID
     */
    private Long programId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 参与状态：0-已报名，1-已确认，2-已完成，3-已取消
     */
    private Integer status;

    /**
     * 报名时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollTime;

    /**
     * 确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    /**
     * 备注
     */
    private String remark;

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
    public static final int STATUS_ENROLLED = 0;
    public static final int STATUS_CONFIRMED = 1;
    public static final int STATUS_COMPLETED = 2;
    public static final int STATUS_CANCELLED = 3;
} 