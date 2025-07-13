package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.CourseEnrollment;

public interface CourseEnrollmentMapper {
    /**
     * 查看用户是否报名课程，返回查询到的条目数量
     */
    Long checkUserInCourse(Long courseId, Long userId);

    /**
     * 新增一条选课报名记录
     */
    int insertEnrollment(CourseEnrollment enrollment);

    /**
     * 根据课程ID和用户ID删除报名记录
     */
    int deleteEnrollmentByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);
}