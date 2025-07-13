package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.SysPermission;
import org.hwadee.backend.mapper.SysPermissionMapper;
import org.hwadee.backend.service.SysPermissionService;
import org.hwadee.backend.utils.PageResult;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    private static final Logger logger = LoggerFactory.getLogger(SysPermissionServiceImpl.class);

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Override
    public Result<SysPermission> addPermission(SysPermission permission) {
        try {
            // 检查权限名是否存在
            if (permissionMapper.checkPermissionNameExists(permission.getPermissionName(), null) > 0) {
                return Result.error("权限名称已存在");
            }

            // 检查权限标识是否存在
            if (permissionMapper.checkPermissionKeyExists(permission.getPermissionKey(), null) > 0) {
                return Result.error("权限标识已存在");
            }

            // 设置状态为启用
            permission.setStatus(SysPermission.ENABLE);

            // 插入权限
            permissionMapper.insert(permission);
            return Result.success(permission);
        } catch (Exception e) {
            logger.error("添加权限失败", e);
            return Result.error("添加权限失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updatePermission(SysPermission permission) {
        try {
            // 检查权限是否存在
            SysPermission existingPermission = permissionMapper.selectById(permission.getPermissionId());
            if (existingPermission == null) {
                return Result.error("权限不存在");
            }

            // 检查权限名是否存在
            if (!existingPermission.getPermissionName().equals(permission.getPermissionName()) &&
                    permissionMapper.checkPermissionNameExists(permission.getPermissionName(), permission.getPermissionId()) > 0) {
                return Result.error("权限名称已存在");
            }

            // 检查权限标识是否存在
            if (!existingPermission.getPermissionKey().equals(permission.getPermissionKey()) &&
                    permissionMapper.checkPermissionKeyExists(permission.getPermissionKey(), permission.getPermissionId()) > 0) {
                return Result.error("权限标识已存在");
            }

            // 更新权限
            permissionMapper.update(permission);
            return Result.success(true);
        } catch (Exception e) {
            logger.error("更新权限失败", e);
            return Result.error("更新权限失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> deletePermission(Long permissionId) {
        try {
            // 检查是否有子权限
            int childCount = permissionMapper.countChildrenByParentId(permissionId);
            if (childCount > 0) {
                return Result.error("该权限下有子权限，无法删除");
            }

            permissionMapper.deleteById(permissionId);
            return Result.success(true);
        } catch (Exception e) {
            logger.error("删除权限失败", e);
            return Result.error("删除权限失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> batchDeletePermission(List<Long> permissionIds) {
        try {
            // 检查是否有子权限
            for (Long permissionId : permissionIds) {
                int childCount = permissionMapper.countChildrenByParentId(permissionId);
                if (childCount > 0) {
                    return Result.error("权限ID为" + permissionId + "的权限下有子权限，无法删除");
                }
            }

            permissionMapper.deleteBatch(permissionIds);
            return Result.success(true);
        } catch (Exception e) {
            logger.error("批量删除权限失败", e);
            return Result.error("批量删除权限失败：" + e.getMessage());
        }
    }

    @Override
    public Result<SysPermission> getPermissionById(Long permissionId) {
        try {
            SysPermission permission = permissionMapper.selectById(permissionId);
            if (permission == null) {
                return Result.error("权限不存在");
            }
            return Result.success(permission);
        } catch (Exception e) {
            logger.error("查询权限失败", e);
            return Result.error("查询权限失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysPermission>> getAllPermissions() {
        try {
            List<SysPermission> permissions = permissionMapper.selectAll();
            return Result.success(permissions);
        } catch (Exception e) {
            logger.error("查询所有权限失败", e);
            return Result.error("查询所有权限失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getMenuTree() {
        try {
            List<SysPermission> permissions = permissionMapper.selectMenuTree();
            List<Map<String, Object>> menuTree = buildMenuTree(permissions, 0L);
            return Result.success(menuTree);
        } catch (Exception e) {
            logger.error("查询菜单树失败", e);
            return Result.error("查询菜单树失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<SysPermission>> getPermissionsByPage(
            String permissionName, Integer permissionType, Integer status, Integer pageNum, Integer pageSize) {
        try {
            // 计算分页参数
            int offset = (pageNum - 1) * pageSize;
            int limit = pageSize;
            
            // 查询数据
            List<SysPermission> permissions = permissionMapper.selectByCondition(
                    permissionName, permissionType, status, offset, limit);
            long total = permissionMapper.countByCondition(permissionName, permissionType, status);
            
            // 构建分页结果
            PageResult<SysPermission> pageResult = new PageResult<>(permissions, total, pageNum, pageSize);
            return Result.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询权限列表失败", e);
            return Result.error("分页查询权限列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> checkPermissionNameExists(String permissionName, Long permissionId) {
        try {
            int count = permissionMapper.checkPermissionNameExists(permissionName, permissionId);
            return Result.success(count > 0);
        } catch (Exception e) {
            logger.error("检查权限名失败", e);
            return Result.error("检查权限名失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> checkPermissionKeyExists(String permissionKey, Long permissionId) {
        try {
            int count = permissionMapper.checkPermissionKeyExists(permissionKey, permissionId);
            return Result.success(count > 0);
        } catch (Exception e) {
            logger.error("检查权限标识失败", e);
            return Result.error("检查权限标识失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysPermission>> getPermissionsByRoleId(Long roleId) {
        try {
            List<SysPermission> permissions = permissionMapper.selectPermissionsByRoleId(roleId);
            return Result.success(permissions);
        } catch (Exception e) {
            logger.error("查询角色权限列表失败", e);
            return Result.error("查询角色权限列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysPermission>> getPermissionsByUserId(Long userId) {
        try {
            List<SysPermission> permissions = permissionMapper.selectPermissionsByUserId(userId);
            return Result.success(permissions);
        } catch (Exception e) {
            logger.error("查询用户权限列表失败", e);
            return Result.error("查询用户权限列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getMenuTreeByUserId(Long userId) {
        try {
            List<SysPermission> permissions = permissionMapper.selectPermissionsByUserId(userId);
            List<Map<String, Object>> menuTree = buildMenuTree(permissions, 0L);
            return Result.success(menuTree);
        } catch (Exception e) {
            logger.error("查询用户菜单树失败", e);
            return Result.error("查询用户菜单树失败：" + e.getMessage());
        }
    }

    /**
     * 构建菜单树
     * @param permissions 权限列表
     * @param parentId 父ID
     * @return 菜单树
     */
    private List<Map<String, Object>> buildMenuTree(List<SysPermission> permissions, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        // 筛选当前层级的权限
        List<SysPermission> currentLevel = permissions.stream()
                .filter(p -> Objects.equals(p.getParentId(), parentId))
                .sorted(Comparator.comparing(SysPermission::getSortOrder))
                .collect(Collectors.toList());

        // 构建树节点
        for (SysPermission permission : currentLevel) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", permission.getPermissionId());
            node.put("label", permission.getPermissionName());
            node.put("path", permission.getPath());
            node.put("component", permission.getComponent());
            node.put("icon", permission.getIcon());
            node.put("type", permission.getPermissionType());

            // 递归构建子节点
            List<Map<String, Object>> children = buildMenuTree(permissions, permission.getPermissionId());
            if (!children.isEmpty()) {
                node.put("children", children);
            }

            tree.add(node);
        }
        
        return tree;
    }
} 