package org.hwadee.backend.controller;

import org.hwadee.backend.entity.SysPermission;
import org.hwadee.backend.service.SysPermissionService;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/system/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 添加权限
     */
    @PostMapping
    public Result<SysPermission> addPermission(@RequestBody SysPermission permission) {
        return permissionService.addPermission(permission);
    }

    /**
     * 更新权限
     */
    @PutMapping
    public Result<Boolean> updatePermission(@RequestBody SysPermission permission) {
        return permissionService.updatePermission(permission);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{permissionId}")
    public Result<Boolean> deletePermission(@PathVariable Long permissionId) {
        return permissionService.deletePermission(permissionId);
    }

    /**
     * 批量删除权限
     */
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeletePermission(@RequestBody List<Long> permissionIds) {
        return permissionService.batchDeletePermission(permissionIds);
    }

    /**
     * 根据ID查询权限
     */
    @GetMapping("/{permissionId}")
    public Result<SysPermission> getPermissionById(@PathVariable Long permissionId) {
        return permissionService.getPermissionById(permissionId);
    }

    /**
     * 查询所有权限
     */
    @GetMapping("/list")
    public Result<List<SysPermission>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    /**
     * 查询菜单树
     */
    @GetMapping("/menu-tree")
    public Result<List<Map<String, Object>>> getMenuTree() {
        try {
            Result<List<Map<String, Object>>> result = permissionService.getMenuTree();
            if (result != null) {
                return result;
            }
            return Result.error("获取菜单树失败");
        } catch (Exception e) {
            return Result.error("获取菜单树失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询权限列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysPermission>> getPermissionsByPage(
            @RequestParam(required = false) String permissionName,
            @RequestParam(required = false) Integer permissionType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Result<PageResult<SysPermission>> result = permissionService.getPermissionsByPage(
                permissionName, permissionType, status, pageNum, pageSize);
            if (result != null) {
                return result;
            }
            return Result.error("获取权限列表失败");
        } catch (Exception e) {
            return Result.error("获取权限列表失败：" + e.getMessage());
        }
    }

    /**
     * 检查权限名是否存在
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkPermissionNameExists(
            @RequestParam String permissionName,
            @RequestParam(required = false) Long permissionId) {
        return permissionService.checkPermissionNameExists(permissionName, permissionId);
    }

    /**
     * 检查权限标识是否存在
     */
    @GetMapping("/check-key")
    public Result<Boolean> checkPermissionKeyExists(
            @RequestParam String permissionKey,
            @RequestParam(required = false) Long permissionId) {
        return permissionService.checkPermissionKeyExists(permissionKey, permissionId);
    }

    /**
     * 根据角色ID查询权限列表
     */
    @GetMapping("/role/{roleId}")
    public Result<List<SysPermission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        return permissionService.getPermissionsByRoleId(roleId);
    }

    /**
     * 根据用户ID查询权限列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<SysPermission>> getPermissionsByUserId(@PathVariable Long userId) {
        return permissionService.getPermissionsByUserId(userId);
    }

    /**
     * 根据用户ID查询权限菜单树
     */
    @GetMapping("/user/{userId}/menu-tree")
    public Result<List<Map<String, Object>>> getMenuTreeByUserId(@PathVariable Long userId) {
        return permissionService.getMenuTreeByUserId(userId);
    }
} 