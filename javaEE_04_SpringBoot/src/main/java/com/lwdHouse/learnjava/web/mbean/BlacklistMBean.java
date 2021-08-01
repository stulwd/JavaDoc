package com.lwdHouse.learnjava.web.mbean;


import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
// ManagedResource表示这是一个MBean, 将要被注册到JMX, objectName制定了MBean的名字，通常以company:name=Xxx来分类Bean
@ManagedResource(objectName = "sample:name=blacklist", description = "Blacklist of IP addresses")
public class BlacklistMBean {
    private Set<String> ips = new HashSet<>();

    // 属性，只有get属性，没有set属性，说明这是一个只读属性
    @ManagedAttribute(description = "Get IP addresses in blacklist")
    public String[] getBlacklist(){
        return ips.toArray(String[]::new);
    }

    // 操作
    @ManagedOperation
    @ManagedOperationParameter(name = "ip", description = "Target IP address that will be added to blacklist")
    public void addBlacklist(String ip){
        ips.add(ip);
    }

    // 操作
    @ManagedOperation
    @ManagedOperationParameter(name = "ip", description = "Target IP address that will be removed from blacklist")
    public void removeBlacklist(String ip){
        ips.remove(ip);
    }

    // 没有注解的方法不会暴露给JMX
    public boolean shouldBlock(String ip){
        return ips.contains(ip);
    }
}
