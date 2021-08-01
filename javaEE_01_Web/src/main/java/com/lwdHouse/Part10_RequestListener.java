package com.lwdHouse;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;


@WebListener
public class Part10_RequestListener implements ServletRequestListener{
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
    }
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {}
}
