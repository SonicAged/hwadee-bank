-- 创建用户
CREATE USER 'bank'@'%' IDENTIFIED BY '123456';

-- 授予BankAnalysis数据库的所有权限
GRANT ALL PRIVILEGES ON BankAnalysis.* TO 'bank'@'%';

-- 刷新权限使更改生效
FLUSH PRIVILEGES;
