package org.hwadee.backend.service;

import org.hwadee.backend.entity.Course;
import org.hwadee.backend.entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 课程服务接口
 */
public interface CourseService {

    /**
     * 分页获取课程列表
     */
    PageResult<Course> getCourseList(Integer page, Integer size, String courseName, Long categoryId, Integer status);

    /**
     * 根据ID获取课程
     */
    Course getCourseById(Long courseId);

    /**
     * 创建课程
     */
    Course createCourse(Course course);

    /**
     * 更新课程
     */
    Course updateCourse(Course course);

    /**
     * 删除课程
     */
    void deleteCourse(Long courseId);

    /**
     * 报名课程
     */
    void enrollCourse(Long courseId, Long userId);

    /**
     * 退出课程
     */
    void withdrawCourse(Long courseId, Long userId);

    /**
     * 获取课程章节
     */
    List<Map<String, Object>> getCourseChapters(Long courseId);

    /**
     * 获取课程资源
     */
    PageResult<Map<String, Object>> getCourseResources(Long courseId, Integer page, Integer size, String type, String keyword);

    /**
     * 保存学习进度
     */
    void saveLearningProgress(Long userId, Long courseId, Long sectionId, Integer progress, Integer duration, Integer lastPosition);

    /**
     * 获取学习记录
     */
    List<Map<String, Object>> getLearningRecords(Long userId, Long courseId);

    /**
     * 获取课程测试
     */
    List<Map<String, Object>> getCourseTests(Long courseId, Long userId);

    /**
     * 获取测试详情
     */
    Map<String, Object> getTestDetail(Long testId, Long userId);

    /**
     * 提交测试
     */
    Map<String, Object> submitTest(Long testId, Long userId, List<Map<String, Object>> answers);

    /**
     * 保存笔记
     */
    void saveNote(Long userId, Long courseId, Long sectionId, String content);

    /**
     * 获取笔记
     */
    List<Map<String, Object>> getNotes(Long userId, Long courseId, Long sectionId);

    /**
     * 获取课程统计信息
     */
    Map<String, Object> getCourseStatistics(Long courseId);

    /**
     * 获取已报名课程
     */
    List<Map<String, Object>> getEnrolledCourses(Long userId);

    /**
     * 判断用户是否报名
     */
    Boolean checkUserInCourse(Long courseId, Long userId);
}