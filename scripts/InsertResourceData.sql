-- 插入测试学习资源数据
INSERT INTO learning_resource (
    resource_name, 
    resource_type, 
    category_id, 
    subject, 
    keywords, 
    description, 
    difficulty_level, 
    credit_value, 
    uploader_id, 
    tags, 
    prerequisites, 
    learning_objectives, 
    status
) VALUES
-- Java相关资源
('Java核心编程基础', '课程', 3, 'Java编程', 'Java,编程,基础', 'Java编程基础课程，适合初学者', 1, 2.00, 1, '初学者友好,编程基础', '无', '掌握Java语言基础语法和编程思想', 1),
('Spring Boot实战教程', '教材', 3, 'Java框架', 'Spring Boot,Java,框架', 'Spring Boot框架学习教材，从入门到精通', 2, 3.50, 1, '框架学习,企业级开发', 'Java基础', '掌握Spring Boot框架开发企业级应用', 1),
('Java设计模式详解', '课程', 3, 'Java设计模式', '设计模式,Java,面向对象', '详细讲解Java中的23种设计模式及应用场景', 3, 4.00, 1, '设计模式,面试必备', 'Java面向对象基础', '灵活运用设计模式解决软件设计问题', 1),

-- Python相关资源
('Python数据分析入门', '课件', 4, 'Python', 'Python,数据分析,Pandas', 'Python数据分析基础，包含Pandas、Numpy等库的使用', 1, 2.50, 2, '数据分析,Python', '基础编程知识', '使用Python进行数据清洗和分析', 1),
('Python机器学习实战', '实训项目', 4, '机器学习', 'Python,机器学习,AI', '通过实际项目学习Python机器学习技术', 3, 5.00, 2, '人工智能,项目实战', 'Python基础,数学基础', '掌握常见机器学习算法及应用', 1),

-- 前端技术资源
('Vue.js完全指南', '教材', 5, '前端开发', 'Vue,JavaScript,前端', 'Vue.js框架完全学习指南，包含实战案例', 2, 3.00, 3, '框架学习,前端开发', 'JavaScript基础', '独立开发Vue.js项目', 1),
('React开发实战', '课程', 5, '前端开发', 'React,JavaScript,前端', 'React框架开发实战课程', 2, 3.50, 3, '框架学习,前端开发', 'JavaScript ES6基础', '使用React开发现代Web应用', 1),
('CSS高级技巧与动画', '课件', 5, '前端开发', 'CSS,动画,前端', 'CSS高级特性与动画效果制作详解', 2, 2.00, 3, '前端开发,UI设计', 'HTML,CSS基础', '创建美观的网页动画和交互效果', 1),

-- 数据库资源
('MySQL性能优化实战', '课程', 6, '数据库', 'MySQL,性能优化,数据库', 'MySQL数据库性能调优与最佳实践', 3, 4.00, 4, '数据库,性能优化', 'SQL基础,数据库原理', '解决数据库性能瓶颈问题', 1),
('MongoDB入门到精通', '教材', 6, '数据库', 'MongoDB,NoSQL,数据库', 'MongoDB文档数据库完全学习指南', 2, 3.00, 4, '数据库,NoSQL', '基本数据库概念', '使用MongoDB存储和查询文档数据', 1); 