package com.lwdHouse.learnjava.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 *  更换数据库的aspect，配合RoutingWithSlave注解，切入到用RoutingWithSlave标注的方法
 *  在执行目标方法之前，前置通知的代码是try(new RoutingDataSourceContext("slaveDataSource")),
 *  RoutingDataSourceContext()构造函数里会设置 threadLocal，值代表当前线程所用的数据源字符串。
 *  在执行目标方法时，使用的数据源实际上是RoutingDataSource，RoutingDataSource在操作数据库前，会
 *  切换数据源，根据什么切换呢，就根据当时读取的threadLocal值，如果为“slaveDataSource”，就使用从数据源
 *  try结束时，RoutingDataSourceContext会调用close方法，close里会把threadLocal里的值移除
 * 【注意】：新添加的aspect不支持热更新，必须重启springboot
 */
@Aspect
@Component
public class RoutingAspect {
    @Around("@annotation(routingWithSlave)")
    public Object routingWithDataSource(ProceedingJoinPoint pjp, RoutingWithSlave routingWithSlave)
            throws Throwable {
        // 这里切换数据库，实际上实例化一个RoutingDataSourceContext即可，实例化的时候，就为我们设置了threadLocal
        try (RoutingDataSourceContext ctx = new RoutingDataSourceContext(RoutingDataSourceContext.SLAVE_DATASOURCE)){
            return pjp.proceed();
        }
    }
}
