-- ====================================
-- 修复数据库字符集为utf8mb4的脚本
-- 数据库名称：BankAnalysis
-- 说明：此脚本将修改现有数据库的字符集以支持中文
-- ====================================

-- 使用数据库
USE BankAnalysis;

-- 设置连接的字符集为utf8mb4
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 修改数据库字符集
ALTER DATABASE BankAnalysis CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 修改学分转换规则表的字符集
ALTER TABLE credit_conversion_rule CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE credit_conversion_rule MODIFY source_type VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL;
ALTER TABLE credit_conversion_rule MODIFY target_type VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL;

-- 修改其他可能包含中文的表和列
ALTER TABLE sys_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE credit_record CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE credit_application CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE learning_resource CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE resource_category CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE resource_tag CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 删除现有的转换规则
DELETE FROM credit_conversion_rule;

-- 插入中文的转换规则
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status) VALUES
('技能证书', '职业培训', 0.8, 1.0, 10.0, 1),
('职业培训', '技能证书', 0.7, 1.0, 8.0, 1),
('学历教育', '职业培训', 0.9, 1.0, 15.0, 1),
('在线课程', '职业培训', 0.6, 1.0, 5.0, 1),
('其他', '技能证书', 0.9, 1.0, 12.0, 1);

-- 添加更多转换规则，确保覆盖所有可能的转换路径
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status) VALUES
('学历教育', '技能证书', 0.8, 1.0, 10.0, 1),
('学历教育', '在线课程', 0.7, 1.0, 8.0, 1),
('技能证书', '学历教育', 0.6, 1.0, 8.0, 1),
('技能证书', '在线课程', 0.8, 1.0, 10.0, 1),
('职业培训', '学历教育', 0.5, 1.0, 6.0, 1),
('职业培训', '在线课程', 0.7, 1.0, 8.0, 1),
('在线课程', '学历教育', 0.5, 1.0, 6.0, 1),
('在线课程', '技能证书', 0.6, 1.0, 7.0, 1);

-- 确认更新结果
SELECT * FROM credit_conversion_rule;

-- 显示表的字符集信息
SHOW TABLE STATUS LIKE 'credit_conversion_rule';

-- 显示列的字符集信息
SHOW FULL COLUMNS FROM credit_conversion_rule; 