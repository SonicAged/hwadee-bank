-- 添加学分转换操作权限
INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time, update_time)
VALUES 
('学分转换查询', 'credit:conversion:query', 3, 58, '', '', '', 1, 1, NOW(), NOW()),
('学分转换创建', 'credit:conversion:create', 3, 58, '', '', '', 2, 1, NOW(), NOW()),
('学分转换修改', 'credit:conversion:edit', 3, 58, '', '', '', 3, 1, NOW(), NOW()),
('学分转换删除', 'credit:conversion:delete', 3, 58, '', '', '', 4, 1, NOW(), NOW()),
('执行学分转换', 'credit:conversion:execute', 3, 58, '', '', '', 5, 1, NOW(), NOW());

-- 为管理员角色分配这些权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, permission_id FROM sys_permission 
WHERE permission_key IN ('credit:conversion:query', 'credit:conversion:create', 'credit:conversion:edit', 'credit:conversion:delete', 'credit:conversion:execute');

-- 为教师角色分配查询和执行权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission 
WHERE permission_key IN ('credit:conversion:query', 'credit:conversion:execute');

-- 为学生角色分配查询权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission 
WHERE permission_key = 'credit:conversion:query'; 