安装mongodb：

下载软件包：
https://fastdl.mongodb.org/win32/mongodb-win32-x86_64-2008plus-ssl-3.2.21-signed.msi

双击安装
注意选Custom setup

本机安装路径设置在了D:\mongodb

安装完成后
新建两个空文件夹：
D:\mongodb\data\db
D:\mongodb\data\log

在D:\mongodb主目录新建配置文件mongdb.cfg, 内容如下：

```
systemLog:
 destination: file
 path: D:\mongodb\data\log\mongod.log
storage:
 dbPath: D:\mongodb\data\db
```

以管理员权限打开命令行，如果盘符为c，使用`d:`命令切换盘符
进入D:\mongodb文件夹，输入安装服务命令
`.\mongod.exe --config  D:\mongodb\mongod.cfg --install`
注意配置文件要使用绝对路径

然后启动服务：
`net start MongoDB`
也可以在任务管理器的服务界面启动
绑定的端口是 `27017`

关闭服务：`net stop MongoDB`
移除服务：`D:\MongoDB\bin\mongod.exe --remove`

客户端访问程序可以用Robo 3T
https://download.robomongo.org/1.2.1/windows/robo3t-1.2.1-windows-x86_64-3e50a65.zip
也可以直接Navicat连接