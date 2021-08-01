netstat 用法

以zookeeper服务器和客户端运行在同一台机器为例：

使用netstat -ano | find "2182"
打印如下
```
协议    本地地址                外部地址                 状态             PID
TCP    0.0.0.0:2181           0.0.0.0:0              LISTENING       7456
TCP    127.0.0.1:2181         127.0.0.1:64037        ESTABLISHED     7456
TCP    127.0.0.1:64037        127.0.0.1:2181         ESTABLISHED     10268
TCP    [::]:2181              [::]:0                 LISTENING       7456
```
（协议有TCP/IP，本地地址是本进程使用的ip:端口，外部地址是连接到另一方的ip:端口，
所以本机的端口占用情况是在本地地址这一列）

其中PID 7456 是zk server进程，10268是client进程

server启动时，先建立第一条和第四条服务，状态时Listening监听，等待连接
使用的端口是2181

client启动时，启动进程10268，会连接server
client 本身的ip:端口是127.0.0.1:64037
会连接到127.0.0.1:2181