package com.lwdHouse;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * 使用嵌入式tomcat
 *      1.加入2个依赖，并删除掉servlet依赖，因为tomcat里包含了servlet
 *          1）tomcat-embed-core
 *          2）tomcat-embed-jasper
 *        加入后，可以设置scope为compile (编译+运行) 或者provided (编译)
 *          设置为provided后，需要设置ide，使得用ide启动程序时，加载这个库。eclipse设置方法为 => Application - Main，钩上Include dependencies with "Provided" scope
 *          设置为compile后，打的war包里也有这两个库，部署到独立版tomcat后，可能会冲突
 *      2.编写main，启动嵌入式tomcat
 *
 *  好处：
 *      启动简单，无需下载Tomcat或安装任何IDE插件（IntelliJ社区版即可）；
 *      调试方便，可在IDE中使用断点调试；
 *      使用Maven创建war包后，也可以正常部署到独立的Tomcat服务器中。
 */

public class Part03_EmbeddedServlet {
    public static void main(String[] args) throws LifecycleException {
        // 启动tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8087));
        tomcat.getConnector();
        // 创建webapp
        // 设置映射路径
        // 这里相当于独立版tomcat里设置conf\Catalina\localhost\<projectName>.xml文件的
        // <Context path="/" docBase="C:/.../javaSE_01_Web/src/main/webapp" />映射关系
        // 这里直接把webapp目录设置到了源码下，而不是target下
        // context path就是浏览器地址栏的基路径，ip:port/project/
        Context ctx = tomcat.addWebapp("", new File("javaEE_01_Web/src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        // 把javaSE_01_Web/target/classes挂载到/WEB-INF/classes下
        resources.addPreResources(
                                new DirResourceSet(resources,
                                     "/WEB-INF/classes",
                                                   new File("javaEE_01_Web/target/classes").getAbsolutePath(),
                                        "/"));
        // 为tomcat添加servlet的来源
        ctx.setResources(resources);
        // 启动tomcat
        tomcat.start();
        // 等待服务结束
        tomcat.getServer().await();
    }
}
