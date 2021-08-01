package com.lwdHouse;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Part10_ContextAttributeListener implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        // 获取改变的属性名
        scae.getName();
        ServletContextAttributeListener.super.attributeAdded(scae);
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {}
    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {}
}