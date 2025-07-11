package org.hwadee.backend.controller;

import org.hwadee.backend.entity.SysUser;
import org.hwadee.backend.service.SysUserService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private SysUserService userService;

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
     * 修改密码
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