package com.lwdHouse.learnjava.web.mbean;


import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Component
@ManagedResource(objectName = "sample:name=UserAmount", description = "registered User Amount")
public class UserRegistrationStatisticMBean {
     private AtomicInteger adder = new AtomicInteger(0);

    @ManagedAttribute(description = "the amount of users registered")
    public int getAmount(){
        return adder.get();
    }

    // 只有属性，没有操作

    public void updateAmount() {
        adder.incrementAndGet();
    }
}
