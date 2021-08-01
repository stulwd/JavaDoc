package com.lwdHouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(urlPatterns = "/")
public class Part05_02indexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = (String) req.getSession().getAttribute("user");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Powered-By", "JavaEE Servlet");
        PrintWriter pw = resp.getWriter();

        if (parseLangFromCookies(req).equals("zh")){
            if (user == null){
                pw.write("<p><a href=\"/signin\">登录</a></p>");
            }else {
                pw.write("<p><a href=\"signout\">登出</a></p>");
            }
        }else{
            if (user == null){
                pw.write("<p><a href=\"/signin\">Sign in</a></p>");
            }else {
                pw.write("<p><a href=\"signout\">Sign out</a></p>");
            }
        }
        pw.flush();
    }

    private String parseLangFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cook : cookies) {
            if (cook.getName().equals("lang")){
                return cook.getValue();
            }
        }
        return "en";
    }
}
