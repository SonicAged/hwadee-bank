package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.*;
import org.hwadee.backend.mapper.SysUserMapper;
import org.hwadee.backend.service.SysUserService;
import org.hwadee.backend.service.CreditAccountService;
import org.hwadee.backend.utils.JwtUtil;
import org.hwadee.backend.utils.MD5Util;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CreditAccountService creditAccountService;

    @Override
    public Result<LoginResponse> login(LoginDTO loginDTO) {
        try {
            // 验证参数
            if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }

            // 查询用户
            SysUser user = userMapper.selectByUsername(loginDTO.getUsername().trim());
            if (user == null) {
                return Result.error("用户名或密码错误");
            }

            // 检查用户状态
            if (user.getStatus() == SysUser.DISABLE) {
                return Result.error("用户已被禁用");
            }

            // 验证密码
            String encryptedPassword = MD5Util.encrypt(loginDTO.getPassword());
            if (!encryptedPassword.equals(user.getPassword())) {
                return Result.error("用户名或密码错误");
            }

            // 更新最后登录时间
            userMapper.updateLastLoginTime(user.getUserId());

            Long userId = user.getUserId();
            List<String> role = getUserRoles(userId).getData();

            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getUsername(), user.getUserId());

            LoginResponse response = new LoginResponse(
                    userId, user.getUsername(), user.getEmail(),
                    role, token
            );

            return Result.success("登录成功", response);
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> register(SysUser user) {
        try {
            // 验证参数
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
                return Result.error("真实姓名不能为空");
            }

            // 检查用户名是否已存在
            if (userMapper.selectByUsername(user.getUsername().trim()) != null) {
                return Result.error("用户名已存在");
            }

            // 检查邮箱是否已存在
            if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                if (userMapper.selectByEmail(user.getEmail().trim()) != null) {
                    return Result.error("邮箱已存在");
                }
            }

            // 密码加密
            user.setPassword(MD5Util.encrypt(user.getPassword()));
            user.setStatus(SysUser.ENABLE); // 默认启用
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            // 插入用户
            int result = userMapper.insert(user);
            if (result > 0) {
                // 自动创建学分账户
                try {
                    creditAccountService.createAccount(user.getUserId());
                } catch (Exception e) {
                    // 学分账户创建失败不影响用户注册，只记录日志
                    System.err.println("自动创建学分账户失败，用户ID: " + user.getUserId() + ", 错误: " + e.getMessage());
                }
                return Result.success("注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    @Override
    public Result<SysUser> getUserById(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            SysUser user = userMapper.selectByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            return Result.success(user);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Result<SysUser> getUserByUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }

            SysUser user = userMapper.selectByUsername(username.trim());
            if (user == null) {
                return Result.error("用户不存在");
            }

            return Result.success(user);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> updateUser(SysUser user) {
        try {
            if (user.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }

            // 检查用户是否存在
            SysUser existingUser = userMapper.selectByUserId(user.getUserId());
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            // 检查邮箱唯一性
            if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                SysUser emailUser = userMapper.selectByEmail(user.getEmail().trim());
                if (emailUser != null && !emailUser.getUserId().equals(user.getUserId())) {
                    return Result.error("邮箱已存在");
                }
            }

            user.setUpdateTime(LocalDateTime.now());
            int result = userMapper.update(user);
            if (result > 0) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> updateProfile(Long userId, UpdateProfileDTO profileDTO) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            // 检查用户是否存在
            SysUser existingUser = userMapper.selectByUserId(userId);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            // 检查邮箱唯一性
            if (profileDTO.getEmail() != null && !profileDTO.getEmail().trim().isEmpty()) {
                SysUser emailUser = userMapper.selectByEmail(profileDTO.getEmail().trim());
                if (emailUser != null && !emailUser.getUserId().equals(userId)) {
                    return Result.error("邮箱已存在");
                }
            }

            // 更新用户信息
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.setRealName(profileDTO.getRealName());
            user.setEmail(profileDTO.getEmail());
            user.setPhone(profileDTO.getPhone());
            user.setAvatar(profileDTO.getAvatar());
            user.setGender(profileDTO.getGender());
            user.setBirthDate(profileDTO.getBirthDate());
            user.setUpdateTime(LocalDateTime.now());

            int result = userMapper.update(user);
            if (result > 0) {
                return Result.success("个人资料更新成功");
            } else {
                return Result.error("个人资料更新失败");
            }
        } catch (Exception e) {
            return Result.error("个人资料更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> deleteUser(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            // 查询用户是否存在
            SysUser existingUser = userMapper.selectByUserId(userId);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }
            
            // 软删除：将用户状态设置为禁用，而不是物理删除
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.setStatus(SysUser.DISABLE); // 设置状态为禁用
            user.setUpdateTime(LocalDateTime.now());
            
            // 更新用户状态
            int result = userMapper.update(user);
            
            if (result > 0) {
                return Result.success("用户删除成功");
            } else {
                return Result.error("用户删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysUser>> getUserList(String username, String realName, Integer status, int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;

            int offset = (page - 1) * size;
            List<SysUser> users = userMapper.selectByCondition(username, realName, status, offset, size);
            
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<SysUser>> getUserListPage(String username, String realName, Integer status, int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;

            int offset = (page - 1) * size;
            
            // 查询数据总数
            long total = userMapper.countByCondition(username, realName, status);
            
            // 查询用户列表
            List<SysUser> users = userMapper.selectByCondition(username, realName, status, offset, size);
            
            // 封装分页结果
            PageResult<SysUser> pageResult = new PageResult<>();
            pageResult.setList(users);
            pageResult.setTotal(total);
            pageResult.setPage(page);
            pageResult.setSize(size);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                return Result.error("原密码不能为空");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return Result.error("新密码不能为空");
            }

            // 查询用户
            SysUser user = userMapper.selectByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证原密码
            String encryptedOldPassword = MD5Util.encrypt(oldPassword);
            if (!encryptedOldPassword.equals(user.getPassword())) {
                return Result.error("原密码错误");
            }

            // 更新密码
            user.setPassword(MD5Util.encrypt(newPassword));
            user.setUpdateTime(LocalDateTime.now());
            int result = userMapper.update(user);
            
            if (result > 0) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            return Result.error("密码修改失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> resetPassword(Long userId, String newPassword) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return Result.error("新密码不能为空");
            }

            // 查询用户
            SysUser user = userMapper.selectByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 重置密码
            user.setPassword(MD5Util.encrypt(newPassword));
            user.setUpdateTime(LocalDateTime.now());
            int result = userMapper.update(user);
            
            if (result > 0) {
                return Result.success("密码重置成功");
            } else {
                return Result.error("密码重置失败");
            }
        } catch (Exception e) {
            return Result.error("密码重置失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> checkUsernameExists(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }

            SysUser user = userMapper.selectByUsername(username.trim());
            return Result.success(user != null);
        } catch (Exception e) {
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> checkEmailExists(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return Result.error("邮箱不能为空");
            }

            SysUser user = userMapper.selectByEmail(email.trim());
            return Result.success(user != null);
        } catch (Exception e) {
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<String>> getUserPermissions(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            List<String> permissions = userMapper.selectPermissionsByUserId(userId);
            return Result.success(permissions);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<String>> getUserRoles(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            List<String> roles = userMapper.selectRolesByUserId(userId);
            return Result.success(roles);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
} 