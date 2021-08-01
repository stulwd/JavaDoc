package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 使用 Filter 进行过滤
 * 设置编码的Filter
 *  ----------    调用     ----------   调用    ----------
 * |       ===|=========|=>       ==|========|=>          |
 * |  filter  | doFilter|    filter |doFilter|  servlet  |
 * |       <==|=========|==       <=|========|==          |
 *  ----------    返回    ----------    返回    ----------
 */

@WebFilter(urlPatterns = "/*")
public class Part09_01_EncodeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Encoding Filter:doFilter");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
        // 上面doFilter会调用下级过滤器，
        // 再调用下下级过滤器...调用servlet，servlet返回，然后返回...返回,返回到这里
    }
}
