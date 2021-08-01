package com.lwdHouse.learnjava.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * spring-boot中的Filter更简单，不需要配置DelegatingFilterProxy
 * 如下
 */
@Order(20)      // 排序，值小的先被过滤
@Component
public class ApiFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    @PostConstruct
    public void init() {
        setFilter(new ApiFilter());
        // 设置过滤路径
        setUrlPatterns(List.of("/api/*"));
    }

    class ApiFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            var resp = (HttpServletResponse) response;
            resp.setHeader("X-Api-Version", "1.0");
            chain.doFilter(request, response);
        }
    }
}
