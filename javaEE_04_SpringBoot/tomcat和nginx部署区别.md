tomcat部署：

将web资源（如myweb）放在 `${tomcat_home}/webapps/`下，如`${tomcat_home}/webapps/myweb`,
然后在`${tomcat_home}/conf/server.xml`中配置port端口。
运行`${tomcat_home}/bin/startup.bat或shutdown.bat`启动或停止

nginx部署：

在`${nginx_home}/conf/nginx.conf`下配置资源路径和端口
运行`${nginx_home}/nginx.exe`启动
如果需要配置反向代理，参考 how-to-use-nginx.md