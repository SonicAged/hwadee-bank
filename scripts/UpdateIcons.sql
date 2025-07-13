-- 更新系统权限图标为 Element Plus 可用的图标
-- 原始图标使用的是 el-icon-* 格式，现在需要更新为 Element Plus 的组件名称格式

-- 系统管理相关图标
UPDATE sys_permission SET icon = 'Setting' WHERE icon = 'el-icon-setting';
UPDATE sys_permission SET icon = 'User' WHERE icon = 'el-icon-user';
UPDATE sys_permission SET icon = 'UserFilled' WHERE icon = 'el-icon-s-custom';
UPDATE sys_permission SET icon = 'Key' WHERE icon = 'el-icon-key';

-- 学分管理相关图标
UPDATE sys_permission SET icon = 'Money' WHERE icon = 'el-icon-coin';
UPDATE sys_permission SET icon = 'Wallet' WHERE icon = 'el-icon-wallet';
UPDATE sys_permission SET icon = 'Document' WHERE icon = 'el-icon-document';
UPDATE sys_permission SET icon = 'Edit' WHERE icon = 'el-icon-edit';

-- 资源管理相关图标
UPDATE sys_permission SET icon = 'Folder' WHERE icon = 'el-icon-folder';
UPDATE sys_permission SET icon = 'Files' WHERE icon = 'el-icon-collection';
UPDATE sys_permission SET icon = 'Menu' WHERE icon = 'el-icon-menu';

-- 处理其他可能的图标
UPDATE sys_permission SET icon = 'Bell' WHERE icon = 'el-icon-bell';
UPDATE sys_permission SET icon = 'Star' WHERE icon = 'el-icon-star';
UPDATE sys_permission SET icon = 'Location' WHERE icon = 'el-icon-location';
UPDATE sys_permission SET icon = 'List' WHERE icon = 'el-icon-list';
UPDATE sys_permission SET icon = 'Connection' WHERE icon = 'el-icon-connection';
UPDATE sys_permission SET icon = 'Monitor' WHERE icon = 'el-icon-monitor';

-- 'Money'和'Wallet'图标在Element Plus中不一定存在，如果不存在可以使用替代品
-- 检查Money图标是否存在，如果不存在则使用CreditCard替代
UPDATE sys_permission SET icon = 'CreditCard' WHERE icon = 'Money';

-- 检查其他可能有问题的图标，确保页面正常显示
UPDATE sys_permission SET icon = 'Collection' WHERE icon = 'el-icon-tickets'; 