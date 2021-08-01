package com.lwdHouse.learnjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * 拦截器Interceptor
 * 拦截器本质上就是基于AOP的方法拦截
 * 返回ModelAndView后，后续对View的渲染就脱离了Interceptor的拦截范围。
 *
 * Filter和Interceptor的拦截范围对比：
 * Interceptor的范围（虚线表示）：                        Filter的范围（虚线表示）：
 *        │   ▲                                               │   ▲
 *        ▼   │                                               ▼   │
 *      ┌───────┐                                           ┌───────┐
 *      │Filter1│                                           │Filter1│
 *      └───────┘                                           └───────┘
 *        │   ▲                                               │   ▲
 *        ▼   │                                               ▼   │
 *      ┌───────┐                                           ┌───────┐
 *      │Filter2│                                    ┌ ─ ─ ─│Filter2│─ ─ ─ ─ ─ ─ ─ ─ ┐
 *      └───────┘                                           └───────┘
 *        │   ▲                                      │        │   ▲                  │
 *        ▼   │                                               ▼   │
 * ┌─────────────────┐                               │ ┌─────────────────┐           │
 * │DispatcherServlet│<───┐                            │DispatcherServlet│<───┐
 * └─────────────────┘    │                          │ └─────────────────┘    │      │
 *  │              ┌────────────┐                       │              ┌────────────┐
 *  │              │ModelAndView│                    │  │              │ModelAndView││
 *  │              └────────────┘                       │              └────────────┘
 *  │ ┌ ─ ─ ─ ─ ─ ─ ─ ─ ┐ ▲                          │  │                     ▲      │
 *  │    ┌───────────┐    │                             │    ┌───────────┐    │
 *  ├─┼─>│Controller1│──┼─┤                          │  ├───>│Controller1│────┤      │
 *  │    └───────────┘    │                             │    └───────────┘    │
 *  │ │                 │ │                          │  │                     │      │
 *  │    ┌───────────┐    │                             │    ┌───────────┐    │
 *  └─┼─>│Controller2│──┼─┘                          │  └───>│Controller2│────┘      │
 *       └───────────┘                                       └───────────┘
 *    └ ─ ─ ─ ─ ─ ─ ─ ─ ┘                            └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 */

@Order(1)       // 通过Order指定拦截器的顺序
@Component
public class LoggerInterceptor implements HandlerInterceptor {
    final Logger logger = LoggerFactory.getLogger(getClass());

    // Controller方法调用前执行
    // preHandle()中，也可以直接处理响应，然后返回false表示无需调用Controller方法继续处理了
    // 通常在认证或者安全检查失败时直接返回错误响应
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("preHandle {}...", request.getRequestURI());
        if (request.getParameter("debug") != null){
            PrintWriter pw = response.getWriter();
            pw.write("<p>debug mode</p>");
            pw.flush();
            return false;
        }
        return true;
    }

    // Controller方法正常返回后执行
    // 因为捕获了Controller方法返回的ModelAndView，所以可以继续往ModelAndView里添加一些通用数据
    // 很多页面需要的全局数据如Copyright信息等都可以放到这里
    // 无需在每个Controller方法中重复添加
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("postHandle {}...", request.getRequestURI());
        if (modelAndView != null){
            modelAndView.addObject("__time__", LocalDateTime.now());
        }
    }

    // 无论Controller方法是否抛异常都会执行
    // 参数ex就是Controller方法抛出的异常（未抛出异常是null）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion {}: Exception = {}", request.getRequestURI(), ex);
    }
}
