
package com.lwdHouse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * commons Logging
 * 日志级别：
 *  FATAL
 *  ERROR
 *  WARNING
 *  INFO
 *  DEBUG
 *  TRACE
 *
 *  pom依赖：
 *   1.commons-logging
 */
public class Part02_CommonsLogging {
    public static void main(String[] args) {
        fun1();
    }

    public static void fun1() {
        Log log = LogFactory.getLog(Part02_CommonsLogging.class);
        log.info("start...");
        log.warn("end.");
        // 可以用log记录错误信息
        try{
            int n = 10 / 0;
        }catch (Exception e){
            log.error("运算错误", e);
        }
    }
}

// 如果在静态方法中引用Log，通常直接定义一个静态类型变量：
class LogTest{
    private static final Log log = LogFactory.getLog(LogTest.class);
    public static void fun1(){
        log.info("hello world!");
    }
}

// 在实例方法中引用Log，通常定义一个实例变量：
// 使用getClass()而不是LogTest2.class的好处是，当有个类继承了LogTest2时，子类获取的Class也是正确的
class LogTest2{
    protected final Log log = LogFactory.getLog(this.getClass());
    public void fun2(){
        this.log.info("hello world!");
    }
}