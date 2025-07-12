package org.hwadee.backend.controller;

import org.hwadee.backend.entity.Course;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.service.CourseService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程管理控制器
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 分页获取课程列表
     */
    @GetMapping("/page")
    public Result<PageResult<Course>> getCourseList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        
        PageResult<Course> result = courseService.getCourseList(page, size, courseName, categoryId, status);
        return Result.success(result);
    }

    /**
     * 获取课程详情
     */
    @GetMapping("/{courseId}")
    public Result<Course> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return Result.success(course);
    }

    /**
     * 创建课程
     */
    @PostMapping("/create")
    public Result<Course> createCourse(@RequestBody Course course) {
        Course newCourse = courseService.createCourse(course);
        return Result.success(newCourse);
    }

    /**
     * 更新课程
     */
    @PutMapping("/{courseId}")
    public Result<Course> updateCourse(@PathVariable Long courseId, @RequestBody Course course) {
        course.setCourseId(courseId);
        Course updatedCourse = courseService.updateCourse(course);
        return Result.success(updatedCourse);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{courseId}")
    public Result<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return Result.success();
    }

    /**
     * 报名课程
     */
    @PostMapping("/{courseId}/enroll")
    public Result<Void> enrollCourse(@PathVariable Long courseId) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        courseService.enrollCourse(courseId, userId);
        return Result.success();
    }

    /**
     * 退出课程
     */
    @PostMapping("/{courseId}/withdraw")
    public Result<Void> withdrawCourse(@PathVariable Long courseId) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        courseService.withdrawCourse(courseId, userId);
        return Result.success();
    }

    /**
     * 获取课程章节
     */
    @GetMapping("/{courseId}/chapters")
    public Result<List<Map<String, Object>>> getCourseChapters(@PathVariable Long courseId) {
        List<Map<String, Object>> chapters = courseService.getCourseChapters(courseId);
        return Result.success(chapters);
    }

    /**
     * 获取课程资源
     */
    @GetMapping("/{courseId}/resources")
    public Result<PageResult<Map<String, Object>>> getCourseResources(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {
        
        PageResult<Map<String, Object>> resources = courseService.getCourseResources(courseId, page, size, type, keyword);
        return Result.success(resources);
    }

    /**
     * 保存学习进度
     */
    @PostMapping("/learning/progress")
    public Result<Void> saveLearningProgress(@RequestBody Map<String, Object> data) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        Long courseId = Long.parseLong(data.get("courseId").toString());
        Long sectionId = Long.parseLong(data.get("sectionId").toString());
        Integer progress = Integer.parseInt(data.get("progress").toString());
        Integer duration = Integer.parseInt(data.get("duration").toString());
        Integer lastPosition = Integer.parseInt(data.get("lastPosition").toString());
        
        courseService.saveLearningProgress(userId, courseId, sectionId, progress, duration, lastPosition);
        return Result.success();
    }

    /**
     * 获取学习记录
     */
    @GetMapping("/{courseId}/learning/records")
    public Result<List<Map<String, Object>>> getLearningRecords(@PathVariable Long courseId) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        List<Map<String, Object>> records = courseService.getLearningRecords(userId, courseId);
        return Result.success(records);
    }

    /**
     * 获取课程测试
     */
    @GetMapping("/{courseId}/tests")
    public Result<List<Map<String, Object>>> getCourseTests(@PathVariable Long courseId) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        List<Map<String, Object>> tests = courseService.getCourseTests(courseId, userId);
        return Result.success(tests);
    }

    /**
     * 获取测试详情
     */
    @GetMapping("/tests/{testId}")
    public Result<Map<String, Object>> getTestDetail(@PathVariable Long testId) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        Map<String, Object> test = courseService.getTestDetail(testId, userId);
        return Result.success(test);
    }

    /**
     * 提交测试
     */
    @PostMapping("/tests/{testId}/submit")
    public Result<Map<String, Object>> submitTest(
            @PathVariable Long testId, 
            @RequestBody Map<String, Object> data) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        List<Map<String, Object>> answers = (List<Map<String, Object>>) data.get("answers");
        
        Map<String, Object> result = courseService.submitTest(testId, userId, answers);
        return Result.success(result);
    }

    /**
     * 保存笔记
     */
    @PostMapping("/notes/save")
    public Result<Void> saveNote(@RequestBody Map<String, Object> data) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        Long courseId = Long.parseLong(data.get("courseId").toString());
        Long sectionId = Long.parseLong(data.get("sectionId").toString());
        String content = (String) data.get("content");
        
        courseService.saveNote(userId, courseId, sectionId, content);
        return Result.success();
    }

    /**
     * 获取笔记
     */
    @GetMapping("/notes")
    public Result<List<Map<String, Object>>> getNotes(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long sectionId) {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        List<Map<String, Object>> notes = courseService.getNotes(userId, courseId, sectionId);
        return Result.success(notes);
    }

    /**
     * 获取课程统计信息
     */
    @GetMapping("/{courseId}/statistics")
    public Result<Map<String, Object>> getCourseStatistics(@PathVariable Long courseId) {
        Map<String, Object> statistics = courseService.getCourseStatistics(courseId);
        return Result.success(statistics);
    }

    /**
     * 获取已报名课程
     */
    @GetMapping("/enrolled")
    public Result<List<Map<String, Object>>> getEnrolledCourses() {
        // 这里需要从当前登录用户获取用户ID
        Long userId = 1L; // 模拟用户ID
        
        List<Map<String, Object>> courses = courseService.getEnrolledCourses(userId);
        return Result.success(courses);
    }
} 