-- 确保学分转换菜单权限存在
INSERT INTO sys_permission (permission_id, permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT 58, '学分转换', 'credit:conversion', 2, 5, '/credit/conversion', 'credit/CreditConversion', 'RefreshRight', 4, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_id = 58);

-- 确保学分统计菜单权限存在
INSERT INTO sys_permission (permission_id, permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
SELECT 59, '统计概览', 'credit:statistics', 2, 5, '/credit/statistics', 'credit/CreditStatistics', 'PieChart', 5, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_id = 59);

-- 确保角色权限关联存在
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, 58 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role_permission WHERE role_id = 1 AND permission_id = 58);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, 58 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role_permission WHERE role_id = 2 AND permission_id = 58);

-- 检查credit_conversion_rule表是否有数据
SELECT COUNT(*) AS rule_count FROM credit_conversion_rule;

-- 如果没有足够的转换规则，添加一些基本规则（如果需要）
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status)
SELECT '学历教育', '职业培训', 0.9, 1.00, 15.00, 1
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '学历教育' AND target_type = '职业培训'
);

INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status)
SELECT '职业培训', '学历教育', 0.5, 1.00, 6.00, 1
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM credit_conversion_rule 
    WHERE source_type = '职业培训' AND target_type = '学历教育'
);

-- 确保信用额度类型包括所有需要的类型
SELECT DISTINCT source_type FROM credit_conversion_rule
UNION
SELECT DISTINCT target_type FROM credit_conversion_rule
ORDER BY 1; 