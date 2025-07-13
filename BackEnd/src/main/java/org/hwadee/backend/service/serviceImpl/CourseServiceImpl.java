package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.Course;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.mapper.CourseMapper;
import org.hwadee.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageResult<Course> getCourseList(Integer page, Integer size, String courseName, Long categoryId, Integer status) {
        // 计算分页参数
        int offset = (page - 1) * size;
        int limit = size;

        // 查询课程列表
        List<Course> list = courseMapper.selectByCondition(courseName, categoryId, null, status, offset, limit);
        
        // 统计总数
        long total = courseMapper.countByCondition(courseName, categoryId, null, status);
        
        // 返回分页结果
        return new PageResult<>(list, total, page, size);
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseMapper.selectByCourseId(courseId);
    }

    @Override
    @Transactional
    public Course createCourse(Course course) {
        // 设置课程状态和学生数
        course.setStatus(Course.OPEN);
        course.setCurrentStudents(0);
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        course.setCreateTime(now);
        course.setUpdateTime(now);
        
        // 插入课程
        courseMapper.insert(course);
        
        return course;
    }

    @Override
    @Transactional
    public Course updateCourse(Course course) {
        // 设置更新时间
        course.setUpdateTime(LocalDateTime.now());
        
        // 更新课程
        courseMapper.update(course);
        
        return courseMapper.selectByCourseId(course.getCourseId());
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        courseMapper.deleteByCourseId(courseId);
    }

    @Override
    @Transactional
    public void enrollCourse(Long courseId, Long userId) {
        // 查询课程信息
        Course course = courseMapper.selectByCourseId(courseId);
        
        // 检查课程状态和人数
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        
        if (course.getStatus() != Course.OPEN) {
            throw new RuntimeException("课程已关闭或已满员，无法报名");
        }
        
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new RuntimeException("课程已满员，无法报名");
        }
        
        // TODO: 检查用户是否已报名
        
        // 更新课程学生数
        int newCurrentStudents = course.getCurrentStudents() + 1;
        courseMapper.updateCurrentStudents(courseId, newCurrentStudents);
        
        // TODO: 添加用户报名记录
        
        // 如果报名后达到最大人数，更新课程状态为满员
        if (newCurrentStudents >= course.getMaxStudents()) {
            Course updateCourse = new Course();
            updateCourse.setCourseId(courseId);
            updateCourse.setStatus(Course.FULL);
            updateCourse.setUpdateTime(LocalDateTime.now());
            courseMapper.update(updateCourse);
        }
    }

    @Override
    @Transactional
    public void withdrawCourse(Long courseId, Long userId) {
        // 查询课程信息
        Course course = courseMapper.selectByCourseId(courseId);
        
        // 检查课程是否存在
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        
        // TODO: 检查用户是否已报名
        
        // 更新课程学生数
        int newCurrentStudents = Math.max(0, course.getCurrentStudents() - 1);
        courseMapper.updateCurrentStudents(courseId, newCurrentStudents);
        
        // TODO: 删除用户报名记录
        
        // 如果退出后人数未满，更新课程状态为开放
        if (course.getStatus() == Course.FULL && newCurrentStudents < course.getMaxStudents()) {
            Course updateCourse = new Course();
            updateCourse.setCourseId(courseId);
            updateCourse.setStatus(Course.OPEN);
            updateCourse.setUpdateTime(LocalDateTime.now());
            courseMapper.update(updateCourse);
        }
    }

    @Override
    public List<Map<String, Object>> getCourseChapters(Long courseId) {
        // TODO: 实现从数据库获取课程章节
        
        // 模拟数据，实际开发中应该从数据库获取
        List<Map<String, Object>> chapters = new ArrayList<>();
        
        // 模拟第一章
        Map<String, Object> chapter1 = new HashMap<>();
        chapter1.put("chapterId", 1L);
        chapter1.put("courseId", courseId);
        chapter1.put("chapterName", "第一章：Java 基础语法");
        chapter1.put("chapterOrder", 1);
        
        List<Map<String, Object>> sections1 = new ArrayList<>();
        
        Map<String, Object> section1_1 = new HashMap<>();
        section1_1.put("sectionId", 101L);
        section1_1.put("chapterId", 1L);
        section1_1.put("sectionName", "1.1 Java简介与环境配置");
        section1_1.put("resourceType", "video");
        section1_1.put("contentUrl", "https://example.com/videos/java-intro.mp4");
        section1_1.put("duration", 15 * 60);
        section1_1.put("status", "completed");
        section1_1.put("progress", 100);
        sections1.add(section1_1);
        
        Map<String, Object> section1_2 = new HashMap<>();
        section1_2.put("sectionId", 102L);
        section1_2.put("chapterId", 1L);
        section1_2.put("sectionName", "1.2 变量与数据类型");
        section1_2.put("resourceType", "video");
        section1_2.put("contentUrl", "https://example.com/videos/java-variables.mp4");
        section1_2.put("duration", 20 * 60);
        section1_2.put("status", "in_progress");
        section1_2.put("progress", 60);
        sections1.add(section1_2);
        
        Map<String, Object> section1_3 = new HashMap<>();
        section1_3.put("sectionId", 103L);
        section1_3.put("chapterId", 1L);
        section1_3.put("sectionName", "1.3 Java语法练习题");
        section1_3.put("resourceType", "quiz");
        section1_3.put("contentUrl", "https://example.com/quizzes/java-syntax.json");
        section1_3.put("status", "not_started");
        section1_3.put("progress", 0);
        sections1.add(section1_3);
        
        chapter1.put("sections", sections1);
        chapters.add(chapter1);
        
        // 模拟第二章
        Map<String, Object> chapter2 = new HashMap<>();
        chapter2.put("chapterId", 2L);
        chapter2.put("courseId", courseId);
        chapter2.put("chapterName", "第二章：面向对象编程");
        chapter2.put("chapterOrder", 2);
        
        List<Map<String, Object>> sections2 = new ArrayList<>();
        
        Map<String, Object> section2_1 = new HashMap<>();
        section2_1.put("sectionId", 201L);
        section2_1.put("chapterId", 2L);
        section2_1.put("sectionName", "2.1 类与对象");
        section2_1.put("resourceType", "video");
        section2_1.put("contentUrl", "https://example.com/videos/oop-classes.mp4");
        section2_1.put("duration", 25 * 60);
        section2_1.put("status", "not_started");
        section2_1.put("progress", 0);
        sections2.add(section2_1);
        
        Map<String, Object> section2_2 = new HashMap<>();
        section2_2.put("sectionId", 202L);
        section2_2.put("chapterId", 2L);
        section2_2.put("sectionName", "2.2 面向对象编程指南");
        section2_2.put("resourceType", "document");
        section2_2.put("contentUrl", "https://example.com/docs/oop-guide.pdf");
        section2_2.put("status", "not_started");
        section2_2.put("progress", 0);
        sections2.add(section2_2);
        
        chapter2.put("sections", sections2);
        chapters.add(chapter2);
        
        return chapters;
    }

    @Override
    public PageResult<Map<String, Object>> getCourseResources(Long courseId, Integer page, Integer size, String type, String keyword) {
        // TODO: 实现从数据库获取课程资源
        
        // 模拟数据，实际开发中应该从数据库获取
        List<Map<String, Object>> resources = new ArrayList<>();
        
        Map<String, Object> resource1 = new HashMap<>();
        resource1.put("resourceId", 1L);
        resource1.put("resourceName", "Java基础入门视频");
        resource1.put("resourceType", "video");
        resource1.put("fileSize", 256 * 1024 * 1024);
        resource1.put("duration", 45);
        resource1.put("downloadCount", 120);
        resources.add(resource1);
        
        Map<String, Object> resource2 = new HashMap<>();
        resource2.put("resourceId", 2L);
        resource2.put("resourceName", "面向对象编程指南");
        resource2.put("resourceType", "document");
        resource2.put("fileSize", 2.5 * 1024 * 1024);
        resource2.put("downloadCount", 89);
        resources.add(resource2);
        
        Map<String, Object> resource3 = new HashMap<>();
        resource3.put("resourceId", 3L);
        resource3.put("resourceName", "Java高级特性课件");
        resource3.put("resourceType", "ppt");
        resource3.put("fileSize", 5.8 * 1024 * 1024);
        resource3.put("downloadCount", 65);
        resources.add(resource3);
        
        // 模拟过滤
        if (type != null && !type.equals("all")) {
            resources.removeIf(r -> !r.get("resourceType").equals(type));
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            resources.removeIf(r -> !((String) r.get("resourceName")).toLowerCase().contains(keyword.toLowerCase()));
        }
        
        // 模拟分页
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, resources.size());
        
        List<Map<String, Object>> pagedResources;
        if (startIndex < resources.size()) {
            pagedResources = resources.subList(startIndex, endIndex);
        } else {
            pagedResources = new ArrayList<>();
        }
        
        return new PageResult<>(pagedResources, resources.size(), page, size);
    }

    @Override
    @Transactional
    public void saveLearningProgress(Long userId, Long courseId, Long sectionId, Integer progress, Integer duration, Integer lastPosition) {
        // TODO: 实现保存学习进度的逻辑
        System.out.println("保存学习进度: 用户ID=" + userId + ", 课程ID=" + courseId + 
                          ", 章节ID=" + sectionId + ", 进度=" + progress + 
                          ", 时长=" + duration + ", 最后位置=" + lastPosition);
    }

    @Override
    public List<Map<String, Object>> getLearningRecords(Long userId, Long courseId) {
        // TODO: 实现从数据库获取学习记录
        
        // 模拟数据，实际开发中应该从数据库获取
        List<Map<String, Object>> records = new ArrayList<>();
        
        Map<String, Object> record1 = new HashMap<>();
        record1.put("recordId", 1L);
        record1.put("userId", userId);
        record1.put("courseId", courseId);
        record1.put("sectionId", 101L);
        record1.put("progress", 100);
        record1.put("duration", 15 * 60);
        record1.put("lastPosition", 900);
        record1.put("createTime", LocalDateTime.now().minusDays(3));
        record1.put("updateTime", LocalDateTime.now().minusDays(2));
        records.add(record1);
        
        Map<String, Object> record2 = new HashMap<>();
        record2.put("recordId", 2L);
        record2.put("userId", userId);
        record2.put("courseId", courseId);
        record2.put("sectionId", 102L);
        record2.put("progress", 60);
        record2.put("duration", 12 * 60);
        record2.put("lastPosition", 720);
        record2.put("createTime", LocalDateTime.now().minusDays(1));
        record2.put("updateTime", LocalDateTime.now().minusHours(5));
        records.add(record2);
        
        return records;
    }

    @Override
    public List<Map<String, Object>> getCourseTests(Long courseId, Long userId) {
        // TODO: 实现从数据库获取课程测试
        
        // 模拟数据，实际开发中应该从数据库获取
        List<Map<String, Object>> tests = new ArrayList<>();
        
        Map<String, Object> test1 = new HashMap<>();
        test1.put("testId", 1L);
        test1.put("testName", "Java基础语法测验");
        test1.put("testType", "quiz");
        test1.put("courseId", courseId);
        test1.put("startTime", "2024-05-01 00:00:00");
        test1.put("endTime", "2024-06-30 23:59:59");
        test1.put("duration", 30);
        test1.put("questionCount", 10);
        test1.put("totalScore", 100);
        test1.put("status", "not_started");
        tests.add(test1);
        
        Map<String, Object> test2 = new HashMap<>();
        test2.put("testId", 2L);
        test2.put("testName", "面向对象编程作业");
        test2.put("testType", "assignment");
        test2.put("courseId", courseId);
        test2.put("startTime", "2024-05-01 00:00:00");
        test2.put("endTime", "2024-05-15 23:59:59");
        test2.put("duration", 120);
        test2.put("questionCount", 5);
        test2.put("totalScore", 50);
        test2.put("status", "completed");
        test2.put("score", 45);
        tests.add(test2);
        
        Map<String, Object> test3 = new HashMap<>();
        test3.put("testId", 3L);
        test3.put("testName", "Java期末考试");
        test3.put("testType", "exam");
        test3.put("courseId", courseId);
        test3.put("startTime", "2024-06-20 09:00:00");
        test3.put("endTime", "2024-06-20 12:00:00");
        test3.put("duration", 180);
        test3.put("questionCount", 30);
        test3.put("totalScore", 150);
        test3.put("status", "not_started");
        tests.add(test3);
        
        return tests;
    }

    @Override
    public Map<String, Object> getTestDetail(Long testId, Long userId) {
        // TODO: 实现从数据库获取测试详情
        
        // 模拟数据，实际开发中应该从数据库获取
        Map<String, Object> test = new HashMap<>();
        test.put("testId", testId);
        test.put("testName", "Java基础语法测验");
        test.put("testType", "quiz");
        test.put("courseId", 1L);
        test.put("startTime", "2024-05-01 00:00:00");
        test.put("endTime", "2024-06-30 23:59:59");
        test.put("duration", 30);
        test.put("questionCount", 10);
        test.put("totalScore", 100);
        test.put("status", "not_started");
        
        List<Map<String, Object>> questions = new ArrayList<>();
        
        Map<String, Object> question1 = new HashMap<>();
        question1.put("questionId", 1L);
        question1.put("questionText", "Java中用于声明常量的关键字是？");
        question1.put("type", "single");
        
        List<Map<String, Object>> options1 = new ArrayList<>();
        Map<String, Object> option1_1 = new HashMap<>();
        option1_1.put("value", "A");
        option1_1.put("text", "const");
        options1.add(option1_1);
        
        Map<String, Object> option1_2 = new HashMap<>();
        option1_2.put("value", "B");
        option1_2.put("text", "final");
        options1.add(option1_2);
        
        Map<String, Object> option1_3 = new HashMap<>();
        option1_3.put("value", "C");
        option1_3.put("text", "static");
        options1.add(option1_3);
        
        Map<String, Object> option1_4 = new HashMap<>();
        option1_4.put("value", "D");
        option1_4.put("text", "constant");
        options1.add(option1_4);
        
        question1.put("options", options1);
        questions.add(question1);
        
        Map<String, Object> question2 = new HashMap<>();
        question2.put("questionId", 2L);
        question2.put("questionText", "Java支持的访问修饰符包括？（多选）");
        question2.put("type", "multiple");
        
        List<Map<String, Object>> options2 = new ArrayList<>();
        Map<String, Object> option2_1 = new HashMap<>();
        option2_1.put("value", "A");
        option2_1.put("text", "public");
        options2.add(option2_1);
        
        Map<String, Object> option2_2 = new HashMap<>();
        option2_2.put("value", "B");
        option2_2.put("text", "protected");
        options2.add(option2_2);
        
        Map<String, Object> option2_3 = new HashMap<>();
        option2_3.put("value", "C");
        option2_3.put("text", "private");
        options2.add(option2_3);
        
        Map<String, Object> option2_4 = new HashMap<>();
        option2_4.put("value", "D");
        option2_4.put("text", "friend");
        options2.add(option2_4);
        
        question2.put("options", options2);
        questions.add(question2);
        
        test.put("questions", questions);
        
        return test;
    }

    @Override
    @Transactional
    public Map<String, Object> submitTest(Long testId, Long userId, List<Map<String, Object>> answers) {
        // TODO: 实现保存测试答案和评分逻辑
        
        // 模拟评分，实际开发中应该与标准答案比对
        Map<String, Object> result = new HashMap<>();
        result.put("testId", testId);
        result.put("userId", userId);
        result.put("score", 85);
        result.put("totalScore", 100);
        result.put("correctCount", 9);
        result.put("totalCount", 10);
        result.put("submittedAt", LocalDateTime.now());
        
        return result;
    }

    @Override
    @Transactional
    public void saveNote(Long userId, Long courseId, Long sectionId, String content) {
        // TODO: 实现保存笔记的逻辑
        System.out.println("保存笔记: 用户ID=" + userId + ", 课程ID=" + courseId + 
                          ", 章节ID=" + sectionId + ", 内容长度=" + content.length());
    }

    @Override
    public List<Map<String, Object>> getNotes(Long userId, Long courseId, Long sectionId) {
        // TODO: 实现从数据库获取笔记
        
        // 模拟数据，实际开发中应该从数据库获取
        List<Map<String, Object>> notes = new ArrayList<>();
        
        Map<String, Object> note = new HashMap<>();
        note.put("noteId", 1L);
        note.put("userId", userId);
        note.put("courseId", courseId);
        note.put("sectionId", sectionId);
        note.put("content", "这是一条学习笔记的示例内容，记录了学习过程中的重点内容和思考。");
        note.put("createTime", LocalDateTime.now().minusDays(1));
        note.put("updateTime", LocalDateTime.now().minusHours(2));
        notes.add(note);
        
        return notes;
    }

    @Override
    public Map<String, Object> getCourseStatistics(Long courseId) {
        // TODO: 实现从数据库获取课程统计信息
        
        // 模拟数据，实际开发中应该从数据库获取
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("enrollCount", 25);
        statistics.put("completeCount", 10);
        statistics.put("averageProgress", 65);
        statistics.put("averageScore", 85);
        
        return statistics;
    }

    @Override
    public List<Map<String, Object>> getEnrolledCourses(Long userId) {
        // TODO: 实现从数据库获取已报名课程
        
        // 模拟数据，实际开发中应该从数据库获取
        List<Map<String, Object>> courses = new ArrayList<>();
        
        Map<String, Object> course1 = new HashMap<>();
        course1.put("courseId", 1);
        course1.put("courseName", "Java高级编程");
        course1.put("startDate", "2024-03-01");
        course1.put("endDate", "2024-06-30");
        course1.put("lastLearningTime", "2024-05-10 15:30:00");
        course1.put("completionRate", 65);
        course1.put("completedSections", 13);
        course1.put("totalSections", 20);
        course1.put("totalLearningTime", 480);
        course1.put("testScore", 85);
        course1.put("assignmentCount", 3);
        course1.put("learningStatus", "in_progress");
        courses.add(course1);
        
        Map<String, Object> course2 = new HashMap<>();
        course2.put("courseId", 2);
        course2.put("courseName", "数据库系统原理");
        course2.put("startDate", "2024-02-15");
        course2.put("endDate", "2024-05-31");
        course2.put("lastLearningTime", "2024-05-05 10:15:00");
        course2.put("completionRate", 90);
        course2.put("completedSections", 18);
        course2.put("totalSections", 20);
        course2.put("totalLearningTime", 600);
        course2.put("testScore", 92);
        course2.put("assignmentCount", 4);
        course2.put("learningStatus", "nearly_complete");
        courses.add(course2);
        
        Map<String, Object> course3 = new HashMap<>();
        course3.put("courseId", 3);
        course3.put("courseName", "Web前端开发");
        course3.put("startDate", "2024-04-01");
        course3.put("endDate", "2024-07-15");
        course3.put("lastLearningTime", "2024-04-25 09:20:00");
        course3.put("completionRate", 25);
        course3.put("completedSections", 5);
        course3.put("totalSections", 20);
        course3.put("totalLearningTime", 180);
        course3.put("testScore", 75);
        course3.put("assignmentCount", 1);
        course3.put("learningStatus", "just_started");
        courses.add(course3);
        
        return courses;
    }
} 