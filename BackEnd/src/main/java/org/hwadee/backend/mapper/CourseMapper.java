package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.Course;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {

    /**
     * 插入课程
     */
    int insert(Course course);

    /**
     * 根据课程ID查询课程
     */
    Course selectByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据课程编码查询课程
     */
    Course selectByCourseCode(@Param("courseCode") String courseCode);

    /**
     * 更新课程
     */
    int update(Course course);

    /**
     * 根据课程ID删除课程
     */
    int deleteByCourseId(@Param("courseId") Long courseId);

    /**
     * 查询所有课程（分页）
     */
    List<Course> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件查询课程
     */
    List<Course> selectByCondition(@Param("courseName") String courseName, @Param("categoryId") Long categoryId, @Param("instructorId") Long instructorId, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件统计课程数量
     */
    long countByCondition(@Param("courseName") String courseName, @Param("categoryId") Long categoryId, @Param("instructorId") Long instructorId, @Param("status") Integer status);

    /**
     * 根据教师ID查询课程
     */
    List<Course> selectByInstructorId(@Param("instructorId") Long instructorId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 更新课程学生数
     */
    int updateCurrentStudents(@Param("courseId") Long courseId, @Param("currentStudents") Integer currentStudents);
} 