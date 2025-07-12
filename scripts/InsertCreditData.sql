-- 删除已存在的测试数据（如果需要）
DELETE FROM credit_conversion_rule WHERE source_type IN ('技能证书', '职业培训', '学历教育', '在线课程');
DELETE FROM credit_record WHERE user_id IN (SELECT user_id FROM sys_user WHERE username IN ('student1', 'student2'));
DELETE FROM credit_application WHERE user_id IN (SELECT user_id FROM sys_user WHERE username IN ('student1', 'student2'));
DELETE FROM credit_account WHERE user_id IN (SELECT user_id FROM sys_user WHERE username IN ('student1', 'student2'));
DELETE FROM sys_user WHERE username IN ('student1', 'student2');

-- 插入两个学生用户
INSERT INTO sys_user (username, password, real_name, email, phone, gender, birth_date, status)
VALUES 
('student1', MD5('123456'), '张三', 'student1@example.com', '13800138001', 1, '2000-01-15', 1),
('student2', MD5('123456'), '李四', 'student2@example.com', '13800138002', 2, '2001-03-20', 1);

-- 获取插入的用户ID
SELECT user_id INTO @student1_id FROM sys_user WHERE username = 'student1';
SELECT user_id INTO @student2_id FROM sys_user WHERE username = 'student2';

-- 创建学分账户
INSERT INTO credit_account (user_id, total_credits, available_credits, frozen_credits)
VALUES 
(@student1_id, 25.5, 20.5, 5.0),  -- 张三的学分账户
(@student2_id, 18.0, 15.0, 3.0);  -- 李四的学分账户

-- 插入学分申请记录
INSERT INTO credit_application (user_id, application_type, achievement_name, achievement_description, applied_credits, evidence_files, current_step, status, apply_time)
VALUES
-- 张三的申请记录
(@student1_id, '技能证书', '计算机二级证书', '通过了全国计算机等级考试二级Python语言程序设计', 3.0, '["cert_python.pdf"]', 1, 3, '2024-01-10 14:30:00'),
(@student1_id, '职业培训', 'Web前端开发培训', '完成了为期3个月的Web前端开发实训课程', 5.0, '["training_cert.pdf", "project_demo.zip"]', 1, 1, '2024-03-15 09:15:00'),
(@student1_id, '学历教育', '大学英语四级证书', '通过英语四级考试，成绩良好', 2.5, '["cet4_cert.jpg"]', 1, 3, '2023-12-20 16:45:00'),

-- 李四的申请记录
(@student2_id, '技能证书', '软件测试工程师证书', '获得ISTQB初级软件测试工程师认证', 4.0, '["istqb_cert.pdf"]', 1, 3, '2024-02-05 10:20:00'),
(@student2_id, '在线课程', 'Java高级编程课程', '完成慕课网Java高级编程课程学习', 3.0, '["course_completion.pdf"]', 1, 4, '2024-03-01 11:30:00'),
(@student2_id, '职业培训', '敏捷开发实践培训', '参加为期1周的敏捷开发实践培训', 2.0, '["agile_training.pdf"]', 1, 1, '2024-03-18 14:00:00');

-- 插入学分记录
INSERT INTO credit_record (user_id, credit_type, credit_source, credit_amount, operation_type, status, remark, create_time)
VALUES
-- 张三的学分记录
(@student1_id, '技能证书', '计算机二级证书', 3.0, 1, 1, '技能认证学分获得', '2024-01-12 10:00:00'),
(@student1_id, '学历教育', '大学英语四级证书', 2.5, 1, 1, '语言能力认证学分获得', '2023-12-22 14:30:00'),
(@student1_id, '职业培训', 'Web前端开发培训', 5.0, 1, 2, '培训课程学分申请中', '2024-03-15 09:15:00'),
(@student1_id, '技能证书', '转换到职业培训学分', 2.0, 3, 1, '学分类型转换', '2024-02-01 15:45:00'),

-- 李四的学分记录
(@student2_id, '技能证书', '软件测试工程师证书', 4.0, 1, 1, '专业技能认证学分获得', '2024-02-07 11:20:00'),
(@student2_id, '在线课程', 'Java高级编程课程', 3.0, 1, 0, '在线课程学分申请被拒绝', '2024-03-02 16:30:00'),
(@student2_id, '职业培训', '敏捷开发实践培训', 2.0, 1, 2, '培训课程学分申请中', '2024-03-18 14:00:00'),
(@student2_id, '技能证书', '消费学分兑换课程', 1.0, 2, 1, '学分消费-课程兑换', '2024-02-20 09:30:00');

-- 插入学分转换规则
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status)
VALUES
('技能证书', '职业培训', 0.8, 1.0, 10.0, 1),
('职业培训', '技能证书', 0.7, 1.0, 8.0, 1),
('学历教育', '职业培训', 0.9, 1.0, 15.0, 1),
('在线课程', '职业培训', 0.6, 1.0, 5.0, 1); 