package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.SysPermission;

import java.util.List;

/**
 * 权限Mapper接口
 */
@Mapper
public interface SysPermissionMapper {

    /**
     * 添加权限
     */
    int insert(SysPermission permission);

    /**
     * 更新权限
     */
    int update(SysPermission permission);

    /**
     * 删除权限
     */
    int deleteById(@Param("permissionId") Long permissionId);

    /**
     * 根据ID查询权限
     */
    SysPermission selectById(@Param("permissionId") Long permissionId);

    /**
     * 查询所有权限
     */
    List<SysPermission> selectAll();

    /**
     * 查询所有菜单权限（用于构建菜单树）
     */
    List<SysPermission> selectMenuTree();

    /**
     * 根据条件分页查询权限列表
     */
    List<SysPermission> selectByCondition(@Param("permissionName") String permissionName, 
                                         @Param("permissionType") Integer permissionType,
                                         @Param("status") Integer status, 
                                         @Param("offset") Integer offset, 
                                         @Param("limit") Integer limit);

    /**
     * 查询权限总数
     */
    long countByCondition(@Param("permissionName") String permissionName, 
                         @Param("permissionType") Integer permissionType,
                         @Param("status") Integer status);

    /**
     * 检查权限名是否存在
     */
    int checkPermissionNameExists(@Param("permissionName") String permissionName, @Param("permissionId") Long permissionId);

    /**
     * 检查权限标识是否存在
     */
    int checkPermissionKeyExists(@Param("permissionKey") String permissionKey, @Param("permissionId") Long permissionId);

    /**
     * 批量删除权限
     */
    int deleteBatch(@Param("permissionIds") List<Long> permissionIds);

    /**
     * 根据角色ID查询权限列表
     */
    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限列表
     */
    List<SysPermission> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询子权限数量
     */
    int countChildrenByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父ID查询子权限
     */
    List<SysPermission> selectByParentId(@Param("parentId") Long parentId);
} 