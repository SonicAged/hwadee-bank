package org.hwadee.backend.controller;

import org.hwadee.backend.entity.SysRole;
import org.hwadee.backend.service.SysRoleService;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    /**
     * 添加角色
     */
    @PostMapping
    public Result<SysRole> addRole(@RequestBody SysRole role) {
        return roleService.addRole(role);
    }

    /**
     * 更新角色
     */
    @PutMapping
    public Result<Boolean> updateRole(@RequestBody SysRole role) {
        return roleService.updateRole(role);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    public Result<Boolean> deleteRole(@PathVariable Long roleId) {
        return roleService.deleteRole(roleId);
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteRole(@RequestBody List<Long> roleIds) {
        return roleService.batchDeleteRole(roleIds);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{roleId}")
    public Result<SysRole> getRoleById(@PathVariable Long roleId) {
        return roleService.getRoleById(roleId);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/list")
    public Result<List<SysRole>> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    public Result<PageResult<SysRole>> getRolesByPage(
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return roleService.getRolesByPage(roleName, status, pageNum, pageSize);
    }

    /**
     * 检查角色名是否存在
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkRoleNameExists(
            @RequestParam String roleName,
            @RequestParam(required = false) Long roleId) {
        return roleService.checkRoleNameExists(roleName, roleId);
    }

    /**
     * 检查角色标识是否存在
     */
    @GetMapping("/check-key")
    public Result<Boolean> checkRoleKeyExists(
            @RequestParam String roleKey,
            @RequestParam(required = false) Long roleId) {
        return roleService.checkRoleKeyExists(roleKey, roleId);
    }

    /**
     * 根据用户ID查询角色列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<SysRole>> getRolesByUserId(@PathVariable Long userId) {
        return roleService.getRolesByUserId(userId);
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/{roleId}/permissions")
    public Result<Boolean> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        return roleService.assignPermissions(roleId, permissionIds);
    }

    /**
     * 根据角色ID查询权限ID列表
     */
    @GetMapping("/{roleId}/permissions")
    public Result<List<Long>> getPermissionIdsByRoleId(@PathVariable Long roleId) {
        return roleService.getPermissionIdsByRoleId(roleId);
    }
} 