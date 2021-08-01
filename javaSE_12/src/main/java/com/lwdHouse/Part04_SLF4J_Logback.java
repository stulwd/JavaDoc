package com.lwdHouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * SLF4J和Logback
 *      SLF4J：日志接口，相当于commons logging
 *      logback：日志实现，相当于Log4j，需要编写配置文件logback.xml
 * pom依赖：
 *      slf4j-api
 *      logback-classic（scope要设置正确）
 *      logback-core
 */
public class Part04_SLF4J_Logback {
    public static void main(String[] args) {
        new LoggerTest().fun();
    }
}

class LoggerTest{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public void fun(){
        logger.info("some information");
    }
}