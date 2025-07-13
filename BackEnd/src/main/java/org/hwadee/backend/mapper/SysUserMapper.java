package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.SysUser;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户
     */
    SysUser selectByUserId(@Param("userId") Long userId);

    /**
     * 根据邮箱查询用户
     */
    SysUser selectByEmail(@Param("email") String email);

    /**
     * 插入用户
     */
    int insert(SysUser user);

    /**
     * 更新用户信息
     */
    int update(SysUser user);

    /**
     * 根据用户ID删除用户
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 查询所有用户（分页）
     */
    List<SysUser> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件查询用户数量
     */
    long countByCondition(@Param("username") String username, @Param("realName") String realName, @Param("status") Integer status);

    /**
     * 根据条件查询用户列表
     */
    List<SysUser> selectByCondition(@Param("username") String username, @Param("realName") String realName, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户角色
     */
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户权限
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    List<SysUser> selectUserByRoleId(@Param("roleId") Long roleId);
}