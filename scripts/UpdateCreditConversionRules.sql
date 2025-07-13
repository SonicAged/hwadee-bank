-- 更新学分转换规则，将英文改为中文
USE BankAnalysis;

-- 设置连接的字符集为utf8mb4
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_database=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_server=utf8mb4;

-- 先删除现有的转换规则
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