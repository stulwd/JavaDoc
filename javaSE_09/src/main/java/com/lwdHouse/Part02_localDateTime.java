package com.lwdHouse;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * LocalDateTime：狭义的时间，不带时区的时间
 */
public class Part02_localDateTime {

    public static void main(String[] args) {
//        fun1();
//        fun2();
//        fun3();
        fun4();
    }


    /**
     * LocalDateTime
     */
    public static void fun1(){
        LocalDate d = LocalDate.now();
        LocalTime t = LocalTime.now();
        LocalDateTime dt = LocalDateTime.now();
        System.out.println(d);
        System.out.println(t);
        System.out.println(dt);

        // LocalDateTime转LocalDate和LocalTime
        LocalDate d1 = dt.toLocalDate();
        LocalTime t1 = dt.toLocalTime();
        System.out.println(d1);
        System.out.println(t1);

        // 工厂方法创建
        LocalDate d2 = LocalDate.of(2019, 11, 12);  // 2019-11-12
        LocalTime t2 = LocalTime.of(15, 16, 17);    // 15:16:17

        // 解析
        LocalDateTime dt2 = LocalDateTime.parse("2019-11-12T15:16:17");
        LocalDate d3 = LocalDate.parse("2019-11-12");
        LocalTime t3 = LocalTime.parse("15:16:17");
        System.out.println(dt2);
        System.out.println(d3);
        System.out.println(t3);
    }

    /**
     * DateTimeFormatter
     */
    private static void fun2() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));
//        dtf.parse("xxx");

        LocalDateTime dt = LocalDateTime.parse("2021*6*04 12-30-00", DateTimeFormatter.ofPattern("yyyy*M*dd HH-mm-ss"));
        System.out.println(dt);
    }

    /**
     * 加减日期,调整日期,比较日期时间
     *      所有的操作，返回的都是新的对象
     */
    public static void fun3() {
        // 加减日期
        LocalDateTime dt = LocalDateTime.of(2012, 12, 12, 8, 30, 59);
        System.out.println(dt);
        LocalDateTime dt2 = dt.plusDays(5).minusHours(3).minusMonths(1);
        System.out.println(dt2);

        // 调整日期
        LocalDateTime dt3 = LocalDateTime.of(2019, 10, 26, 20, 30, 59);
        System.out.println(dt3);
        LocalDateTime dt4 = dt3.withDayOfMonth(21);
        System.out.println(dt4);
        LocalDateTime dt5 = dt4.withMonth(9);
        System.out.println(dt5);

        // 使用with
        LocalDateTime d = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        System.out.println(d);
        // 本月最后一天
        // with方法，传入一个TemporalAdjuster对象
        LocalDate d2 = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        // 下月第一天
        LocalDate d3 = d2.with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println(d3);
        // 本月第一个周一
        LocalDate d4 = d3.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        System.out.println(d4);

        // 比较
        System.out.println(LocalDate.now().isAfter(LocalDate.of(2021, 6, 3)));
        System.out.println(LocalTime.now().isBefore(LocalTime.of(1, 23, 40)));
    }

    /**
     * Duration和Period
     */
    public static void fun4() {
        // duration
        LocalDateTime start = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 9, 19, 25, 30);
        Duration d = Duration.between(start, end);
        System.out.println(d);
        // PT1235H10M30S
        // 表示1235小时10分钟30秒

        // period
        Period p = LocalDate.of(2019, 11, 19).until(LocalDate.of(2021, 1, 9));
        System.out.println(p);
        // P1Y1M21D
        // 表示1个月21天
    }
}
