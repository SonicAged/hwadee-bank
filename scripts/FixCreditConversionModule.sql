-- ===============================
-- 学分转换模块综合修复脚本
-- ===============================

-- 1. 确保学分转换菜单权限存在
INSERT INTO sys_permission (permission_id, permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT 58, '学分转换', 'credit:conversion', 2, 5, '/credit/conversion', 'credit/CreditConversion', 'RefreshRight', 4, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_id = 58);

-- 2. 添加学分转换的操作权限
INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT '学分转换查询', 'credit:conversion:query', 3, 58, '', '', '', 1, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_key = 'credit:conversion:query');

INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT '学分转换创建', 'credit:conversion:create', 3, 58, '', '', '', 2, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_key = 'credit:conversion:create');

INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT '学分转换修改', 'credit:conversion:edit', 3, 58, '', '', '', 3, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_key = 'credit:conversion:edit');

INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT '学分转换删除', 'credit:conversion:delete', 3, 58, '', '', '', 4, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_key = 'credit:conversion:delete');

INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT '执行学分转换', 'credit:conversion:execute', 3, 58, '', '', '', 5, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_key = 'credit:conversion:execute');

-- 3. 将学分转换菜单权限分配给角色
-- 管理员角色
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, permission_id FROM sys_permission 
WHERE permission_key = 'credit:conversion'
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = 1 AND permission_id = (
        SELECT permission_id FROM sys_permission WHERE permission_key = 'credit:conversion'
    )
);

-- 教师角色
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission 
WHERE permission_key = 'credit:conversion'
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = 2 AND permission_id = (
        SELECT permission_id FROM sys_permission WHERE permission_key = 'credit:conversion'
    )
);

-- 学生角色
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission 
WHERE permission_key = 'credit:conversion'
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = 3 AND permission_id = (
        SELECT permission_id FROM sys_permission WHERE permission_key = 'credit:conversion'
    )
);

-- 4. 将学分转换操作权限分配给角色
-- 管理员角色 - 所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, permission_id FROM sys_permission 
WHERE permission_key LIKE 'credit:conversion:%'
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = 1 AND permission_id = sys_permission.permission_id
);

-- 教师角色 - 查询和执行权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission 
WHERE permission_key IN ('credit:conversion:query', 'credit:conversion:execute')
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = 2 AND permission_id = sys_permission.permission_id
);

-- 学生角色 - 只有查询权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission 
WHERE permission_key = 'credit:conversion:query'
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = 3 AND permission_id = sys_permission.permission_id
);

-- 5. 确保学分转换规则表中有足够的数据
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status, create_time, update_time)
SELECT '学历教育', '职业培训', 0.9, 1.00, 15.00, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '学历教育' AND target_type = '职业培训'
);

INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status, create_time, update_time)
SELECT '学历教育', '技能证书', 0.8, 1.00, 10.00, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '学历教育' AND target_type = '技能证书'
);

INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status, create_time, update_time)
SELECT '学历教育', '在线课程', 0.7, 1.00, 8.00, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '学历教育' AND target_type = '在线课程'
);

INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status, create_time, update_time)
SELECT '职业培训', '学历教育', 0.5, 1.00, 6.00, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '职业培训' AND target_type = '学历教育'
);

INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status, create_time, update_time)
SELECT '职业培训', '技能证书', 0.7, 1.00, 8.00, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '职业培训' AND target_type = '技能证书'
);

INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status, create_time, update_time)
SELECT '职业培训', '在线课程', 0.7, 1.00, 8.00, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '职业培训' AND target_type = '在线课程'
);

-- 6. 验证设置
-- 检查学分转换权限
SELECT 'PERMISSIONS CHECK' AS check_type, p.permission_id, p.permission_name, p.permission_key, p.permission_type, p.parent_id
FROM sys_permission p
WHERE p.permission_key LIKE 'credit:conversion%'
ORDER BY p.permission_id;

-- 检查角色权限分配
SELECT 'ROLE PERMISSIONS CHECK' AS check_type, r.role_name, p.permission_name, p.permission_key 
FROM sys_role r
JOIN sys_role_permission rp ON r.role_id = rp.role_id
JOIN sys_permission p ON rp.permission_id = p.permission_id
WHERE p.permission_key LIKE 'credit:conversion%'
ORDER BY r.role_name, p.permission_id;

-- 检查学分转换规则
SELECT 'CONVERSION RULES CHECK' AS check_type, rule_id, source_type, target_type, conversion_rate, min_credits, max_credits, status
FROM credit_conversion_rule
ORDER BY source_type, target_type; 