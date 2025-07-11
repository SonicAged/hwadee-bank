<template>
  <div class="about-container">
    <div class="about-content">
      <div class="about-header">
        <h1>关于我们</h1>
        <p>终身学习学分银行平台</p>
      </div>
      
      <div class="about-main">
        <el-card class="info-card">
          <h2>平台简介</h2>
          <p>终身学习学分银行平台是一个综合性的学习管理系统，旨在为学习者提供学分积累、转换和管理服务。</p>
          
          <h3>主要功能</h3>
          <ul>
            <li>多角色权限管理系统</li>
            <li>学分管理中心</li>
            <li>学习资源管理</li>
            <li>课程与培训管理</li>
            <li>认证评估系统</li>
            <li>审计日志系统</li>
          </ul>
          
          <h3>技术栈</h3>
          <div class="tech-stack">
            <el-tag type="primary" class="tech-tag">Vue 3</el-tag>
            <el-tag type="success" class="tech-tag">TypeScript</el-tag>
            <el-tag type="info" class="tech-tag">Element Plus</el-tag>
            <el-tag type="warning" class="tech-tag">Spring Boot 3</el-tag>
            <el-tag type="danger" class="tech-tag">MySQL 8</el-tag>
            <el-tag class="tech-tag">MyBatis</el-tag>
          </div>
        </el-card>
      </div>
      
      <div class="back-button">
        <el-button type="primary" @click="$router.back()">返回</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
}

.about-content {
  background: white;
  border-radius: 15px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
}

.about-header {
  text-align: center;
  margin-bottom: 40px;
}

.about-header h1 {
  color: #333;
  margin: 0 0 15px 0;
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.about-header p {
  color: #666;
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.about-main {
  margin-bottom: 30px;
}

.info-card {
  border: none;
  box-shadow: none;
}

.info-card h2 {
  color: #333;
  margin-bottom: 16px;
  font-size: 24px;
}

.info-card h3 {
  color: #555;
  margin-top: 24px;
  margin-bottom: 12px;
  font-size: 18px;
}

.info-card p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
}

.info-card ul {
  color: #666;
  line-height: 1.8;
  padding-left: 20px;
}

.info-card li {
  margin-bottom: 8px;
}

.tech-stack {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tech-tag {
  margin: 0;
}

.back-button {
  text-align: center;
}

.back-button .el-button {
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 8px;
}
</style>
