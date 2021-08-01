package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志Filter
 */
@WebFilter(urlPatterns = "/*")
public class Part09_02_LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /* contextPath就是Part03_embeddedServlet里tomcat.webApp()设置的第一个参数，就是浏览器访问的根路径，不是真实的文件路径 */
        String contextPath = ((HttpServletRequest) request).getServletContext().getContextPath();
        String requestUrl = ((HttpServletRequest) request).getRequestURL().toString();
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String realPath = ((HttpServletRequest) request).getServletContext().getRealPath(requestURI);
        String servletPath = ((HttpServletRequest) request).getServletPath();
        System.out.println("contextPath: " + contextPath);
        System.out.println("requestUrl: " + requestUrl);
        System.out.println("requestURI: " + requestURI);
        System.out.println("realPath: " + realPath);
        System.out.println("servletPath: " + servletPath);
        chain.doFilter(request, response);
    }
}
