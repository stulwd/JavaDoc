package com.lwdHouse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * ZonedDateTime，相比LocalDateTime，增加了时区
 * ZonedDateTime = LocalDateTime + ZoneId
 */
public class Part03_ZonedDateTime {
    public static void main(String[] args) {
//        fun1();
        fun2();
    }

    /**
     * 获得ZonedDateTime
     */
    public static void fun1() {
        ZonedDateTime zbj = ZonedDateTime.now();
        ZonedDateTime zny = ZonedDateTime.now(ZoneId.of("America/New_York"));
        System.out.println(zbj);
        // 2021-06-04T01:35:52.324112800+08:00[Asia/Shanghai]
        System.out.println(zny);
        // 2021-06-03T13:35:52.325110400-04:00[America/New_York]

        // 给LocalDateTime加ZoneId
        LocalDateTime dateTime = LocalDateTime.of(2021, 6, 4, 1, 40, 30);
//        LocalDateTime dateTime = LocalDateTime.now();
        ZonedDateTime zbj1 = dateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zny1 = dateTime.atZone(ZoneId.of("America/New_York"));
        System.out.println(zbj1);
        System.out.println(zny1);
    }

    /**
     * 时区转换
     */
    public static void fun2() {
        ZonedDateTime zbj = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime zny = zbj.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println(zbj);
        // 2021-06-04T01:47:42.756526900+08:00[Asia/Shanghai]
        System.out.println(zny);
        // 2021-06-03T13:47:42.756526900-04:00[America/New_York]
    }
}
