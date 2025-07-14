-- ====================================
-- 权限角色对应关系设置脚本
-- 创建时间：2024-12-29
-- 说明：此脚本会添加更完整的权限设计并分配给不同角色
-- ====================================

USE BankAnalysis;

-- 清空现有的角色-权限关联
TRUNCATE TABLE sys_role_permission;

-- 1. 扩展现有权限，添加更多权限节点
-- 添加课程管理模块权限
INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order) VALUES
-- 课程管理模块
('课程管理', 'course', 1, 0, '/course', NULL, 'Files', 4),
('课程列表', 'course:list', 2, 12, '/course/list', 'course/CourseList', 'Document', 1),
('课程详情', 'course:detail', 2, 12, '/course/detail', 'course/CourseDetail', 'View', 2),
('学习进度', 'course:progress', 2, 12, '/course/progress', 'course/LearningProgress', 'Histogram', 3),
-- 课程按钮权限
('课程查询', 'course:list:query', 3, 13, '', '', '', 1),
('课程创建', 'course:list:create', 3, 13, '', '', '', 2),
('课程修改', 'course:list:edit', 3, 13, '', '', '', 3),
('课程删除', 'course:list:delete', 3, 13, '', '', '', 4),
('课程审核', 'course:list:audit', 3, 13, '', '', '', 5),

-- 培训项目模块
('培训项目', 'training', 1, 0, '/training', NULL, 'Connection', 5),
('培训计划', 'training:program', 2, 22, '/training/program', 'course/TrainingProgram', 'Calendar', 1),
-- 培训按钮权限
('培训查询', 'training:program:query', 3, 23, '', '', '', 1),
('培训创建', 'training:program:create', 3, 23, '', '', '', 2),
('培训修改', 'training:program:edit', 3, 23, '', '', '', 3),
('培训删除', 'training:program:delete', 3, 23, '', '', '', 4),

-- 学分管理扩展按钮权限
('学分账户查询', 'credit:account:query', 3, 6, '', '', '', 1),
('学分账户修改', 'credit:account:edit', 3, 6, '', '', '', 2),
('学分记录查询', 'credit:record:query', 3, 7, '', '', '', 1),
('学分记录创建', 'credit:record:create', 3, 7, '', '', '', 2),
('学分申请查询', 'credit:application:query', 3, 8, '', '', '', 1),
('学分申请创建', 'credit:application:create', 3, 8, '', '', '', 2),
('学分申请审核', 'credit:application:audit', 3, 8, '', '', '', 3),

-- 资源管理扩展按钮权限
('资源查询', 'resource:library:query', 3, 10, '', '', '', 1),
('资源上传', 'resource:library:upload', 3, 10, '', '', '', 2),
('资源下载', 'resource:library:download', 3, 10, '', '', '', 3),
('资源修改', 'resource:library:edit', 3, 10, '', '', '', 4),
('资源删除', 'resource:library:delete', 3, 10, '', '', '', 5),
('资源分类查询', 'resource:category:query', 3, 11, '', '', '', 1),
('资源分类创建', 'resource:category:create', 3, 11, '', '', '', 2),
('资源分类修改', 'resource:category:edit', 3, 11, '', '', '', 3),
('资源分类删除', 'resource:category:delete', 3, 11, '', '', '', 4),

-- 系统管理扩展按钮权限
('用户查询', 'system:user:query', 3, 2, '', '', '', 1),
('用户创建', 'system:user:create', 3, 2, '', '', '', 2),
('用户修改', 'system:user:edit', 3, 2, '', '', '', 3),
('用户删除', 'system:user:delete', 3, 2, '', '', '', 4),
('角色查询', 'system:role:query', 3, 3, '', '', '', 1),
('角色创建', 'system:role:create', 3, 3, '', '', '', 2),
('角色修改', 'system:role:edit', 3, 3, '', '', '', 3),
('角色删除', 'system:role:delete', 3, 3, '', '', '', 4),
('权限查询', 'system:permission:query', 3, 4, '', '', '', 1),
('权限创建', 'system:permission:create', 3, 4, '', '', '', 2),
('权限修改', 'system:permission:edit', 3, 4, '', '', '', 3),
('权限删除', 'system:permission:delete', 3, 4, '', '', '', 4),

-- 日志与审计模块
('日志审计', 'log', 1, 0, '/audit', NULL, 'WarningFilled', 6),
('操作日志', 'log:operation', 2, 56, '/audit/operation', 'audit/OperationLog', 'List', 1),
('系统日志', 'log:system', 2, 56, '/audit/system', 'audit/SystemLog', 'Monitor', 2);

-- 2. 为不同角色分配权限

-- 2.1 超级管理员：拥有全部权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, permission_id FROM sys_permission;

-- 2.2 教师角色权限分配
-- 系统权限：只有查看用户的权限，没有系统管理的其他权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission WHERE permission_key = 'system:user:query';

-- 课程权限：全部课程和培训相关权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission WHERE 
  permission_key LIKE 'course%' OR 
  permission_key LIKE 'training%';

-- 资源权限：全部资源管理权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission WHERE 
  permission_key LIKE 'resource%';

-- 学分权限：查询权限和审核权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission WHERE 
  permission_key IN ('credit', 'credit:account', 'credit:account:query', 
                     'credit:record', 'credit:record:query', 
                     'credit:application', 'credit:application:query', 'credit:application:audit');

-- 日志权限：只有操作日志的查看权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, permission_id FROM sys_permission WHERE 
  permission_key IN ('log', 'log:operation');

-- 2.3 学生角色权限分配
-- 课程权限：只有查询和学习权限，没有管理权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission WHERE 
  permission_key IN ('course', 'course:list', 'course:detail', 'course:progress', 'course:list:query');

-- 培训权限：只有查询培训的权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission WHERE 
  permission_key IN ('training', 'training:program', 'training:program:query');

-- 资源权限：可以查询和下载资源，但不能管理
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission WHERE 
  permission_key IN ('resource', 'resource:library', 'resource:library:query', 'resource:library:download',
                    'resource:category', 'resource:category:query');

-- 学分权限：可以查看自己的学分账户和记录，可以申请学分
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, permission_id FROM sys_permission WHERE 
  permission_key IN ('credit', 'credit:account', 'credit:account:query',
                    'credit:record', 'credit:record:query',
                    'credit:application', 'credit:application:query', 'credit:application:create');

-- 3. 查询验证结果
SELECT '超级管理员权限数量：' AS role_name, COUNT(*) AS permission_count 
FROM sys_role_permission WHERE role_id = 1;

SELECT '教师角色权限数量：' AS role_name, COUNT(*) AS permission_count 
FROM sys_role_permission WHERE role_id = 2;

SELECT '学生角色权限数量：' AS role_name, COUNT(*) AS permission_count 
FROM sys_role_permission WHERE role_id = 3; 

INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order, status, create_time) VALUES ('学分转换', 'credit:conversion', 2, 5, '/credit/conversion', 'credit/CreditConversion', 'RefreshRight', 4, 1, NOW()), ('统计概览', 'credit:statistics', 2, 5, '/credit/statistics', 'credit/CreditStatistics', 'PieChart', 5, 1, NOW()), ('培训项目', 'course:training', 2, 12, '/course/training', 'course/TrainingProgram', 'School', 2, 1, NOW());"

INSERT INTO sys_role_permission (role_id, permission_id) SELECT 1, permission_id FROM sys_permission WHERE permission_key IN ('credit:conversion', 'credit:statistics', 'course:training'); INSERT INTO sys_role_permission (role_id, permission_id) SELECT 2, permission_id FROM sys_permission WHERE permission_key IN ('credit:conversion', 'credit:statistics', 'course:training'); INSERT INTO sys_role_permission (role_id, permission_id) SELECT 3, permission_id FROM sys_permission WHERE permission_key = 'course:training';"