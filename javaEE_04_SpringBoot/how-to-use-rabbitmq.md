安装：(需要先安装erlang)

安装包在：

erlang: D:\安装包\otp_win64_24.0.exe

rabbitmq: D:\安装包\rabbitmq-server-3.8.17.exe

两个都是直接双击安装，以管理员权限。安装完erlang，记得加环境变量ERLANG_HOME,和path
本机安装在了 D:\RabbitMQ Server\rabbitmq_server-3.8.17 和 D:\erl-24.0下

进入rabbitmq的sbin下，执行rabbitmq-plugins enable rabbitmq_management 安装插件

运行rabbitmq-server.bat启动服务

后台管理界面： http://localhost:15672 账密 guest/guest

如果启动失败，查看5672端口占用情况（有可能是artemis正在运行，占用这个端口），并杀掉对应进程
相关命令：
查占用端口的pid: `netstat -ano | findstr 5672` 
杀pid: `kill <pid>`

或者配置一个新的端口5673：
在C:\Users\wendi\AppData\Roaming\RabbitMQ 下新建文件 rabbitmq.conf
添加内容如下
```
# 数据管理端口
listeners.tcp.default = 5673
# 数据管理界面，根据需要修改，这个是访问页面的配置
management.tcp.port = 15672
management.tcp.ip   = 0.0.0.0
```

