elasticSearch安装：

需要安装elasticSearch和kibana

下载地址：
https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-2-2
https://artifacts.elastic.co/downloads/kibana/kibana-6.2.2-windows-x86_64.zip

本机安装位置：
D:\kibana\kibana-6.2.2-windows-x86_64
D:\elasticSearch\elasticsearch-6.2.2

### 启动es：
先安装ik插件：
在es主目录下执行：
```
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.3.0/elasticsearch-analysis-ik-6.3.0.zip
```

如果安装失败，手动安装：
在这个页面下载插件：https://github.com/medcl/elasticsearch-analysis-ik/releases
找到和es版本一致的插件下载，本机使用6.2.2
下载后，解压到es主目录/plugins/ik下，注意不带主目录，直接把内容解压到ik下

执行.\elasticsearch.bat启动
端口9200

### 启动kibana进行客户端访问es
进入kibana主目录\bin下, 直接启动：.kibana.bat
绑定端口5601

访问http://localhost:5601进行发送请求给es