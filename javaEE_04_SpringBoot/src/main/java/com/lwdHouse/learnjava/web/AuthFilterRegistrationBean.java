package com.lwdHouse.learnjava.web;

import com.lwdHouse.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Order(10)
@Component
public class AuthFilterRegistrationBean extends FilterRegistrationBean<Filter> {

    @Autowired
    UserService userService;

    @Autowired
    UserController userController;

    @Override
    public Filter getFilter() {
        return new AuthFilter(userService, userController);
    }
}
