package com.lwdHouse;

import java.util.logging.Logger;

/**
 * JDK Logging
 * 七个级别：
 *      SEVERE
 *      WARNING
 *      INFO
 *      CONFIG
 *      FINE
 *      FINER
 *      FINEST
 * 默认级别就是FINE, FINE以下的不会输出
 * 需要在JVM启动时传递参数 -Djava.util.logging.config.file=<config-file-name>
 * 进行设置
 * 不方便，一般不用这个系统的日志系统
 */
public class Part01_Log {
    public static void main(String[] args) {
        fun1();
    }

    public static void fun1() {
        Logger logger = Logger.getGlobal();
        logger.info("start process");
        // 信息: start process
        logger.warning("memory is running out");
        // 警告: memory is running out
        logger.fine("ignored.");
        // 不输出
        logger.severe("process will be terminated...");
        // 严重: process will be terminated...
        logger.info("end");
        // 信息: end
    }
}
