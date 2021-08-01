- SHOW DATABASES; 显示所有数据库
- USE database;  使用数据库
- DROP DATABASE IF EXISTS XXX; 删除数据库
- CREATE DATABASE xxx; 创建数据库
- CREATE USER IF NOT EXISTS learn@'%' IDENTIFIED BY 'learnpassword'; 
  创建用户@后面表示可以访问的主机，%表示所有主机
  
- GRANT ALL PRIVILEGES ON learnjdbc.* TO learn@'%' WITH GRANT OPTION;
  为用户learn赋予learnjdbc数据库所有表（learnjdbc.*）的所有权限（ALL PRIVILEGES）
  
- CREATE TABLE students (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(50) NOT NULL,
  gender TINYINT(1) NOT NULL,
  grade INT NOT NULL,
  PRIMARY KEY(id)
  ) Engine=INNODB DEFAULT CHARSET=UTF8;
  创建表