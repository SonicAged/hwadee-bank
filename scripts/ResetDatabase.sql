-- ====================================
-- 终身学习学分银行平台数据库重建脚本
-- 数据库名称：BankAnalysis
-- MySQL版本：8.0+
-- 创建时间：2024-12-28
-- 说明：此脚本会删除现有数据库并重新创建全新的数据库结构
-- ====================================

-- 删除现有数据库用户（如果存在）
DROP USER IF EXISTS 'bank'@'%';

-- 删除现有数据库（如果存在）
DROP DATABASE IF EXISTS BankAnalysis;

-- 创建新的数据库，确保使用utf8mb4字符集和utf8mb4_unicode_ci排序规则
CREATE DATABASE BankAnalysis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建数据库用户
CREATE USER 'bank'@'%' IDENTIFIED BY '123456';

-- 授予数据库权限
GRANT ALL PRIVILEGES ON BankAnalysis.* TO 'bank'@'%';
FLUSH PRIVILEGES;

-- 使用数据库
USE BankAnalysis;

-- 设置连接的字符集为utf8mb4
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_database=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_server=utf8mb4;

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

-- 学分申请表
CREATE TABLE credit_application (
    application_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    application_type VARCHAR(50) NOT NULL COMMENT '申请类型：技能证书、职业培训、学历教育等',
    achievement_name VARCHAR(200) NOT NULL COMMENT '成果名称',
    achievement_description TEXT COMMENT '成果描述',
    applied_credits DECIMAL(10,2) NOT NULL COMMENT '申请学分',
    evidence_files JSON COMMENT '证据文件列表',
    current_step INT DEFAULT 1 COMMENT '当前审核步骤',
    status TINYINT DEFAULT 1 COMMENT '申请状态：1-待审核，2-审核中，3-通过，4-拒绝',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    approve_time DATETIME COMMENT '审核时间',
    remark TEXT COMMENT '备注',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='学分申请表';

-- ====================================
-- 3. 资源管理系统相关表
-- ====================================

-- 资源分类表
CREATE TABLE resource_category (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父级分类ID，0表示顶级分类',
    category_path VARCHAR(500) COMMENT '分类路径，用逗号分隔',
    level INT DEFAULT 1 COMMENT '分类层级',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    icon VARCHAR(100) COMMENT '分类图标',
    description TEXT COMMENT '分类描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='资源分类表';

-- 学习资源表（完整版）
CREATE TABLE learning_resource (
    resource_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    resource_name VARCHAR(200) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(50) NOT NULL COMMENT '资源类型：课程、教材、课件、实训项目',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    subject VARCHAR(100) COMMENT '学科领域',
    keywords VARCHAR(500) COMMENT '关键词，用逗号分隔',
    description TEXT COMMENT '资源描述',
    content_url VARCHAR(500) COMMENT '资源内容URL',
    thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小(字节)',
    duration INT DEFAULT 0 COMMENT '时长(分钟)',
    difficulty_level TINYINT DEFAULT 1 COMMENT '难度级别：1-初级，2-中级，3-高级',
    credit_value DECIMAL(5,2) DEFAULT 0.00 COMMENT '学分价值',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏次数',
    rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '平均评分(0-5)',
    rating_count INT DEFAULT 0 COMMENT '评分人数',
    uploader_id BIGINT NOT NULL COMMENT '上传者ID',
    tags VARCHAR(200) COMMENT '标签，用逗号分隔',
    prerequisites TEXT COMMENT '前置要求',
    learning_objectives TEXT COMMENT '学习目标',
    status TINYINT DEFAULT 2 COMMENT '状态：0-下架，1-上架，2-审核中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_resource_type (resource_type),
    INDEX idx_difficulty_level (difficulty_level),
    INDEX idx_status (status),
    INDEX idx_uploader_id (uploader_id),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (category_id) REFERENCES resource_category(category_id),
    FOREIGN KEY (uploader_id) REFERENCES sys_user(user_id)
) COMMENT='学习资源表';

-- 资源评价表
CREATE TABLE resource_review (
    review_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    rating TINYINT NOT NULL COMMENT '评分(1-5)',
    review_content TEXT COMMENT '评价内容',
    helpful_count INT DEFAULT 0 COMMENT '有用数',
    status TINYINT DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_resource_id (resource_id),
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    UNIQUE KEY uk_resource_user (resource_id, user_id),
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='资源评价表';

-- 用户学习轨迹表
CREATE TABLE user_learning_track (
    track_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '轨迹ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    action_type VARCHAR(50) NOT NULL COMMENT '行为类型：view-浏览，download-下载，favorite-收藏，complete-完成',
    progress INT DEFAULT 0 COMMENT '学习进度百分比',
    duration_minutes INT DEFAULT 0 COMMENT '学习时长(分钟)',
    score DECIMAL(5,2) COMMENT '学习得分',
    status TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_resource_id (resource_id),
    INDEX idx_action_type (action_type),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE
) COMMENT='用户学习轨迹表';

-- 资源推荐表
CREATE TABLE resource_recommendation (
    recommendation_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '推荐ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    recommendation_type VARCHAR(50) NOT NULL COMMENT '推荐类型：collaborative-协同过滤，content-内容推荐，popular-热门推荐',
    score DECIMAL(5,4) DEFAULT 0.0000 COMMENT '推荐分数',
    reason TEXT COMMENT '推荐理由',
    status TINYINT DEFAULT 1 COMMENT '状态：0-失效，1-有效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_resource_id (resource_id),
    INDEX idx_recommendation_type (recommendation_type),
    INDEX idx_score (score),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE
) COMMENT='资源推荐表';

-- 用户收藏表
CREATE TABLE user_favorite (
    favorite_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    favorite_type VARCHAR(50) DEFAULT 'resource' COMMENT '收藏类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_resource_id (resource_id),
    UNIQUE KEY uk_user_resource (user_id, resource_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE
) COMMENT='用户收藏表';

-- 资源标签表
CREATE TABLE resource_tag (
    tag_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_color VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
    use_count INT DEFAULT 0 COMMENT '使用次数',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_tag_name (tag_name)
) COMMENT='资源标签表';

-- 资源标签关联表
CREATE TABLE resource_tag_relation (
    relation_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_resource_id (resource_id),
    INDEX idx_tag_id (tag_id),
    UNIQUE KEY uk_resource_tag (resource_id, tag_id),
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES resource_tag(tag_id) ON DELETE CASCADE
) COMMENT='资源标签关联表';

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
    program_type VARCHAR(50) NOT NULL COMMENT '项目类型：技能培训、认证培训、职业发展等',
    organizer VARCHAR(100) NOT NULL COMMENT '主办机构',
    description TEXT COMMENT '项目描述',
    duration_days INT NOT NULL COMMENT '培训天数',
    credit_value DECIMAL(5,2) NOT NULL COMMENT '学分值',
    max_participants INT COMMENT '最大参与人数',
    current_participants INT DEFAULT 0 COMMENT '当前参与人数',
    registration_start_date DATE COMMENT '报名开始日期',
    registration_end_date DATE COMMENT '报名截止日期',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '培训费用',
    status TINYINT DEFAULT 1 COMMENT '状态：0-关闭，1-开放，2-满员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='培训项目表';

-- 课程选课表
CREATE TABLE course_enrollment (
    enrollment_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '选课ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    user_id BIGINT NOT NULL COMMENT '学生ID',
    enrollment_date DATE NOT NULL COMMENT '选课日期',
    status TINYINT DEFAULT 1 COMMENT '状态：0-已退课，1-正常',
    final_grade DECIMAL(5,2) COMMENT '最终成绩',
    credits_earned DECIMAL(5,2) COMMENT '获得学分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_course_user (course_id, user_id)
) COMMENT='课程选课表';

-- 学习进度表
CREATE TABLE learning_progress (
    progress_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT COMMENT '课程ID',
    resource_id BIGINT COMMENT '资源ID',
    progress_percentage DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度百分比',
    learning_time INT DEFAULT 0 COMMENT '学习时长(分钟)',
    last_access_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
    completion_date DATETIME COMMENT '完成日期',
    status TINYINT DEFAULT 1 COMMENT '状态：0-未开始，1-学习中，2-已完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resource(resource_id) ON DELETE CASCADE
) COMMENT='学习进度表';

-- 证书表
CREATE TABLE certificate (
    certificate_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '证书ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    certificate_type VARCHAR(50) NOT NULL COMMENT '证书类型：课程证书、培训证书、技能证书等',
    certificate_name VARCHAR(200) NOT NULL COMMENT '证书名称',
    issuing_organization VARCHAR(200) NOT NULL COMMENT '发证机构',
    issue_date DATE NOT NULL COMMENT '发证日期',
    expiry_date DATE COMMENT '有效期至',
    certificate_number VARCHAR(100) UNIQUE NOT NULL COMMENT '证书编号',
    certificate_url VARCHAR(500) COMMENT '证书文件URL',
    verification_status TINYINT DEFAULT 1 COMMENT '验证状态：0-未验证，1-已验证，2-验证失败',
    status TINYINT DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='证书表';

-- ====================================
-- 5. 审核与日志管理相关表
-- ====================================

-- 审核流程表
CREATE TABLE approval_process (
    process_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流程ID',
    application_id BIGINT NOT NULL COMMENT '申请ID',
    step_number INT NOT NULL COMMENT '步骤编号',
    step_name VARCHAR(100) NOT NULL COMMENT '步骤名称',
    approver_id BIGINT NOT NULL COMMENT '审批人ID',
    approval_status TINYINT NOT NULL COMMENT '审批状态：1-待审批，2-通过，3-拒绝',
    approval_time DATETIME COMMENT '审批时间',
    approval_comment TEXT COMMENT '审批意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (application_id) REFERENCES credit_application(application_id) ON DELETE CASCADE,
    FOREIGN KEY (approver_id) REFERENCES sys_user(user_id) ON DELETE CASCADE
) COMMENT='审核流程表';

-- 操作日志表
CREATE TABLE operation_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    module VARCHAR(50) NOT NULL COMMENT '模块名称',
    operation VARCHAR(100) NOT NULL COMMENT '操作名称',
    method VARCHAR(10) NOT NULL COMMENT '请求方法',
    request_url VARCHAR(200) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    response_time INT COMMENT '响应时间(毫秒)',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    status TINYINT DEFAULT 1 COMMENT '状态：0-失败，1-成功',
    error_message TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE SET NULL
) COMMENT='操作日志表';

-- 系统日志表
CREATE TABLE system_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    log_level VARCHAR(20) NOT NULL COMMENT '日志级别：DEBUG、INFO、WARN、ERROR',
    log_message TEXT NOT NULL COMMENT '日志消息',
    class_name VARCHAR(200) COMMENT '类名',
    method_name VARCHAR(100) COMMENT '方法名',
    line_number INT COMMENT '行号',
    thread_name VARCHAR(100) COMMENT '线程名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='系统日志表';

-- ====================================
-- 6. 索引创建
-- ====================================

-- 用户相关索引
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_email ON sys_user(email);
CREATE INDEX idx_user_status ON sys_user(status);

-- 学分相关索引
CREATE INDEX idx_credit_record_user_id ON credit_record(user_id);
CREATE INDEX idx_credit_record_create_time ON credit_record(create_time);
CREATE INDEX idx_credit_application_user_id ON credit_application(user_id);
CREATE INDEX idx_credit_application_status ON credit_application(status);

-- 日志相关索引
CREATE INDEX idx_operation_log_user_id ON operation_log(user_id);
CREATE INDEX idx_operation_log_create_time ON operation_log(create_time);
CREATE INDEX idx_system_log_create_time ON system_log(create_time);

-- ====================================
-- 7. 基础数据插入
-- ====================================

-- 插入管理员用户
INSERT INTO sys_user (username, password, real_name, email, phone, gender, status) VALUES
('admin', MD5('admin123'), 'Administrator', 'admin@bankanalysis.com', '18888888888', 1, 1),
('teacher1', MD5('123456'), 'Prof. Zhang', 'teacher1@bankanalysis.com', '18888888801', 1, 1),
('teacher2', MD5('123456'), 'Teacher Li', 'teacher2@bankanalysis.com', '18888888802', 2, 1);

-- 插入系统角色
INSERT INTO sys_role (role_name, role_key, description) VALUES
('Super Admin', 'admin', 'System super administrator with all permissions'),
('Teacher', 'teacher', 'Teacher role, can manage courses and resources'),
('Student', 'student', 'Student role, can learn courses and resources');

-- 插入基础权限
INSERT INTO sys_permission (permission_name, permission_key, permission_type, parent_id, path, component, icon, sort_order) VALUES
('System Management', 'system', 1, 0, '/system', NULL, 'Setting', 1),
('User Management', 'system:user', 2, 1, '/system/user', 'system/User', 'User', 1),
('Role Management', 'system:role', 2, 1, '/system/role', 'system/Role', 'UserFilled', 2),
('Permission Management', 'system:permission', 2, 1, '/system/permission', 'system/Permission', 'Key', 3),
('Credit Management', 'credit', 1, 0, '/credit', NULL, 'CreditCard', 2),
('Credit Account', 'credit:account', 2, 5, '/credit/account', 'credit/Account', 'Wallet', 1),
('Credit Record', 'credit:record', 2, 5, '/credit/record', 'credit/Record', 'Document', 2),
('Credit Application', 'credit:application', 2, 5, '/credit/application', 'credit/Application', 'Edit', 3),
('Resource Management', 'resource', 1, 0, '/resource', NULL, 'Folder', 3),
('Resource Library', 'resource:library', 2, 9, '/resource/library', 'resource/Library', 'Files', 1),
('Resource Category', 'resource:category', 2, 9, '/resource/category', 'resource/Category', 'Menu', 2);

-- 绑定管理员角色权限
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), (2, 2), (3, 2);

-- 插入资源分类数据
INSERT INTO resource_category (category_name, parent_id, category_path, level, sort_order, description) VALUES
('Computer Science', 0, '1', 1, 1, 'Computer Science Related Resources'),
('Programming Languages', 1, '1,2', 2, 1, 'Various Programming Language Learning Resources'),
('Java', 2, '1,2,3', 3, 1, 'Java Programming Language'),
('Python', 2, '1,2,4', 3, 2, 'Python Programming Language'),
('JavaScript', 2, '1,2,5', 3, 3, 'JavaScript Programming Language'),
('Frontend Technology', 1, '1,6', 2, 2, 'Frontend Development Technology'),
('React', 6, '1,6,7', 3, 1, 'React Frontend Framework'),
('Vue', 6, '1,6,8', 3, 2, 'Vue Frontend Framework'),
('Database', 1, '1,9', 2, 3, 'Database Technology'),
('MySQL', 9, '1,9,10', 3, 1, 'MySQL Database'),
('Software Engineering', 0, '11', 1, 2, 'Software Engineering Methods and Practices'),
('Project Management', 11, '11,12', 2, 1, 'Project Management Knowledge'),
('Testing Technology', 11, '11,13', 2, 2, 'Software Testing Technology'),
('Artificial Intelligence', 0, '14', 1, 3, 'AI Related Technology'),
('Machine Learning', 14, '14,15', 2, 1, 'Machine Learning Technology'),
('Deep Learning', 14, '14,16', 2, 2, 'Deep Learning Technology');

-- 插入资源标签数据
INSERT INTO resource_tag (tag_name, tag_color, use_count) VALUES
('Beginner Friendly', '#67C23A', 0),
('Practical Project', '#E6A23C', 0),
('Interview Essential', '#F56C6C', 0),
('Framework Learning', '#909399', 0),
('Algorithm DataStructure', '#409EFF', 0),
('Enterprise Development', '#606266', 0),
('Open Source', '#67C23A', 0),
('Video Tutorial', '#E6A23C', 0),
('PDF Document', '#F56C6C', 0),
('Online Course', '#909399', 0);

-- 插入学分转换规则
INSERT INTO credit_conversion_rule (source_type, target_type, conversion_rate, min_credits, max_credits, status) VALUES
('技能证书', '职业培训', 0.8, 1.0, 10.0, 1),
('职业培训', '技能证书', 0.7, 1.0, 8.0, 1),
('学历教育', '职业培训', 0.9, 1.0, 15.0, 1),
('在线课程', '职业培训', 0.6, 1.0, 5.0, 1),
('其他', '技能证书', 0.9, 1.0, 12.0, 1);

-- 插入示例学习资源
INSERT INTO learning_resource (resource_name, resource_type, category_id, subject, keywords, description, content_url, thumbnail_url, difficulty_level, credit_value, uploader_id, tags, prerequisites, learning_objectives, status) VALUES
('Java Basic Programming Tutorial', 'Video Course', 3, 'Computer Science', 'Java,Programming,Basic,OOP', 'Java programming language basic tutorial, includes syntax and OOP concepts', 'https://example.com/java-basic', 'https://example.com/java-basic-thumb.jpg', 1, 3.0, 2, 'Beginner Friendly,Video Tutorial', 'None', 'Master Java basic syntax and OOP concepts', 1),
('Python Data Analysis Practice', 'Practical Project', 4, 'Data Science', 'Python,Data Analysis,pandas,numpy', 'Python data analysis practical project using pandas and numpy', 'https://example.com/python-data-analysis', 'https://example.com/python-data-thumb.jpg', 2, 4.0, 2, 'Practical Project,Open Source', 'Master Python basic syntax', 'Ability to use Python for data analysis and visualization', 1),
('React Frontend Development Guide', 'eBook', 7, 'Frontend Development', 'React,Frontend,JavaScript,Components', 'Complete React frontend framework learning guide from basic to advanced', 'https://example.com/react-guide.pdf', 'https://example.com/react-guide-thumb.jpg', 2, 3.5, 3, 'Framework Learning,PDF Document', 'Master JavaScript and ES6 syntax', 'Ability to develop modern frontend applications with React', 1);

-- 创建示例学生账户
INSERT INTO sys_user (username, password, real_name, email, phone, gender, birth_date, status) VALUES
('student1', MD5('123456'), 'John Wang', 'student1@example.com', '13800138001', 1, '2000-01-15', 1),
('student2', MD5('123456'), 'Mary Li', 'student2@example.com', '13800138002', 2, '2001-03-20', 1);

-- 绑定学生角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(4, 3), (5, 3);

-- 创建学分账户
INSERT INTO credit_account (user_id, total_credits, available_credits, frozen_credits) VALUES
(4, 25.5, 20.5, 5.0),
(5, 18.0, 15.0, 3.0);

-- 插入学分记录
INSERT INTO credit_record (user_id, credit_type, credit_source, credit_amount, operation_type, status, remark) VALUES
(4, 'Skill Certificate', 'Computer Level 2 Certificate', 3.0, 1, 1, 'Skill certification credit earned'),
(4, 'Academic Education', 'CET-4 Certificate', 2.5, 1, 1, 'Language ability certification credit earned'),
(5, 'Skill Certificate', 'Software Testing Engineer Certificate', 4.0, 1, 1, 'Professional skill certification credit earned'),
(5, 'Online Course', 'Java Advanced Programming Course', 3.0, 1, 1, 'Online course credit earned');

-- 完成脚本
SELECT 'Database rebuild completed!' AS message;
SELECT COUNT(*) AS user_count FROM sys_user;
SELECT COUNT(*) AS category_count FROM resource_category;
SELECT COUNT(*) AS tag_count FROM resource_tag;
SELECT COUNT(*) AS resource_count FROM learning_resource;
SELECT COUNT(*) AS account_count FROM credit_account; 