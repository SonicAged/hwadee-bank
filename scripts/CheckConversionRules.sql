-- 检查学分转换规则表中的数据
SELECT * FROM credit_conversion_rule;

-- 检查学分转换服务相关配置
SELECT * FROM sys_permission WHERE permission_key LIKE 'credit:conversion%';

-- 检查各角色的学分转换权限
SELECT r.role_name, p.permission_name, p.permission_key 
FROM sys_role r
JOIN sys_role_permission rp ON r.role_id = rp.role_id
JOIN sys_permission p ON rp.permission_id = p.permission_id
WHERE p.permission_key LIKE 'credit:conversion%'
ORDER BY r.role_name, p.permission_id;

-- 检查学分转换相关的菜单权限
SELECT * FROM sys_permission WHERE permission_id = 58; 