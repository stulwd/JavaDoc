package com.lwdHouse.ioc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * aop+切入点表达式的方式
 */
// 用@Component表示它本身也是一个Bean
@Component
// 加上@Aspect注解，表示他的Before，Around注解会被扫描并织入到响应的方法上
@Aspect
// 此外，还要给@Configuration类加上@EnableAspectAutoProxy注解
public class LoggingAspect {

    @Before("execution(public * com.lwdHouse.ioc.UserService.*(..))")
    public void doAccessCheck() {
        System.out.println("[Before] do access check...");
    }

    @Around("execution(public * com.lwdHouse.ioc.MailService.*(..))")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        System.err.println("[Around] start: " + pjp.getSignature());;
        Object retVal = pjp.proceed();
        System.err.println("[Around] done: " + pjp.getSignature());
        return retVal;
    }
}
