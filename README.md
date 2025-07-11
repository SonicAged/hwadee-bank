# 终身学习学分银行平台分析系统

## 项目概述

本项目是一个前后端分离的终身学习学分银行平台分析系统，支持多角色权限管理、学分管理、学习资源管理、课程管理、认证评估和日志审计等功能。

## 技术栈

### 后端技术
- **Java**: JDK 17
- **Spring Boot**: 3.5.3
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis
- **连接池**: Druid
- **安全认证**: JWT + MD5加密
- **依赖管理**: Maven

### 前端技术
- **Vue.js**: 3.5.17
- **TypeScript**: 5.8.0
- **UI框架**: Element Plus 2.8.8
- **HTTP客户端**: Axios 1.7.2
- **状态管理**: Pinia 2.2.6
- **路由**: Vue Router 4.5.1
- **构建工具**: Vite 7.0.0

## 系统架构

### 后端分层架构
```
src/main/java/org/hwadee/backend/
├── entity/          # 实体类
├── mapper/          # MyBatis映射器
├── service/         # 业务逻辑接口
├── serviceImpl/     # 业务逻辑实现
├── controller/      # 控制器层
├── config/          # 配置类
└── utils/           # 工具类
```

### 前端模块架构
```
src/
├── views/           # 页面组件
│   ├── auth/        # 认证页面
│   ├── system/      # 系统管理
│   ├── credit/      # 学分管理
│   ├── resource/    # 资源管理
│   ├── course/      # 课程管理
│   └── audit/       # 审计管理
├── stores/          # 状态管理
├── utils/           # 工具函数
└── router/          # 路由配置
```

## 功能模块

### 1. 权限管理系统
- 多角色权限模型（管理员、机构用户、教师、学生、审核员）
- 细粒度权限控制
- 动态权限调整
- 多因素认证（JWT + MD5）

### 2. 学分管理中心
- 学分账户管理
- 学分积累与转换
- 学分记录查询
- 可视化统计展示

### 3. 学习资源管理
- 资源库建设
- 资源分类与检索
- 资源推荐系统
- 资源评价反馈

### 4. 课程与培训管理
- 课程目录展示
- 在线学习支持
- 培训项目管理
- 学习进度跟踪

### 5. 认证与评估系统
- 学分认证申请
- 多级审核流程
- 评估标准管理
- 电子证书颁发

### 6. 日志审计系统
- 操作日志记录
- 系统日志监控
- 审计报表生成

## 环境要求

### 开发环境
- Node.js 18+
- JDK 17
- MySQL 8.0
- Maven 3.6+

### 数据库配置
- 数据库名: `BankAnalysis`
- 用户名: `root`
- 密码: `123456`
- 端口: `3306`

## 快速开始

### 1. 数据库初始化
```bash
# 连接MySQL数据库
mysql -u root -p

# 执行SQL脚本
source BankAnalysis.sql
```

### 2. 后端启动
```bash
cd BackEnd
mvn spring-boot:run
```
后端服务将在 `http://localhost:8080` 启动

### 3. 前端启动
```bash
cd BankAnalysis
npm install
npm run dev
```
前端应用将在 `http://localhost:5173` 启动

## 默认账户

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | 超级管理员 | 拥有所有权限 |
| teacher1 | 123456 | 教师 | 可管理课程和资源 |
| student1 | 123456 | 学生 | 可学习课程和申请学分 |
| auditor1 | 123456 | 审核员 | 负责学分认证审核 |

## API接口文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/check-username` - 检查用户名
- `GET /api/auth/check-email` - 检查邮箱

### 用户管理接口
- `GET /api/user/{userId}` - 获取用户信息
- `PUT /api/user/{userId}` - 更新用户信息
- `GET /api/user/list` - 获取用户列表
- `POST /api/user/{userId}/change-password` - 修改密码
- `GET /api/user/{userId}/permissions` - 获取用户权限
- `GET /api/user/{userId}/roles` - 获取用户角色

## 系统特性

### 安全特性
- JWT Token认证
- MD5密码加密
- 权限拦截器
- 跨域请求处理

### 性能特性
- 数据库连接池优化
- 前端路由懒加载
- 组件按需导入
- 响应式设计

### 可扩展性
- 模块化架构设计
- 标准化接口规范
- 配置化权限管理
- 插件化功能扩展

## 部署说明

### 生产环境配置
1. 修改 `application.properties` 中的数据库连接信息
2. 配置生产环境的JWT密钥
3. 调整日志级别和输出路径
4. 配置文件上传路径

### Docker部署
```bash
# 构建后端镜像
cd BackEnd
docker build -t bank-analysis-backend .

# 构建前端镜像
cd BankAnalysis
docker build -t bank-analysis-frontend .

# 使用docker-compose启动
docker-compose up -d
```

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用ESLint + Prettier格式化前端代码
- 统一的命名规范和注释规范

### 开发流程
1. 数据库表设计
2. 后端实体类和Mapper开发
3. Service业务逻辑实现
4. Controller接口开发
5. 前端页面和组件开发
6. 接口联调测试

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有问题或建议，请联系开发团队。 