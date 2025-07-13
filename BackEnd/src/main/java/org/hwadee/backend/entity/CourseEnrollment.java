package org.hwadee.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollment {
    private Long enrollmentId;
    private Long courseId;
    private Long userId;
    private LocalDate enrollmentDate;
    private Integer status;
    private BigDecimal finalGrade;
    private BigDecimal creditsEarned;
    private LocalDateTime createTime;

    public static final int DROP = 0;
    public static final int LEARNING = 1;

    public static CourseEnrollment init(Course course, Long userId) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.courseId = course.getCourseId();
        courseEnrollment.userId = userId;
        courseEnrollment.status = LEARNING;
        courseEnrollment.finalGrade = BigDecimal.ZERO;
        courseEnrollment.creditsEarned = BigDecimal.ZERO;
        return courseEnrollment;
    }
}
