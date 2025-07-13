package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.SysRole;

import java.util.List;

/**
 * 角色Mapper接口
 */
@Mapper
public interface SysRoleMapper {

    /**
     * 添加角色
     */
    int insert(SysRole role);

    /**
     * 更新角色
     */
    int update(SysRole role);

    /**
     * 删除角色
     */
    int deleteById(@Param("roleId") Long roleId);

    /**
     * 根据ID查询角色
     */
    SysRole selectById(@Param("roleId") Long roleId);

    /**
     * 查询所有角色
     */
    List<SysRole> selectAll();

    /**
     * 根据条件分页查询角色列表
     */
    List<SysRole> selectByCondition(@Param("roleName") String roleName, 
                                   @Param("status") Integer status, 
                                   @Param("offset") Integer offset, 
                                   @Param("limit") Integer limit);

    /**
     * 查询角色总数
     */
    long countByCondition(@Param("roleName") String roleName, @Param("status") Integer status);

    /**
     * 检查角色名是否存在
     */
    int checkRoleNameExists(@Param("roleName") String roleName, @Param("roleId") Long roleId);

    /**
     * 检查角色标识是否存在
     */
    int checkRoleKeyExists(@Param("roleKey") String roleKey, @Param("roleId") Long roleId);

    /**
     * 批量删除角色
     */
    int deleteBatch(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 为角色分配权限
     */
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 删除角色权限关联
     */
    int deleteRolePermissionByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID查询权限ID列表
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);
} 