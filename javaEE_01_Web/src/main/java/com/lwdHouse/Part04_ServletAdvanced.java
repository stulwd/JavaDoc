package com.lwdHouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(urlPatterns = {"/hello1"})
public class Part04_ServletAdvanced extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("QueryString: "+req.getQueryString());
        // // context path就是浏览器地址栏的基路径，ip:port/project/
        System.out.println("ContextPath: "+req.getContextPath());
        System.out.println("Cookies: "+req.getCookies().toString());
        System.out.println("Scheme: "+req.getScheme());
    }
}
