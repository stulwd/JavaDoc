### 本工程的nginx服务

安装在了项目目录的nginx/下，配置文件是.\nginx\nginx-1.21.0\conf\nginx.conf,直接在上面编辑，
具体语法参考下面 Nginx使用指南 和 \javaEE_04_SpringBoot\src\main\java\com\lwdHouse\learnjava\Application.java
文件中createWebMvcConfigurer()方法里的注释

cmd到nginx/下，直接.\nginx.exe即可启动 nginx 服务器


### Nginx使用指南：

双击nginx.exe,即可运行

1. `nginx -s signal`
  
signal可以是
- stop: 停止
- quit: 优雅的退出
  （也可以执行kill -s QUIT <nginx的pid>，pid在/usr/local/nginx/logs 或者 /var/run
    文件里写着，标识是 nginx.pid）
  
- reload: 重新加载配置文件。
  （主线程会先检查新的配置文件正确性，然后再生效，而不是直接替换掉旧的
    配置文件，如果新的不正确，旧的还能正常访问）
- reopen: reopening the log file


2. nginx的执行线程通过`ps -ax | grep nginx`获取

3. nginx的运行和错误日志日志将记录在/usr/local/nginx/logs 或者 /var/log/nginx
   文件夹下的access.log 和 error.log 文件

3. 配置文件语法
```
server {
    location / {
        root /data/www;
    }

    location /images/ {
        root /data;
    }
}
```
- 对于`http://localhost/images/example.png`请求，
  nginx将会映射到/data/images/example.png
- 对于`http://localhost/some/example.html`请求，
  nginx将会映射到/data/www/some/example.html
  

4. nginx作为反向代理使用
nginx作为反向代理服务器是一种流行的用法，
代理服务器是指：接受请求，转发给其他服务器，获取处理结果，返回给客户的服务器
   
```
server {
        listen 80;
        server_name wendi.com;    # 这里如果使用自定义域名，要在C:\Windows\System32\drivers\etc\hosts文件下添加一个域名ip映射
        root "<静态文件所在的路径>";

        access_log "<每次访问页面产生的日志路径>";
        error_log "<错误日志路径>";
        
        location /static {
          # 这里没有配置root，会使用上一层context的root
        }
        location /favicon.ico {
          # 这里没有配置root，会使用上一层context的root
        }
        location /WEB-INF {
            return 404;
        }

        # 配置将被代理的路径
        location / {
            proxy_pass       http://localhost:8000;   # 将请求转发的地址
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
```






