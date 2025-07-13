package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.SysRole;
import org.hwadee.backend.mapper.SysRoleMapper;
import org.hwadee.backend.service.SysRoleService;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public Result<SysRole> addRole(SysRole role) {
        try {
            // 检查角色名是否存在
            if (roleMapper.checkRoleNameExists(role.getRoleName(), null) > 0) {
                return Result.error("角色名称已存在");
            }

            // 检查角色标识是否存在
            if (roleMapper.checkRoleKeyExists(role.getRoleKey(), null) > 0) {
                return Result.error("角色标识已存在");
            }

            // 设置状态为启用
            role.setStatus(SysRole.ENABLE);

            // 插入角色
            roleMapper.insert(role);
            return Result.success(role);
        } catch (Exception e) {
            logger.error("添加角色失败", e);
            return Result.error("添加角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updateRole(SysRole role) {
        try {
            // 检查角色是否存在
            SysRole existingRole = roleMapper.selectById(role.getRoleId());
            if (existingRole == null) {
                return Result.error("角色不存在");
            }

            // 检查角色名是否存在
            if (!existingRole.getRoleName().equals(role.getRoleName()) &&
                    roleMapper.checkRoleNameExists(role.getRoleName(), role.getRoleId()) > 0) {
                return Result.error("角色名称已存在");
            }

            // 检查角色标识是否存在
            if (!existingRole.getRoleKey().equals(role.getRoleKey()) &&
                    roleMapper.checkRoleKeyExists(role.getRoleKey(), role.getRoleId()) > 0) {
                return Result.error("角色标识已存在");
            }

            // 更新角色
            roleMapper.update(role);
            return Result.success(true);
        } catch (Exception e) {
            logger.error("更新角色失败", e);
            return Result.error("更新角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteRole(Long roleId) {
        try {
            roleMapper.deleteById(roleId);
            return Result.success(true);
        } catch (Exception e) {
            logger.error("删除角色失败", e);
            return Result.error("删除角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> batchDeleteRole(List<Long> roleIds) {
        try {
            roleMapper.deleteBatch(roleIds);
            return Result.success(true);
        } catch (Exception e) {
            logger.error("批量删除角色失败", e);
            return Result.error("批量删除角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<SysRole> getRoleById(Long roleId) {
        try {
            SysRole role = roleMapper.selectById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }
            return Result.success(role);
        } catch (Exception e) {
            logger.error("查询角色失败", e);
            return Result.error("查询角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysRole>> getAllRoles() {
        try {
            List<SysRole> roles = roleMapper.selectAll();
            return Result.success(roles);
        } catch (Exception e) {
            logger.error("查询角色列表失败", e);
            return Result.error("查询角色列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<SysRole>> getRolesByPage(String roleName, Integer status, Integer pageNum, Integer pageSize) {
        try {
            // 计算分页参数
            int offset = (pageNum - 1) * pageSize;
            int limit = pageSize;
            
            // 查询数据
            List<SysRole> roles = roleMapper.selectByCondition(roleName, status, offset, limit);
            long total = roleMapper.countByCondition(roleName, status);
            
            // 构建分页结果
            PageResult<SysRole> pageResult = new PageResult<>(roles, total, pageNum, pageSize);
            return Result.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询角色列表失败", e);
            return Result.error("分页查询角色列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> checkRoleNameExists(String roleName, Long roleId) {
        try {
            int count = roleMapper.checkRoleNameExists(roleName, roleId);
            return Result.success(count > 0);
        } catch (Exception e) {
            logger.error("检查角色名失败", e);
            return Result.error("检查角色名失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> checkRoleKeyExists(String roleKey, Long roleId) {
        try {
            int count = roleMapper.checkRoleKeyExists(roleKey, roleId);
            return Result.success(count > 0);
        } catch (Exception e) {
            logger.error("检查角色标识失败", e);
            return Result.error("检查角色标识失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysRole>> getRolesByUserId(Long userId) {
        try {
            List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
            return Result.success(roles);
        } catch (Exception e) {
            logger.error("查询用户角色列表失败", e);
            return Result.error("查询用户角色列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> assignPermissions(Long roleId, List<Long> permissionIds) {
        try {
            // 检查角色是否存在
            SysRole role = roleMapper.selectById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }

            // 先删除原有权限
            roleMapper.deleteRolePermissionByRoleId(roleId);

            // 添加新权限
            if (permissionIds != null && !permissionIds.isEmpty()) {
                for (Long permissionId : permissionIds) {
                    roleMapper.insertRolePermission(roleId, permissionId);
                }
            }

            return Result.success(true);
        } catch (Exception e) {
            logger.error("分配权限失败", e);
            return Result.error("分配权限失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Long>> getPermissionIdsByRoleId(Long roleId) {
        try {
            List<Long> permissionIds = roleMapper.selectPermissionIdsByRoleId(roleId);
            return Result.success(permissionIds);
        } catch (Exception e) {
            logger.error("查询角色权限ID列表失败", e);
            return Result.error("查询角色权限ID列表失败：" + e.getMessage());
        }
    }
} 