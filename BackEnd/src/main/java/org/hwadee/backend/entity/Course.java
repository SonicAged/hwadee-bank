package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 课程实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程编码
     */
    private String courseCode;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 授课教师ID
     */
    private Long instructorId;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 课程大纲
     */
    private String syllabus;

    /**
     * 学时
     */
    private Integer creditHours;

    /**
     * 学分值
     */
    private BigDecimal creditValue;

    /**
     * 最大学生数
     */
    private Integer maxStudents;

    /**
     * 当前学生数
     */
    private Integer currentStudents;

    /**
     * 开课日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 结课日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 状态：0-关闭，1-开放，2-满员
     */
    private Integer status;

    public static final int CLOSED = 0;
    public static final int OPEN = 1;
    public static final int FULL = 2;

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