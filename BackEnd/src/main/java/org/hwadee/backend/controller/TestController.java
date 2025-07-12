package org.hwadee.backend.controller;

import org.hwadee.backend.entity.SysUser;
import org.hwadee.backend.entity.UpdateProfileDTO;
import org.hwadee.backend.service.SysUserService;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private SysUserService userService;

    /**
     * 测试用户个人资料更新
     */
    @PostMapping("/profile/{userId}")
    public Result<String> testUpdateProfile(@PathVariable Long userId) {
        try {
            UpdateProfileDTO profileDTO = new UpdateProfileDTO();
            profileDTO.setRealName("测试用户");
            profileDTO.setEmail("test@example.com");
            profileDTO.setPhone("13800138000");
            profileDTO.setGender(1);
            profileDTO.setBirthDate(LocalDate.of(1990, 1, 1));
            profileDTO.setAvatar("http://example.com/avatar.jpg");

            Result<String> result = userService.updateProfile(userId, profileDTO);
            logger.info("测试更新个人资料结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("测试更新个人资料失败", e);
            return Result.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试密码修改
     */
    @PostMapping("/password/{userId}")
    public Result<String> testChangePassword(@PathVariable Long userId,
                                           @RequestParam String oldPassword,
                                           @RequestParam String newPassword) {
        try {
            Result<String> result = userService.changePassword(userId, oldPassword, newPassword);
            logger.info("测试修改密码结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("测试修改密码失败", e);
            return Result.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户信息用于测试
     */
    @GetMapping("/user/{userId}")
    public Result<SysUser> testGetUser(@PathVariable Long userId) {
        try {
            Result<SysUser> result = userService.getUserById(userId);
            logger.info("测试获取用户信息结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("测试获取用户信息失败", e);
            return Result.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("服务正常运行");
    }
} 