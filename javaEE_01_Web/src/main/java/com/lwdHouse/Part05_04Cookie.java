package com.lwdHouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

//@WebServlet(urlPatterns = "/pref")
public class Part05_04Cookie extends HttpServlet {
    private static final Set<String> LANGUAGES = Set.of("zh", "en");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter("lang");
        if (LANGUAGES.contains(lang)){
            Cookie cookie = new Cookie("lang", lang);
            // 该cookie生效的路径, 浏览器根据此前缀决定是否发送Cookie
            cookie.setPath("/");
            // 该cookie有效期（单位秒）
            cookie.setMaxAge(100 * 24 * 60 * 60);
            // 添加cookie到响应
            resp.addCookie(cookie);
        }
        resp.sendRedirect("/");
    }
}
