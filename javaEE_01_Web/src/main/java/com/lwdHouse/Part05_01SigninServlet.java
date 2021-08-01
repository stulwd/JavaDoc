package com.lwdHouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 验证身份
 *      为了跟踪用户状态，服务器可以向浏览器分配一个唯一Session ID，并以Cookie的形式发送到浏览器，
 *      浏览器在后续访问时总是附带此Cookie，这样，服务器就可以识别用户身份
 * Session：
 *      特定浏览器（客户端）与服务器进行的对话
 * Session Id
 *      每个浏览器（客户端）第一次访问服务器后，服务器会创建一个HttpSession，并返回给客户端这个Session ID
 *      （以Cookie的形式发送到浏览器），下次带着上次分配的Session ID访问。服务器在内存中自动维护了一个
 *      Session ID到HttpSession的映射表，通过HttpServletRequest.getSession()获取对应的HttpSession
 *      如果用户在一段时间内没有访问服务器，那么Session会自动失效（从映射表里剔除），
 *      下次即使带着上次分配的Session ID访问，服务器也认为是新用户，需要重新分配Session ID
 * 这些点需要知道:
 * 1. 某个浏览器第一次访问服务器的时候，只要这个请求的处理里面用到req.getSession()来获取Session,
 *    代表就有身份相关的操作，那么服务器就会为这个客户端创建一个HttpSession，并且在这次请求的响应头中
 *    返回一个包含了 SessionId 的 Cookie，如下
 *    Set-Cookie: JSESSIONID=858252D2476666DB8541FB21A188B67C; Path=/; HttpOnly
 *    如果没有req.getSession()，服务器是不会创建Session的。
 * 2. 在浏览器第一次访问，并且成功获得Session后，其后的每一次访问中，都会在请求头中插入包含Session的Cookie，如下
 *    Cookie: JSESSIONID=858252D2476666DB8541FB21A188B67C，服务器会根据这个SessionId获取到Session
 *    访问相同服务器的不同接口，也会发送相同的Cookie。
 * 3. 使用ctrl+shift+delete清楚浏览器缓存后，Cookie就会消失。
 *
 * 下面例子中，依靠Session机制，实现用户的登录登出。
 *     登录后，服务器会往当前会话Session中设置一个user属性，值存储真正的用户名
 *     之后访问其他的页面，会获取当前Session，获取user属性，拿到user。进行相关操作。如果user为空，则还是未登录状态。
 *     登出后，会把当前Session的user属性移除。
 *
 * 所以
 * 1.登录和登出的业务逻辑是我们自己根据HttpSession是否存在一个"user"的Key判断的，登出后，Session ID并不会改变；
 * 2.即使没有登录功能，仍然可以使用HttpSession追踪用户，例如，放入一些用户配置信息等。
 *
 */
//@WebServlet(urlPatterns = "/signin")
public class Part05_01SigninServlet extends HttpServlet {

    // 模拟数据库
    Map<String, String> users = Map.of("bob", "bob123", "alice", "alice123", "tom", "tomcat");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = (String) req.getSession().getAttribute("user");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Powered-By", "JavaEE Servlet");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Sign in</h1>");
        pw.write("<form action=\"signin\" method=\"post\">");
        pw.write("<p>Username: <input name=\"username\"></p>");
        pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
        pw.write("<p><button type=\"submit\">Sign in</button> <a href=\"/\">Cancel</a></p>");
        pw.write("</form>");
        pw.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String expectedPassword = users.get(username);
        if (expectedPassword != null && expectedPassword.equals(password)){
            // 登录成功
            req.getSession().setAttribute("user", username);
            resp.sendRedirect("/");
        }else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
