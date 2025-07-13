-- 培训项目表
CREATE TABLE IF NOT EXISTS training_program (
    program_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '培训项目ID',
    program_name VARCHAR(100) NOT NULL COMMENT '培训项目名称',
    program_code VARCHAR(50) NOT NULL COMMENT '培训项目编码',
    program_type INT DEFAULT 1 COMMENT '项目类型：1-线上培训，2-线下培训，3-混合培训',
    manager_id BIGINT COMMENT '培训负责人ID',
    description TEXT COMMENT '培训项目描述',
    content TEXT COMMENT '培训内容大纲',
    credit_value DECIMAL(5,2) DEFAULT 0 COMMENT '学分值',
    cost DECIMAL(10,2) DEFAULT 0 COMMENT '培训费用',
    max_participants INT DEFAULT 0 COMMENT '最大参与人数',
    current_participants INT DEFAULT 0 COMMENT '当前参与人数',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    location VARCHAR(255) COMMENT '地点（线下或混合培训）',
    status INT DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已结束，3-已取消',
    enroll_deadline DATETIME COMMENT '报名截止时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (program_id),
    INDEX idx_program_name (program_name),
    INDEX idx_program_type (program_type),
    INDEX idx_status (status),
    INDEX idx_manager (manager_id)
) COMMENT='培训项目表';

-- 培训参与者表
CREATE TABLE IF NOT EXISTS training_participant (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '参与记录ID',
    program_id BIGINT NOT NULL COMMENT '培训项目ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    status INT DEFAULT 0 COMMENT '参与状态：0-已报名，1-已确认，2-已完成，3-已取消',
    enroll_time DATETIME COMMENT '报名时间',
    confirm_time DATETIME COMMENT '确认时间',
    complete_time DATETIME COMMENT '完成时间',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_program_user (program_id, user_id),
    INDEX idx_program (program_id),
    INDEX idx_user (user_id),
    INDEX idx_status (status)
) COMMENT='培训参与者表';

-- 培训资源表（课程资料、PPT等）
CREATE TABLE IF NOT EXISTS training_resource (
    resource_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '资源ID',
    program_id BIGINT NOT NULL COMMENT '培训项目ID',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(50) COMMENT '资源类型',
    file_url VARCHAR(255) COMMENT '文件URL',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小(KB)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (resource_id),
    INDEX idx_program (program_id),
    INDEX idx_resource_type (resource_type)
) COMMENT='培训资源表';

-- 培训反馈表
CREATE TABLE IF NOT EXISTS training_feedback (
    feedback_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    program_id BIGINT NOT NULL COMMENT '培训项目ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    rating INT DEFAULT 0 COMMENT '评分(1-5)',
    content TEXT COMMENT '反馈内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (feedback_id),
    INDEX idx_program (program_id),
    INDEX idx_user (user_id)
) COMMENT='培训反馈表'; 