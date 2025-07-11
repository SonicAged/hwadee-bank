package org.hwadee.backend.controller;

import org.hwadee.backend.entity.CreditApplication;
import org.hwadee.backend.service.CreditApplicationService;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学分申请控制器
 */
@RestController
@RequestMapping("/credit/application")
public class CreditApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(CreditApplicationController.class);

    @Autowired
    private CreditApplicationService applicationService;

    /**
     * 提交学分申请
     */
    @PostMapping("/submit")
    public Result<String> submitApplication(@RequestBody CreditApplication application) {
        try {
            logger.info("提交学分申请，用户ID: {}", application.getUserId());
            return applicationService.submitApplication(application);
        } catch (Exception e) {
            logger.error("提交申请时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取申请详情
     */
    @GetMapping("/{id}")
    public Result<CreditApplication> getApplicationById(@PathVariable Long id) {
        try {
            return applicationService.getApplicationById(id);
        } catch (Exception e) {
            logger.error("查询申请详情时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取用户的申请列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<CreditApplication>> getUserApplications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return applicationService.getUserApplications(userId, page, size);
        } catch (Exception e) {
            logger.error("查询用户申请列表时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取所有申请列表（管理员）
     */
    @GetMapping("/all")
    public Result<List<CreditApplication>> getAllApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return applicationService.getAllApplications(page, size);
        } catch (Exception e) {
            logger.error("查询申请列表时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 根据状态获取申请列表
     */
    @GetMapping("/status/{status}")
    public Result<List<CreditApplication>> getApplicationsByStatus(
            @PathVariable Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return applicationService.getApplicationsByStatus(status, page, size);
        } catch (Exception e) {
            logger.error("根据状态查询申请列表时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 审核申请
     */
    @PostMapping("/review")
    public Result<String> reviewApplication(
            @RequestParam Long applicationId,
            @RequestParam Integer status,
            @RequestParam(required = false) String reviewComment,
            @RequestParam Long reviewerId) {
        try {
            logger.info("审核申请，申请ID: {}, 状态: {}, 审核人: {}", applicationId, status, reviewerId);
            return applicationService.reviewApplication(applicationId, status, reviewComment, reviewerId);
        } catch (Exception e) {
            logger.error("审核申请时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 更新申请信息
     */
    @PutMapping("/update")
    public Result<String> updateApplication(@RequestBody CreditApplication application) {
        try {
            return applicationService.updateApplication(application);
        } catch (Exception e) {
            logger.error("更新申请时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 删除申请
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteApplication(@PathVariable Long id) {
        try {
            return applicationService.deleteApplication(id);
        } catch (Exception e) {
            logger.error("删除申请时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 统计申请数量
     */
    @GetMapping("/count")
    public Result<Integer> getApplicationCount() {
        try {
            return applicationService.getApplicationCount();
        } catch (Exception e) {
            logger.error("统计申请数量时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 根据状态统计申请数量
     */
    @GetMapping("/count/status/{status}")
    public Result<Integer> getApplicationCountByStatus(@PathVariable Integer status) {
        try {
            return applicationService.getApplicationCountByStatus(status);
        } catch (Exception e) {
            logger.error("根据状态统计申请数量时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 