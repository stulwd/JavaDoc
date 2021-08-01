package com.lwdHouse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * DateTimeFormatter:时间格式化工具
 * 相比SimpleDateFormatter，线程安全
 *   SimpleDateFormatter线程不安全，每次只能new一个新的对象使用
 *   DateTimeFormatter可以只创建新的对象，到处引用
 */
public class Part04_DateTimeFormatter {
    public static void main(String[] args) {
        fun1();
    }

    public static void fun1() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZZZ");
        ZonedDateTime zdt = ZonedDateTime.now();
        System.out.println(formatter.format(zdt));
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss ZZZZ", Locale.CHINA);
        System.out.println(formatter1.format(zdt));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss ZZZZ", Locale.US);
        System.out.println(formatter2.format(zdt.withZoneSameInstant(ZoneId.of("America/New_York"))));


    }
}
