artemis是一个jms消息服务，说具体点就是jms消息服务接口的实现

本机安装路径：D:\ActiveMQ\apache-artemis-2.17.0
，也是下的包直接解压到了此处

然后配置了 ARTEMIS_HOME 和 %ARTEMIS_HOME%\bin环境变量，之后直接使用artemis命令即可

生成消息目录：
然后到C:\Users\wendi\Desktop\javaCE\javaEE_03_SpringMVC\路径下（路径随意）执行
artemis create jms-data，就自动创建了jms-data文件夹作为消息目录，这个命令
会在jms-data下生成bin/ data/ etc/ lib/ lock/ log/ tmp/文件

启动/停止服务：
到C:\Users\wendi\Desktop\javaCE\javaEE_03_SpringMVC\jms-data\bin下
直接运行artemis run/stop 即可启动/停止

后台管理：访问 http://localhost:8161/console 账密admin/admin

这个服务会开启占用的端口如下：
at 0.0.0.0:61616 for protocols [CORE,MQTT,AMQP,STOMP,HORNETQ,OPENWIRE]
0.0.0.0:5672 for protocols [AMQP]
0.0.0.0:1883 for protocols [MQTT]
0.0.0.0:61613 for protocols [STOMP]
0.0.0.0:5445 for protocols [HORNETQ,STOMP]

所以和rabbitmq的端口有所冲突，尽量不要一起运行，除非改端口, rabbitmq修改端口见how-to-use-rabbitmq



