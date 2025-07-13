package org.hwadee.backend.service;

import org.hwadee.backend.entity.LoginResponse;
import org.hwadee.backend.entity.SysUser;
import org.hwadee.backend.entity.LoginDTO;
import org.hwadee.backend.entity.UpdateProfileDTO;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.utils.Result;

import java.util.List;

/**
 * 用户服务接口
 */
public interface SysUserService {

    /**
     * 用户登录
     */
    Result<LoginResponse> login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    Result<String> register(SysUser user);

    /**
     * 根据用户ID查询用户
     */
    Result<SysUser> getUserById(Long userId);

    /**
     * 根据用户名查询用户
     */
    Result<SysUser> getUserByUsername(String username);

    /**
     * 更新用户信息
     */
    Result<String> updateUser(SysUser user);

    /**
     * 更新用户个人资料
     */
    Result<String> updateProfile(Long userId, UpdateProfileDTO profileDTO);

    /**
     * 删除用户
     */
    Result<String> deleteUser(Long userId);

    /**
     * 分页查询用户列表
     */
    Result<List<SysUser>> getUserList(String username, String realName, Integer status, int page, int size);
    
    /**
     * 分页查询用户列表（返回分页信息）
     */
    Result<PageResult<SysUser>> getUserListPage(String username, String realName, Integer status, int page, int size);

    /**
     * 修改密码
     */
    Result<String> changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     */
    Result<String> resetPassword(Long userId, String newPassword);

    /**
     * 检查用户名是否存在
     */
    Result<Boolean> checkUsernameExists(String username);

    /**
     * 检查邮箱是否存在
     */
    Result<Boolean> checkEmailExists(String email);

    /**
     * 获取用户权限
     */
    Result<List<String>> getUserPermissions(Long userId);

    /**
     * 获取用户角色
     */
    Result<List<String>> getUserRoles(Long userId);
} 