package org.hwadee.backend.service;

import org.hwadee.backend.entity.SysRole;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService {

    /**
     * 添加角色
     */
    Result<SysRole> addRole(SysRole role);

    /**
     * 更新角色
     */
    Result<Boolean> updateRole(SysRole role);

    /**
     * 删除角色
     */
    Result<Boolean> deleteRole(Long roleId);

    /**
     * 批量删除角色
     */
    Result<Boolean> batchDeleteRole(List<Long> roleIds);

    /**
     * 根据ID查询角色
     */
    Result<SysRole> getRoleById(Long roleId);

    /**
     * 查询所有角色
     */
    Result<List<SysRole>> getAllRoles();

    /**
     * 分页查询角色列表
     */
    Result<PageResult<SysRole>> getRolesByPage(String roleName, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 检查角色名是否存在
     */
    Result<Boolean> checkRoleNameExists(String roleName, Long roleId);

    /**
     * 检查角色标识是否存在
     */
    Result<Boolean> checkRoleKeyExists(String roleKey, Long roleId);

    /**
     * 根据用户ID查询角色列表
     */
    Result<List<SysRole>> getRolesByUserId(Long userId);

    /**
     * 为角色分配权限
     */
    Result<Boolean> assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 根据角色ID查询权限ID列表
     */
    Result<List<Long>> getPermissionIdsByRoleId(Long roleId);
} 