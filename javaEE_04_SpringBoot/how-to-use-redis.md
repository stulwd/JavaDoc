redis官方不支持windows，需要在这里下载：https://github.com/tporadowski/redis/releases

redis安装路径：D:\redis，直接解压到了这里

redis默认配置文件：D:\redis\redis.windows.conf

把D:\redis加在了path里，所以直接敲 redis-server.exe 启动，
指定配置文件启动：redis-server.exe /path/to/redis.conf，不指定，使用默认配置文件

默认端口6379

客户端命令行进行访问：redis-cli.exe -h 127.0.0.1 -p 6379

