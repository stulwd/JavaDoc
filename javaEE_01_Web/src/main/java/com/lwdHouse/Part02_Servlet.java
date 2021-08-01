package com.lwdHouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 使用Servlet:
 *      1. 编写Part02_Servlet如下
 *      2. 给pom.xml添加servlet依赖
 *      3. 修改pom.xml文件,生成war包：<packing>war</packing>
 *      4. 创建webapp/WEB-INF/web.xml文件
 *      5. 打包，把包放到TOMCAT_HOME/webapps/下
 *      6. 执行TOMCAT_HOME/bin/start.bat启动
 *      7. 访问
 */

//@WebServlet(urlPatterns = {"/hello"})
public class Part02_Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, World!");
        pw.flush();
    }
}
