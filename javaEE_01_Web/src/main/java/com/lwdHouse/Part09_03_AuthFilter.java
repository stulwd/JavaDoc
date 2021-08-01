package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录检查Filter
 */
@WebFilter(urlPatterns = "/user/*")
public class Part09_03_AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("AuthFilter: check authentication");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getSession().getAttribute("user") == null){
            // 当用户没有登录时，后面所有的 Filter 都不继续处理，直接重定向到登录界面
            System.out.println("AuthFilter: not login!");
            resp.sendRedirect("/signin");
        }else {
            chain.doFilter(request, response);
        }
    }
}
