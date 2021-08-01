package com.lwdHouse.learnjava.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lwdHouse.learnjava.entity.User;
import com.lwdHouse.learnjava.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * spring集成过滤器Filter
 * 注意这个过滤器是创建在spring容器中的，而不是像javaEE_01_Web一样，创建在servlet容器中
 * 因为这个Filter需要获取userService bean等业务相关的东西，
 * 所以如果把它创建在servlet容器中，就拿不到userService
 * 所以在springMVC中，如果你的过滤器里用到了spring容器里的东西
 * 必须把Filter创建在spring容器中。然后借助servlet里创建的DelegatingFilterProxy
 * （web.xml里定义了）来代理这个Filter
 * 所以总结一下：
 * Spring容器通过扫描@Component实例化AuthFilter；
 * Servlet容器从web.xml中读取配置，实例化DelegatingFilterProxy，命名必须和
 * authFilter（和component bean保持一直）；

 */

public class AuthFilter implements Filter {

    final Logger logger = LoggerFactory.getLogger(getClass());

    UserService userService;

    UserController userController;

    public AuthFilter(UserService userService, UserController userController) {
        this.userService = userService;
        this.userController = userController;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        try{
            authenticateByHeader(req);
        }catch (RuntimeException e){
            logger.warn("login by authorization failed.", e);
        }
        chain.doFilter(request, response);
    }

    /**
     * 这个过滤器实现了用header中的Authorization来登录，
     * Basic认证模式并不安全，本节只用来作为使用Filter的示例。
     * @param req
     */
    private void authenticateByHeader(HttpServletRequest req) throws JsonProcessingException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")){
            logger.info("try authenticate by authorization header...");
            String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), StandardCharsets.UTF_8);
            int pos = up.indexOf(":");
            if (pos > 0){
                String email = URLDecoder.decode(up.substring(0, pos), StandardCharsets.UTF_8);
                String password = URLDecoder.decode(up.substring(pos + 1), StandardCharsets.UTF_8);
                User user = userService.signin(email, password);
//                req.getSession().setAttribute(UserController.KEY_USER, user);
                req.getSession().setAttribute(UserController.KEY_USER_ID, user.getId());
                userController.putUserIntoRedis(user);
                logger.info("user {} login by authorization header ok.", email);
            }
        }
    }
}
