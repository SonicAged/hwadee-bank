package org.hwadee.backend.controller;

import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.entity.TrainingProgram;
import org.hwadee.backend.service.TrainingProgramService;
import org.hwadee.backend.utils.JwtUtil;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 培训项目控制器
 */
@RestController
@RequestMapping("/training")
public class TrainingProgramController {

    @Autowired
    private TrainingProgramService trainingProgramService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 分页获取培训项目列表
     */
    @GetMapping("/programs")
    public Result<PageResult<TrainingProgram>> getProgramList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String programName,
            @RequestParam(required = false) Integer programType,
            @RequestParam(required = false) Integer status) {

        PageResult<TrainingProgram> result = trainingProgramService.getProgramList(page, size, programName, programType, status);
        return Result.success(result);
    }

    /**
     * 获取培训项目详情
     */
    @GetMapping("/programs/{programId}")
    public Result<TrainingProgram> getProgramById(@PathVariable Long programId) {
        TrainingProgram program = trainingProgramService.getProgramById(programId);
        return Result.success(program);
    }

    /**
     * 创建培训项目
     */
    @PostMapping("/programs")
    public Result<TrainingProgram> createProgram(@RequestBody TrainingProgram program) {
        TrainingProgram newProgram = trainingProgramService.createProgram(program);
        return Result.success(newProgram);
    }

    /**
     * 更新培训项目
     */
    @PutMapping("/programs/{programId}")
    public Result<TrainingProgram> updateProgram(@PathVariable Long programId, @RequestBody TrainingProgram program) {
        program.setProgramId(programId);
        TrainingProgram updatedProgram = trainingProgramService.updateProgram(program);
        return Result.success(updatedProgram);
    }

    /**
     * 删除培训项目
     */
    @DeleteMapping("/programs/{programId}")
    public Result<Void> deleteProgram(@PathVariable Long programId) {
        trainingProgramService.deleteProgram(programId);
        return Result.success();
    }

    /**
     * 报名参加培训项目
     */
    @PostMapping("/programs/{programId}/enroll")
    public Result<Void> enrollProgram(
            @PathVariable Long programId,
            HttpServletRequest request) {
        try {
            // 从Token获取用户ID
            Long userId = jwtUtil.getUserIdFromRequest(request);
            trainingProgramService.enrollProgram(programId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            // 捕获运行时异常并返回具体错误信息
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            return Result.error("报名失败，请稍后重试");
        }
    }

    /**
     * 取消报名
     */
    @PostMapping("/programs/{programId}/cancel")
    public Result<Void> cancelEnrollment(
            @PathVariable Long programId,
            HttpServletRequest request) {
        try {
            // 从Token获取用户ID
            Long userId = jwtUtil.getUserIdFromRequest(request);
            trainingProgramService.cancelEnrollment(programId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            // 捕获运行时异常并返回具体错误信息
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            return Result.error("取消报名失败，请稍后重试");
        }
    }

    /**
     * 确认参与培训
     */
    @PostMapping("/participants/{participantId}/confirm")
    public Result<Void> confirmParticipation(@PathVariable Long participantId) {
        trainingProgramService.confirmParticipation(participantId);
        return Result.success();
    }

    /**
     * 完成培训
     */
    @PostMapping("/participants/{participantId}/complete")
    public Result<Void> completeTraining(@PathVariable Long participantId) {
        trainingProgramService.completeTraining(participantId);
        return Result.success();
    }

    /**
     * 获取用户参与的培训项目
     */
    @GetMapping("/user/programs")
    public Result<PageResult<Map<String, Object>>> getUserPrograms(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        // 从Token获取用户ID
        Long userId = jwtUtil.getUserIdFromRequest(request);
        PageResult<Map<String, Object>> result = trainingProgramService.getUserPrograms(userId, page, size, status);
        return Result.success(result);
    }

    /**
     * 获取培训项目参与者
     */
    @GetMapping("/programs/{programId}/participants")
    public Result<PageResult<Map<String, Object>>> getProgramParticipants(
            @PathVariable Long programId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        PageResult<Map<String, Object>> result = trainingProgramService.getProgramParticipants(programId, page, size, status);
        return Result.success(result);
    }

    /**
     * 检查用户是否参与培训项目
     */
    @GetMapping("/programs/{programId}/check")
    public Result<Boolean> checkUserInProgram(
            @PathVariable Long programId,
            HttpServletRequest request) {
        // 从Token获取用户ID
        Long userId = jwtUtil.getUserIdFromRequest(request);
        boolean isEnrolled = trainingProgramService.checkUserInProgram(programId, userId);
        return Result.success(isEnrolled);
    }

    /**
     * 获取培训项目统计信息
     */
    @GetMapping("/programs/{programId}/statistics")
    public Result<Map<String, Object>> getProgramStatistics(@PathVariable Long programId) {
        Map<String, Object> statistics = trainingProgramService.getProgramStatistics(programId);
        return Result.success(statistics);
    }

    /**
     * 获取最新培训项目
     */
    @GetMapping("/programs/latest")
    public Result<List<TrainingProgram>> getLatestPrograms(
            @RequestParam(defaultValue = "5") Integer limit) {
        List<TrainingProgram> programs = trainingProgramService.getLatestPrograms(limit);
        return Result.success(programs);
    }
} 