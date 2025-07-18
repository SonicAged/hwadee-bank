package org.hwadee.backend.controller;

import org.hwadee.backend.entity.LoginResponse;
import org.hwadee.backend.entity.SysUser;
import org.hwadee.backend.entity.LoginDTO;
import org.hwadee.backend.service.SysUserService;
import org.hwadee.backend.utils.JwtUtil;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private SysUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        try {
            logger.info("收到登录请求，用户名: {}", loginDTO.getUsername());
            Result<LoginResponse> result = userService.login(loginDTO);
            logger.info("登录结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("登录过程中发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userinfo")
    public Result<SysUser> getCurrentUserInfo(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7); // 移除 "Bearer " 前缀
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            logger.info("获取用户信息，用户ID: {}", userId);
            return userService.getUserById(userId);
        } catch (Exception e) {
            logger.error("获取用户信息时发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户权限
     */
    @GetMapping("/permissions")
    public Result<List<String>> getCurrentUserPermissions(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7); // 移除 "Bearer " 前缀
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            logger.info("获取用户权限，用户ID: {}", userId);
            return userService.getUserPermissions(userId);
        } catch (Exception e) {
            logger.error("获取用户权限时发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户角色
     */
    @GetMapping("/roles")
    public Result<List<String>> getCurrentUserRoles(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7); // 移除 "Bearer " 前缀
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            logger.info("获取用户角色，用户ID: {}", userId);
            return userService.getUserRoles(userId);
        } catch (Exception e) {
            logger.error("获取用户角色时发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody SysUser user) {
        try {
            logger.info("收到注册请求，用户信息: {}", user);
            // 打印请求中的密码字段(仅用于调试)
            logger.info("注册请求中的密码: '{}'", user.getPassword());
            logger.info("密码长度: {}", user.getPassword() != null ? user.getPassword().length() : 0);
            
            Result<String> result = userService.register(user);
            logger.info("注册结果: {}", result.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("注册过程中发生异常", e);
            e.printStackTrace(); // 打印完整的异常堆栈
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