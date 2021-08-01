安装kafka：

需要安装zookeeper和kafka

下载的两个软件包分别解压在了

zk: D:\zookeeper\apache-zookeeper-3.7.0-bin

kafka: D:\kafka\kafka_2.13-2.8.0

### 启动/关闭zookeeper: 

将 conf 目录下的 zoo_sample.cfg 文件，复制一份，重命名为 zoo.cfg
添加新内容：
```
dataDir=D:\zookeeper\apache-zookeeper-3.7.0-bin\data
dataLogDir=D:\zookeeper\apache-zookeeper-3.7.0-bin\log
```

启动，运行bin下的zkServer.cmd, 即可，
绑定的端口是2181
使用 netstat -ano | findstr "2181" 查看服务是否已经启动

关闭服务：使用 netstat -ano | findstr "2181" 查zk的pid，然后使用kill杀掉

#### 启动/关闭kafka:

修改conf目录下的server.properties文件内容： log.dirs=./logs

在kafka主目录，执行`.\bin\windows\kafka-server-start.bat .\config\server.properties`启动
绑定的端口是9092
执行`.\bin\windows\kafka-server-stop.bat`停止