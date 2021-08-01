package com.lwdHouse.ioc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * aop+annotation方式
 * 为带有某个注解的方法添加通知，这样每个方法就自己知道自己是否被安排了
 */
@Component
@Aspect
public class MetricAspect {

    // 表示为那些标注了@MetricTime注解的方法添加环绕通知
    @Around("@annotation(metricTime)")
    public Object metric(ProceedingJoinPoint pjp, MetricTime metricTime) throws Throwable {
        System.err.println("[Metrics] start");
        String name = metricTime.value();
        long start = System.currentTimeMillis();
        try{
            return pjp.proceed();
        }finally {
            long t = System.currentTimeMillis() - start;
            System.err.println("[Metrics] " + name + ":" + t + "ms");
        }
    }
}
