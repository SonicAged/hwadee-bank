-- 终身学习学分银行平台数据库脚本
-- 数据库名称：BankAnalysis
-- MySQL版本：8.0
-- 用户名：bank，密码：123456

CREATE DATABASE IF NOT EXISTS BankAnalysis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BankAnalysis;

-- ====================================
-- 1. 权限管理系统相关表
-- ====================================

-- 用户表
CREATE TABLE sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(MD5加密)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(200) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    birth_date DATE COMMENT '出生日期',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

-- 角色表
CREATE TABLE sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) UNIQUE NOT NULL COMMENT '角色名称',
    role_key VARCHAR(50) UNIQUE NOT NULL COMMENT '角色权限字符串',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='角色表';

-- 权限表
CREATE TABLE sys_permission (
    permission_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_key VARCHAR(100) UNIQUE NOT NULL COMMENT '权限标识',
    permission_type TINYINT NOT NULL COMMENT '权限类型：1-目录，2-菜单，3-按钮',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(200) COMMENT '组件路径',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '显示顺序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE
) COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE sys_role_permission (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES sys_permission(permission_id) ON DELETE CASCADE
) COMMENT='角色权限关联表';

-- ====================================
-- 2. 学分管理中心相关表
-- ====================================

-- 学分账户表
CREATE TABLE credit_account (
    account_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账户ID',
    user_id BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    total_credits DECIMAL(10,2) DEFAULT 0.00 COMMENT '总学分',
    available_credits DECIMAL(10,2) DEFAULT 0.00 COMMENT '可用学分',
    frozen_credits DECIMAL(10,2) DEFAULT 0.00 COMMENT '冻结学分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='学分账户表';

-- 学分记录表
CREATE TABLE credit_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    credit_type VARCHAR(50) NOT NULL COMMENT '学分类型：学历教育、职业培训、技能证书等',
    credit_source VARCHAR(100) NOT NULL COMMENT '学分来源',
    credit_amount DECIMAL(10,2) NOT NULL COMMENT '学分数量',
    operation_type TINYINT NOT NULL COMMENT '操作类型：1-获得，2-消费，3-转换',
    status TINYINT DEFAULT 1 COMMENT '状态：0-无效，1-有效，2-待审核',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='学分记录表';

-- 学分转换规则表
CREATE TABLE credit_conversion_rule (
    rule_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规则ID',
    source_type VARCHAR(50) NOT NULL COMMENT '源学分类型',
    target_type VARCHAR(50) NOT NULL COMMENT '目标学分类型',
    conversion_rate DECIMAL(5,2) NOT NULL COMMENT '转换比例',
    min_credits DECIMAL(10,2) DEFAULT 0.00 COMMENT '最小转换学分',
    max_credits DECIMAL(10,2) COMMENT '最大转换学分',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='学分转换规则表';

-- ====================================
-- 3. 学习资源管理相关表
-- ====================================

-- 资源分类表
CREATE TABLE resource_category (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    level TINYINT DEFAULT 1 COMMENT '分类级别',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='资源分类表';

-- 学习资源表
CREATE TABLE learning_resource (
    resource_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    resource_name VARCHAR(200) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(50) NOT NULL COMMENT '资源类型：课程、教材、课件、实训项目',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    description TEXT COMMENT '资源描述',
    content_url VARCHAR(500) COMMENT '资源内容URL',
    thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
    file_size BIGINT COMMENT '文件大小(字节)',
    duration INT COMMENT '时长(分钟)',
    difficulty_level TINYINT COMMENT '难度级别：1-初级，2-中级，3-高级',
    credit_value DECIMAL(5,2) COMMENT '学分价值',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分(0-5)',
    rating_count INT DEFAULT 0 COMMENT '评分人数',
    uploader_id BIGINT NOT NULL COMMENT '上传者ID',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架，2-审核中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES resource_category(category_id),
    FOREIGN KEY (uploader_id) REFERENCES sys_user(user_id)
) COMMENT='学习资源表';

-- 资源评价表
CREATE TABLE resource_evaluation (
    evaluation_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    rating TINYINT NOT NULL COMMENT '评分(1-5)',
    comment TEXT COMMENT '评价内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_resource_user (resource_id, user_id)
) COMMENT='资源评价表';

-- ====================================
-- 4. 课程与培训管理相关表
-- ====================================

-- 课程表
CREATE TABLE course (
    course_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    course_name VARCHAR(200) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(50) UNIQUE NOT NULL COMMENT '课程编码',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    instructor_id BIGINT NOT NULL COMMENT '授课教师ID',
    description TEXT COMMENT '课程描述',
    syllabus TEXT COMMENT '课程大纲',
    credit_hours INT NOT NULL COMMENT '学时',
    credit_value DECIMAL(5,2) NOT NULL COMMENT '学分值',
    max_students INT COMMENT '最大学生数',
    current_students INT DEFAULT 0 COMMENT '当前学生数',
    start_date DATE COMMENT '开课日期',
    end_date DATE COMMENT '结课日期',
    status TINYINT DEFAULT 1 COMMENT '状态：0-关闭，1-开放，2-满员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES resource_category(category_id),
    FOREIGN KEY (instructor_id) REFERENCES sys_user(user_id)
) COMMENT='课程表';

-- 培训项目表
CREATE TABLE training_program (
    program_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    program_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    program_type TINYINT NOT NULL COMMENT '项目类型：1-线上，2-线下，3-混合',
    organizer_id BIGINT NOT NULL COMMENT '组织者ID',
    description TEXT COMMENT '项目描述',
    location VARCHAR(200) COMMENT '培训地点',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    max_participants INT COMMENT '最大参与人数',
    current_participants INT DEFAULT 0 COMMENT '当前参与人数',
    credit_value DECIMAL(5,2) COMMENT '学分值',
    cost DECIMAL(10,2) DEFAULT 0.00 COMMENT '费用',
    status TINYINT DEFAULT 1 COMMENT '状态：0-取消，1-报名中，2-进行中，3-已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (organizer_id) REFERENCES sys_user(user_id)
) COMMENT='培训项目表';

-- 课程报名表
CREATE TABLE course_enrollment (
    enrollment_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报名ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    enrollment_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-已退课，1-已报名，2-学习中，3-已完成',
    completion_time DATETIME COMMENT '完成时间',
    final_score DECIMAL(5,2) COMMENT '最终成绩',
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_course_user (course_id, user_id)
) COMMENT='课程报名表';

-- 学习进度表
CREATE TABLE learning_progress (
    progress_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    resource_id BIGINT COMMENT '资源ID',
    course_id BIGINT COMMENT '课程ID',
    progress_percentage DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度百分比',
    learning_time INT DEFAULT 0 COMMENT '学习时长(分钟)',
    last_position VARCHAR(100) COMMENT '最后学习位置',
    status TINYINT DEFAULT 1 COMMENT '状态：1-学习中，2-已暂停，3-已完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
) COMMENT='学习进度表';

-- ====================================
-- 5. 认证与评估系统相关表
-- ====================================

-- 评估标准表
CREATE TABLE evaluation_standard (
    standard_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标准ID',
    standard_name VARCHAR(100) NOT NULL COMMENT '标准名称',
    category VARCHAR(50) NOT NULL COMMENT '评估类别',
    criteria TEXT NOT NULL COMMENT '评估标准',
    credit_range VARCHAR(50) COMMENT '学分范围',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='评估标准表';

-- 学分认证申请表
CREATE TABLE credit_application (
    application_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    user_id BIGINT NOT NULL COMMENT '申请用户ID',
    application_type VARCHAR(50) NOT NULL COMMENT '申请类型',
    achievement_name VARCHAR(200) NOT NULL COMMENT '成果名称',
    achievement_description TEXT COMMENT '成果描述',
    applied_credits DECIMAL(5,2) NOT NULL COMMENT '申请学分',
    evidence_files JSON COMMENT '证明材料文件',
    current_step INT DEFAULT 1 COMMENT '当前审核步骤',
    status TINYINT DEFAULT 1 COMMENT '状态：1-待审核，2-审核中，3-通过，4-拒绝，5-撤回',
    rejection_reason TEXT COMMENT '拒绝原因',
    approved_credits DECIMAL(5,2) COMMENT '批准学分',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    review_time DATETIME COMMENT '审核完成时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='学分认证申请表';

-- 审核流程表
CREATE TABLE approval_process (
    process_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流程ID',
    application_id BIGINT NOT NULL COMMENT '申请ID',
    reviewer_id BIGINT NOT NULL COMMENT '审核员ID',
    step_number INT NOT NULL COMMENT '审核步骤',
    step_name VARCHAR(100) NOT NULL COMMENT '步骤名称',
    review_result TINYINT COMMENT '审核结果：1-通过，2-拒绝，3-退回修改',
    review_comment TEXT COMMENT '审核意见',
    review_time DATETIME COMMENT '审核时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (application_id) REFERENCES credit_application(application_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES sys_user(user_id)
) COMMENT='审核流程表';

-- 证书表
CREATE TABLE certificate (
    certificate_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '证书ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    certificate_type VARCHAR(50) NOT NULL COMMENT '证书类型',
    certificate_name VARCHAR(200) NOT NULL COMMENT '证书名称',
    certificate_number VARCHAR(100) UNIQUE NOT NULL COMMENT '证书编号',
    credit_value DECIMAL(5,2) NOT NULL COMMENT '学分值',
    issue_date DATE NOT NULL COMMENT '颁发日期',
    expiry_date DATE COMMENT '过期日期',
    certificate_url VARCHAR(500) COMMENT '证书文件URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0-已撤销，1-有效，2-已过期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='证书表';

-- ====================================
-- 6. 日志审计系统相关表
-- ====================================

-- 操作日志表
CREATE TABLE operation_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_name VARCHAR(100) NOT NULL COMMENT '操作名称',
    method VARCHAR(10) COMMENT '请求方法',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    request_ip VARCHAR(45) COMMENT '请求IP',
    user_agent VARCHAR(500) COMMENT '用户代理',
    execution_time BIGINT COMMENT '执行时间(毫秒)',
    status TINYINT COMMENT '状态：0-失败，1-成功',
    error_message TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE SET NULL
) COMMENT='操作日志表';

-- 系统日志表
CREATE TABLE system_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    log_level VARCHAR(10) NOT NULL COMMENT '日志级别',
    logger_name VARCHAR(200) COMMENT 'Logger名称',
    message TEXT NOT NULL COMMENT '日志信息',
    exception_info TEXT COMMENT '异常信息',
    server_name VARCHAR(100) COMMENT '服务器名称',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='系统日志表';

-- 审计报表表
CREATE TABLE audit_report (
    report_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报表ID',
    report_name VARCHAR(200) NOT NULL COMMENT '报表名称',
    report_type VARCHAR(50) NOT NULL COMMENT '报表类型',
    filter_conditions JSON COMMENT '筛选条件',
    report_data JSON COMMENT '报表数据',
    generated_by BIGINT NOT NULL COMMENT '生成人ID',
    generate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    FOREIGN KEY (generated_by) REFERENCES sys_user(user_id)
) COMMENT='审计报表表';

-- ====================================
-- 插入示例数据
-- ====================================

-- 插入角色数据
INSERT INTO sys_role (role_name, role_key, description) VALUES
('超级管理员', 'ROLE_ADMIN', '系统超级管理员，拥有所有权限'),
('机构用户', 'ROLE_INSTITUTION', '教育机构用户，可管理本机构相关业务'),
('教师', 'ROLE_TEACHER', '教师用户，可管理课程和资源'),
('学生', 'ROLE_STUDENT', '学生用户，可学习课程和申请学分'),
('审核员', 'ROLE_AUDITOR', '审核员，负责学分认证审核');

-- 插入权限数据
INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon) VALUES
('系统管理', 'system', 1, 0, '/system', '', 'Setting'),
('用户管理', 'system:user', 2, 1, '/system/user', 'system/User', 'User'),
('角色管理', 'system:role', 2, 1, '/system/role', 'system/Role', 'UserFilled'),
('权限管理', 'system:permission', 2, 1, '/system/permission', 'system/Permission', 'Lock'),
('学分管理', 'credit', 1, 0, '/credit', '', 'Coin'),
('学分账户', 'credit:account', 2, 5, '/credit/account', 'credit/Account', 'Wallet'),
('学分记录', 'credit:record', 2, 5, '/credit/record', 'credit/Record', 'Document'),
('学分申请', 'credit:application', 2, 5, '/credit/application', 'credit/Application', 'DocumentAdd'),
('资源管理', 'resource', 1, 0, '/resource', '', 'Files'),
('资源库', 'resource:library', 2, 9, '/resource/library', 'resource/Library', 'FolderOpened'),
('资源分类', 'resource:category', 2, 9, '/resource/category', 'resource/Category', 'Menu'),
('课程管理', 'course', 1, 0, '/course', '', 'Reading'),
('课程列表', 'course:list', 2, 13, '/course/list', 'course/List', 'List'),
('培训项目', 'course:training', 2, 13, '/course/training', 'course/Training', 'School'),
('审计管理', 'audit', 1, 0, '/audit', '', 'Monitor'),
('操作日志', 'audit:operation', 2, 16, '/audit/operation', 'audit/Operation', 'Document'),
('系统日志', 'audit:system', 2, 16, '/audit/system', 'audit/System', 'Warning');

-- 插入用户数据
INSERT INTO sys_user (username, password, real_name, email, phone, gender) VALUES
('admin', MD5('123456'), '系统管理员', 'admin@example.com', '13800138000', 1),
('teacher1', MD5('123456'), '张老师', 'teacher1@example.com', '13800138001', 1),
('teacher2', MD5('123456'), '李老师', 'teacher2@example.com', '13800138002', 2),
('student1', MD5('123456'), '王小明', 'student1@example.com', '13800138003', 1),
('student2', MD5('123456'), '李小红', 'student2@example.com', '13800138004', 2),
('auditor1', MD5('123456'), '审核员1', 'auditor1@example.com', '13800138005', 1);

-- 插入用户角色关联数据
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), -- admin - 超级管理员
(2, 3), -- teacher1 - 教师
(3, 3), -- teacher2 - 教师
(4, 4), -- student1 - 学生
(5, 4), -- student2 - 学生
(6, 5); -- auditor1 - 审核员

-- 插入角色权限关联数据（超级管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, permission_id FROM sys_permission;

-- 教师角色权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(3, 9), (3, 10), (3, 11), -- 资源管理相关
(3, 13), (3, 14), (3, 15); -- 课程管理相关

-- 学生角色权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(4, 5), (4, 6), (4, 7), (4, 8), -- 学分管理相关
(4, 9), (4, 10), -- 资源查看
(4, 13), (4, 14); -- 课程查看

-- 审核员角色权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(5, 5), (5, 7), (5, 8); -- 学分审核相关

-- 插入资源分类数据
INSERT INTO resource_category (category_name, parent_id, level) VALUES
('计算机科学', 0, 1),
('编程语言', 1, 2),
('数据库', 1, 2),
('网络技术', 1, 2),
('人工智能', 1, 2),
('管理学', 0, 1),
('项目管理', 6, 2),
('人力资源', 6, 2);

-- 插入学分账户数据
INSERT INTO credit_account (user_id, total_credits, available_credits) VALUES
(4, 120.00, 120.00), -- student1
(5, 80.50, 80.50);   -- student2

-- 插入学分转换规则数据
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits) VALUES
('学历教育', '职业培训', 1.20, 1.00, 100.00),
('职业培训', '技能证书', 0.80, 2.00, 50.00),
('技能证书', '学历教育', 1.50, 1.00, 30.00);

-- 插入评估标准数据
INSERT INTO evaluation_standard (standard_name, category, criteria, credit_range) VALUES
('本科课程标准', '学历教育', '完成本科阶段课程学习，通过考试', '3.0-6.0'),
('职业技能培训标准', '职业培训', '参加职业技能培训并获得结业证书', '1.0-3.0'),
('专业技能证书标准', '技能证书', '获得国家认可的专业技能证书', '2.0-5.0');

-- 插入学习资源数据
INSERT INTO learning_resource (resource_name, resource_type, category_id, description, difficulty_level, credit_value, uploader_id) VALUES
('Java基础编程', '课程', 2, 'Java编程语言基础知识讲解', 1, 3.0, 2),
('MySQL数据库设计', '课程', 3, 'MySQL数据库设计与优化', 2, 2.5, 2),
('项目管理实战', '课程', 7, '项目管理理论与实践', 2, 4.0, 3),
('Python编程入门', '教材', 2, 'Python编程语言入门教材', 1, 2.0, 2);

-- 插入课程数据
INSERT INTO course (course_name, course_code, category_id, instructor_id, description, credit_hours, credit_value, max_students, start_date, end_date) VALUES
('Java高级编程', 'CS001', 2, 2, 'Java高级编程技术与框架应用', 48, 3.0, 30, '2024-03-01', '2024-06-30'),
('数据库系统原理', 'CS002', 3, 3, '数据库系统的基本原理和设计方法', 40, 2.5, 25, '2024-03-15', '2024-07-15');

-- 插入培训项目数据
INSERT INTO training_program (program_name, program_type, organizer_id, description, start_time, end_time, max_participants, credit_value, cost) VALUES
('Spring Boot实战培训', 1, 2, 'Spring Boot框架实战应用培训', '2024-04-01 09:00:00', '2024-04-03 17:00:00', 20, 1.5, 500.00),
('AI技术应用研讨会', 2, 3, '人工智能技术在各行业的应用', '2024-05-10 14:00:00', '2024-05-10 18:00:00', 50, 0.5, 0.00);

-- 创建索引以提高查询性能
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_email ON sys_user(email);
CREATE INDEX idx_credit_record_user_id ON credit_record(user_id);
CREATE INDEX idx_credit_record_create_time ON credit_record(create_time);
CREATE INDEX idx_resource_category ON learning_resource(category_id);
CREATE INDEX idx_resource_uploader ON learning_resource(uploader_id);
CREATE INDEX idx_operation_log_user_id ON operation_log(user_id);
CREATE INDEX idx_operation_log_create_time ON operation_log(create_time); 