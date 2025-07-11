package org.hwadee.backend.controller;

import org.hwadee.backend.entity.SysUser;
import org.hwadee.backend.entity.LoginDTO;
import org.hwadee.backend.service.SysUserService;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private SysUserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            logger.info("收到登录请求，用户名: {}", loginDTO.getUsername());
            Result<String> result = userService.login(loginDTO);
            logger.info("登录结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("登录过程中发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody SysUser user) {
        try {
            logger.info("收到注册请求，用户名: {}", user.getUsername());
            Result<String> result = userService.register(user);
            logger.info("注册结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("注册过程中发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        try {
            return userService.checkUsernameExists(username);
        } catch (Exception e) {
            logger.error("检查用户名时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        try {
            return userService.checkEmailExists(email);
        } catch (Exception e) {
            logger.error("检查邮箱时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 