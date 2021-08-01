mysql服务安装路径：D:\mysql3\mysql-8.0.23-winx64，安装方式是直接解压，
后面所说的安装是指服务的安装

root密码查看：D:\mysql3\mysql-8.0.23-winx64\password.txt

启动停止服务：windows任务管理器 > 服务 > mysql 启动/停止，
或者使用命令net start/stop mysql

如何卸载旧的服务？ windows任务管理器 > 服务 > mysql 先停止服务（或者用 net stop mysql命令停止服务），
然后打开cmd命令提示符（黑色的哪个）不是蓝色的powershell，然后敲击sc remove mysql, 卸载mysql服务。
然后删除掉mysql的安装目录

如何安装服务？ 把mysql包直接解压到一个路径下，进入主目录创建my.ini, 内容格式如下

```
[client]
# 设置mysql客户端默认字符集
default-character-set=utf8

[mysqld]
# 设置3306端口
port = 3306
# 设置mysql的安装目录
basedir=C:\\web\\mysql-8.0.11
# 设置 mysql数据库的数据的存放目录，MySQL 8+ 不需要以下配置，系统自己生成即可，否则有可能报错
# datadir=C:\\web\\sqldata
# 允许最大连接数
max_connections=20
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB

```
进入到bin下，敲 mysqld --initialize --console 初始化数据库
，这个命令会初始化my.ini配置的datadir，然后返回你root密码，一定要记住了

敲mysqld install安装服务

net start mysql启动服务即可

绑定的端口是3306

