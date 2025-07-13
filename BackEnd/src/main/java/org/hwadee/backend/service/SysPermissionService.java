package org.hwadee.backend.service;

import org.hwadee.backend.entity.SysPermission;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;

import java.util.List;
import java.util.Map;

/**
 * 权限服务接口
 */
public interface SysPermissionService {

    /**
     * 添加权限
     */
    Result<SysPermission> addPermission(SysPermission permission);

    /**
     * 更新权限
     */
    Result<Boolean> updatePermission(SysPermission permission);

    /**
     * 删除权限
     */
    Result<Boolean> deletePermission(Long permissionId);

    /**
     * 批量删除权限
     */
    Result<Boolean> batchDeletePermission(List<Long> permissionIds);

    /**
     * 根据ID查询权限
     */
    Result<SysPermission> getPermissionById(Long permissionId);

    /**
     * 查询所有权限
     */
    Result<List<SysPermission>> getAllPermissions();

    /**
     * 查询菜单树
     */
    Result<List<Map<String, Object>>> getMenuTree();

    /**
     * 分页查询权限列表
     */
    Result<PageResult<SysPermission>> getPermissionsByPage(String permissionName, Integer permissionType, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 检查权限名是否存在
     */
    Result<Boolean> checkPermissionNameExists(String permissionName, Long permissionId);

    /**
     * 检查权限标识是否存在
     */
    Result<Boolean> checkPermissionKeyExists(String permissionKey, Long permissionId);

    /**
     * 根据角色ID查询权限列表
     */
    Result<List<SysPermission>> getPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限列表
     */
    Result<List<SysPermission>> getPermissionsByUserId(Long userId);

    /**
     * 根据用户ID查询权限菜单树
     */
    Result<List<Map<String, Object>>> getMenuTreeByUserId(Long userId);
} 