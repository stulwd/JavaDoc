package com.lwdHouse;

/**
 * Log4j
 *  1.需要编写配置文件log4j2.xml
 *  2.需要和commons logging配合使用
 *      1）commons logging负责充当日志API，
 *      2）log4j负责实现日志底层
 * pom依赖：
 *  1.log4j-api
 *  2.log4j-core
 *  3.log4j-jcl
 *  4.commons-logging
 * 默认情况下，Commons Loggin自动搜索并使用Log4j，
 * 如果没有找到Log4j，再使用JDK Logging
 */
public class Part03_Log4j {
}
