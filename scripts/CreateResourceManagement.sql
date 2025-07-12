-- 资源管理模块数据库结构
-- 创建时间：2024-03-25

-- 1. 资源分类表
CREATE TABLE IF NOT EXISTS resource_category (
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
) COMMENT '资源分类表';

-- 2. 完善学习资源表（如果不存在则创建）
CREATE TABLE IF NOT EXISTS learning_resource (
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
    INDEX idx_create_time (create_time)
) COMMENT '学习资源表';

-- 3. 资源评价表
CREATE TABLE IF NOT EXISTS resource_review (
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
    UNIQUE KEY uk_resource_user (resource_id, user_id)
) COMMENT '资源评价表';

-- 4. 用户学习轨迹表
CREATE TABLE IF NOT EXISTS user_learning_track (
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
    INDEX idx_create_time (create_time)
) COMMENT '用户学习轨迹表';

-- 5. 资源推荐表
CREATE TABLE IF NOT EXISTS resource_recommendation (
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
    INDEX idx_create_time (create_time)
) COMMENT '资源推荐表';

-- 6. 用户收藏表
CREATE TABLE IF NOT EXISTS user_favorite (
    favorite_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    favorite_type VARCHAR(50) DEFAULT 'resource' COMMENT '收藏类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_resource_id (resource_id),
    UNIQUE KEY uk_user_resource (user_id, resource_id)
) COMMENT '用户收藏表';

-- 7. 资源标签表
CREATE TABLE IF NOT EXISTS resource_tag (
    tag_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_color VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
    use_count INT DEFAULT 0 COMMENT '使用次数',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_tag_name (tag_name)
) COMMENT '资源标签表';

-- 8. 资源标签关联表
CREATE TABLE IF NOT EXISTS resource_tag_relation (
    relation_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_resource_id (resource_id),
    INDEX idx_tag_id (tag_id),
    UNIQUE KEY uk_resource_tag (resource_id, tag_id)
) COMMENT '资源标签关联表';

-- 插入基础分类数据
INSERT INTO resource_category (category_name, parent_id, category_path, level, sort_order, description) VALUES
('计算机科学', 0, '1', 1, 1, '计算机科学相关资源'),
('编程语言', 1, '1,2', 2, 1, '各类编程语言学习资源'),
('Java', 2, '1,2,3', 3, 1, 'Java编程语言'),
('Python', 2, '1,2,4', 3, 2, 'Python编程语言'),
('前端技术', 1, '1,5', 2, 2, '前端开发技术'),
('数据库', 1, '1,6', 2, 3, '数据库技术'),
('软件工程', 0, '7', 1, 2, '软件工程方法和实践'),
('项目管理', 7, '7,8', 2, 1, '项目管理知识'),
('测试技术', 7, '7,9', 2, 2, '软件测试技术'),
('人工智能', 0, '10', 1, 3, '人工智能相关技术');

-- 插入基础标签数据
INSERT INTO resource_tag (tag_name, tag_color, use_count) VALUES
('初学者友好', '#67C23A', 0),
('实战项目', '#E6A23C', 0),
('面试必备', '#F56C6C', 0),
('框架学习', '#909399', 0),
('算法数据结构', '#409EFF', 0),
('企业级开发', '#606266', 0); 