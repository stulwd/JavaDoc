package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * 监听器:
 *    Web服务器会自动初始化任何标注为@WebListener的类为监听器，并在某些事件触发时回调监听器的函数。
 * 监听器需要实现特定的监听器接口，例如 ServletContextListener 接口的实现类，
 * 会在整个Web应用程序初始化完成后，和Web应用程序关闭后获得回调通知。
 * - 可以把初始化数据库连接池等工作放到contextInitialized()回调方法中
 * - 把清理资源的工作放到contextDestroyed()回调方法中
 *
 * 很多第三方Web框架都会通过一个ServletContextListener接口初始化自己。
 *
 * 还有几种Listener：
 *    HttpSessionListener：监听HttpSession的创建和销毁事件；
 *    ServletRequestListener：监听ServletRequest请求的创建和销毁事件；
 *    ServletRequestAttributeListener：监听ServletRequest请求的属性变化事件（即调用ServletRequest.setAttribute()方法）；
 *    ServletContextAttributeListener：监听ServletContext的属性变化事件（即调用ServletContext.setAttribute()方法）；
 */

@WebListener
public class Part10_AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("WebApp initialized: ServletContext = " + sce.getServletContext());
        System.out.println("WebApp initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WebApp destroyed.");
    }
}