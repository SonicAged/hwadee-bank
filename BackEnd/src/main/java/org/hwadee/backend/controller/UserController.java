package org.hwadee.backend.controller;

import org.hwadee.backend.entity.SysUser;
import org.hwadee.backend.entity.UpdateProfileDTO;
import org.hwadee.backend.entity.ChangePasswordDTO;
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
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SysUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<SysUser> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    public Result<String> updateUser(@PathVariable Long userId, @RequestBody SysUser user) {
        user.setUserId(userId);
        return userService.updateUser(user);
    }

    /**
     * 更新个人资料
     */
    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody UpdateProfileDTO profileDTO, HttpServletRequest request) {
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

            logger.info("更新个人资料，用户ID: {}", userId);
            return userService.updateProfile(userId, profileDTO);
        } catch (Exception e) {
            logger.error("更新个人资料时发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestBody ChangePasswordDTO passwordDTO, HttpServletRequest request) {
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

            // 验证新密码和确认密码是否一致
            if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
                return Result.error("新密码和确认密码不一致");
            }

            logger.info("修改密码，用户ID: {}", userId);
            return userService.changePassword(userId, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        } catch (Exception e) {
            logger.error("修改密码时发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public Result<List<SysUser>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getUserList(username, realName, status, page, size);
    }

    /**
     * 修改密码（管理员用）
     */
    @PostMapping("/{userId}/change-password")
    public Result<String> changePassword(@PathVariable Long userId, 
                                        @RequestParam String oldPassword, 
                                        @RequestParam String newPassword) {
        return userService.changePassword(userId, oldPassword, newPassword);
    }

    /**
     * 获取用户权限
     */
    @GetMapping("/{userId}/permissions")
    public Result<List<String>> getUserPermissions(@PathVariable Long userId) {
        return userService.getUserPermissions(userId);
    }

    /**
     * 获取用户角色
     */
    @GetMapping("/{userId}/roles")
    public Result<List<String>> getUserRoles(@PathVariable Long userId) {
        return userService.getUserRoles(userId);
    }
} 