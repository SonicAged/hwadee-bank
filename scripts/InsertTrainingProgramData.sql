-- 培训项目数据库示例数据插入脚本
-- 作者: Claude
-- 创建时间: 2024-09-01

-- 清空现有数据（如果需要）
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE training_feedback;
-- TRUNCATE TABLE training_resource;
-- TRUNCATE TABLE training_participant;
-- TRUNCATE TABLE training_program;
-- SET FOREIGN_KEY_CHECKS = 1;

-- 插入培训项目数据
INSERT INTO training_program (
    program_name, program_code, program_type, manager_id,
    description, content, credit_value, cost, max_participants,
    current_participants, start_time, end_time, location, status, enroll_deadline,
    create_time, update_time
) VALUES 
-- 线上培训
('Spring Boot高级开发', 'SB2024001', 1, 1,
'本课程将深入介绍Spring Boot框架的高级特性，帮助开发人员提升开发效率和系统性能。',
'1. Spring Boot核心原理\n2. 自动配置原理与定制\n3. 响应式编程\n4. WebFlux框架\n5. 安全与认证\n6. 性能优化\n7. 微服务架构整合',
2.0, 0.00, 50, 0, '2024-10-01 09:00:00', '2024-10-05 17:00:00', NULL, 0, '2024-09-25 23:59:59',
NOW(), NOW()),

('云原生架构实战', 'CN2024002', 1, 2, 
'本培训将介绍云原生架构的设计原则和最佳实践，包括容器化、微服务、DevOps等核心概念。',
'1. 云原生概念与架构\n2. Docker容器化技术\n3. Kubernetes编排平台\n4. 微服务设计模式\n5. CI/CD流水线构建\n6. 可观测性与监控\n7. 案例分析与实战',
3.0, 500.00, 30, 0, '2024-10-15 09:00:00', '2024-10-20 17:00:00', NULL, 0, '2024-10-10 23:59:59',
NOW(), NOW()),

-- 线下培训
('人工智能与大模型应用', 'AI2024003', 2, 3,
'本次培训将探讨人工智能大模型的最新发展和企业实际应用场景，帮助技术团队了解如何有效利用AI技术。',
'1. AI大模型发展现状\n2. 大模型原理与架构\n3. 企业应用场景分析\n4. 开发框架与工具介绍\n5. 模型调优与部署\n6. 案例分享\n7. 实操演练',
1.5, 1000.00, 20, 0, '2024-09-20 09:00:00', '2024-09-21 17:00:00', '北京市海淀区科技园区12号楼3层多功能厅', 0, '2024-09-15 23:59:59',
NOW(), NOW()),

('敏捷项目管理工作坊', 'PM2024004', 2, 4,
'本工作坊将通过实际案例和互动练习，帮助项目经理和团队领导掌握敏捷项目管理的核心方法和技巧。',
'1. 敏捷宣言与原则\n2. Scrum框架详解\n3. 需求管理与用户故事\n4. 敏捷估算与规划\n5. 团队建设与协作\n6. 持续改进与回顾\n7. 敏捷工具应用',
1.0, 800.00, 25, 0, '2024-09-25 09:00:00', '2024-09-26 17:00:00', '上海市浦东新区张江高科技园区18号楼2层培训室', 0, '2024-09-20 23:59:59',
NOW(), NOW()),

-- 混合培训
('全栈开发实战训练营', 'FS2024005', 3, 5,
'本训练营结合线上课程和线下实操，全面提升学员的全栈开发能力，从前端到后端，从设计到部署全覆盖。',
'1. 现代前端技术栈(Vue/React)\n2. 后端开发与API设计\n3. 数据库设计与优化\n4. 云服务与部署\n5. 性能优化\n6. 安全与测试\n7. 项目实战',
5.0, 2000.00, 30, 0, '2024-11-01 09:00:00', '2024-12-15 17:00:00', '广州市天河区科技园B区5号楼(线下部分)', 0, '2024-10-25 23:59:59',
NOW(), NOW()),

('数据分析与商业智能', 'DA2024006', 3, 6,
'本课程将帮助学员掌握数据分析和商业智能的核心技能，结合线上理论学习和线下案例研讨，提升数据驱动决策能力。',
'1. 数据分析基础\n2. 统计学原理\n3. 数据可视化技巧\n4. 商业智能工具应用\n5. 预测分析模型\n6. 数据报告与决策\n7. 行业案例分析',
2.5, 1500.00, 40, 0, '2024-10-10 09:00:00', '2024-11-20 17:00:00', '深圳市南山区科技园C区3号楼(线下部分)', 0, '2024-10-05 23:59:59',
NOW(), NOW());

-- 插入培训参与者数据
-- 注意：需要确保user_id在sys_user表中存在
INSERT INTO training_participant (
    program_id, user_id, status, enroll_time, confirm_time, complete_time, remark, create_time, update_time
) VALUES 
-- Spring Boot高级开发参与者
(1, 1, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(1, 2, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL, NULL, NOW(), NOW()),
(1, 3, 2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), '表现优秀', NOW(), NOW()),
(1, 4, 3, DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, NULL, '个人原因取消', NOW(), NOW()),

-- 云原生架构实战参与者
(2, 2, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(2, 3, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(2, 5, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),

-- 人工智能与大模型应用参与者
(3, 1, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NULL, NULL, NOW(), NOW()),
(3, 3, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NULL, NULL, NOW(), NOW()),
(3, 5, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NULL, NULL, NOW(), NOW()),
(3, 6, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),

-- 敏捷项目管理工作坊参与者
(4, 4, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(4, 5, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(4, 6, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),

-- 全栈开发实战训练营参与者
(5, 1, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(5, 2, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(5, 7, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),

-- 数据分析与商业智能参与者
(6, 3, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(6, 5, 0, NOW(), NULL, NULL, NULL, NOW(), NOW()),
(6, 8, 0, NOW(), NULL, NULL, NULL, NOW(), NOW());

-- 更新培训项目当前参与人数
UPDATE training_program SET current_participants = 4 WHERE program_id = 1;

UPDATE training_program SET current_participants = 3 WHERE program_id = 2;

UPDATE training_program SET current_participants = 4 WHERE program_id = 3;

UPDATE training_program SET current_participants = 3 WHERE program_id = 4;

UPDATE training_program SET current_participants = 3 WHERE program_id = 5;

UPDATE training_program SET current_participants = 3 WHERE program_id = 6;

-- 插入培训资源数据
INSERT INTO training_resource (
    program_id, resource_name, resource_type, file_url, file_size, create_time, update_time
) VALUES 
-- Spring Boot高级开发资源
(1, 'Spring Boot高级开发课程大纲', 'pdf', '/uploads/resources/sb2024001/outline.pdf', 1024, NOW(), NOW()),
(1, 'Spring Boot原理详解', 'pdf', '/uploads/resources/sb2024001/principle.pdf', 2048, NOW(), NOW()),
(1, '响应式编程示例代码', 'zip', '/uploads/resources/sb2024001/reactive-demo.zip', 5120, NOW(), NOW()),
(1, '课程视频第一讲', 'video', '/uploads/resources/sb2024001/video-01.mp4', 51200, NOW(), NOW()),

-- 云原生架构实战资源
(2, '云原生架构概述', 'ppt', '/uploads/resources/cn2024002/overview.pptx', 3072, NOW(), NOW()),
(2, 'Docker实践指南', 'pdf', '/uploads/resources/cn2024002/docker-guide.pdf', 4096, NOW(), NOW()),
(2, 'Kubernetes入门到精通', 'pdf', '/uploads/resources/cn2024002/k8s-mastery.pdf', 8192, NOW(), NOW()),
(2, '微服务架构设计模式', 'pdf', '/uploads/resources/cn2024002/microservice-patterns.pdf', 6144, NOW(), NOW()),

-- 人工智能与大模型应用资源
(3, 'AI大模型原理', 'ppt', '/uploads/resources/ai2024003/llm-principles.pptx', 5120, NOW(), NOW()),
(3, 'OpenAI API使用指南', 'pdf', '/uploads/resources/ai2024003/openai-guide.pdf', 2048, NOW(), NOW()),
(3, '企业AI应用案例集', 'pdf', '/uploads/resources/ai2024003/enterprise-cases.pdf', 4096, NOW(), NOW()),

-- 敏捷项目管理工作坊资源
(4, '敏捷宣言与原则', 'ppt', '/uploads/resources/pm2024004/agile-manifesto.pptx', 1536, NOW(), NOW()),
(4, 'Scrum指南2023版', 'pdf', '/uploads/resources/pm2024004/scrum-guide-2023.pdf', 1024, NOW(), NOW()),
(4, '用户故事编写模板', 'docx', '/uploads/resources/pm2024004/user-story-template.docx', 512, NOW(), NOW()),
(4, '敏捷估算工具包', 'zip', '/uploads/resources/pm2024004/estimation-toolkit.zip', 2048, NOW(), NOW()),

-- 全栈开发实战训练营资源
(5, 'Vue3完全指南', 'pdf', '/uploads/resources/fs2024005/vue3-guide.pdf', 5120, NOW(), NOW()),
(5, 'Node.js后端开发实践', 'pdf', '/uploads/resources/fs2024005/nodejs-backend.pdf', 4096, NOW(), NOW()),
(5, '数据库设计最佳实践', 'pdf', '/uploads/resources/fs2024005/database-design.pdf', 3072, NOW(), NOW()),
(5, '全栈项目源码', 'zip', '/uploads/resources/fs2024005/fullstack-project.zip', 10240, NOW(), NOW()),
(5, '课程视频合集', 'video', '/uploads/resources/fs2024005/video-series.mp4', 102400, NOW(), NOW()),

-- 数据分析与商业智能资源
(6, '数据分析基础', 'ppt', '/uploads/resources/da2024006/data-analysis-basics.pptx', 2048, NOW(), NOW()),
(6, 'Python数据分析实战', 'pdf', '/uploads/resources/da2024006/python-data-analysis.pdf', 4096, NOW(), NOW()),
(6, '数据可视化技巧', 'pdf', '/uploads/resources/da2024006/data-visualization.pdf', 3584, NOW(), NOW()),
(6, '案例数据集', 'csv', '/uploads/resources/da2024006/case-dataset.csv', 8192, NOW(), NOW()),
(6, 'Tableau使用指南', 'pdf', '/uploads/resources/da2024006/tableau-guide.pdf', 2560, NOW(), NOW());

-- 插入培训反馈数据
INSERT INTO training_feedback (
    program_id, user_id, rating, content, create_time, update_time
) VALUES 
-- Spring Boot高级开发反馈
(1, 3, 5, '课程内容非常丰富，讲师讲解深入浅出，收获很大！', NOW(), NOW()),
(1, 2, 4, '内容很有深度，但希望能有更多的实践环节。', NOW(), NOW()),

-- 云原生架构实战反馈
(2, 2, 5, '培训内容紧跟技术前沿，实用性很强，很满意！', NOW(), NOW()),

-- 人工智能与大模型应用反馈
(3, 1, 4, '对AI大模型有了更深入的理解，案例分析很有启发性。', NOW(), NOW()),
(3, 3, 5, '培训质量很高，讲师经验丰富，互动环节设计得很好。', NOW(), NOW());

-- 更新培训项目状态
-- 可选：根据实际情况设置不同的培训项目状态
UPDATE training_program SET status = 1 WHERE program_id = 3;

UPDATE training_program SET status = 2 WHERE program_id = 1; 