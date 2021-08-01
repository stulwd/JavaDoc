package com.lwdHouse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Part06_new_old_API {
    public static void main(String[] args) {

        Instant now = Instant.now();
        now.getEpochSecond();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime zonedDateTime2 = now.atZone(ZoneId.of("America/New_York"));
        System.out.println(zonedDateTime);
        System.out.println(zonedDateTime2);
        Date date = new Date();
        Instant instant = date.toInstant();
    }

    /**
     * 旧API转新的API
     *  通过时间戳
     */
    public static void fun1() {
        // 获取当前时间
        Calendar c = Calendar.getInstance();
        // 获取时间戳
        Instant ins = c.toInstant();
        // 通过时间戳创建ZonedDateTime
        ZonedDateTime zonedDateTime = ins.atZone(c.getTimeZone().toZoneId());
        // Date类型可以先用Calendar.set(Date date)转为Calendar，再按照上面转
    }

    /**
     * 新API转旧API
     */
    public static void fun2() {
        ZonedDateTime now = ZonedDateTime.now();
        long ts = now.toEpochSecond() * 1000;
        Date date = new Date(ts);

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTimeZone(TimeZone.getTimeZone(now.getZone()));
        cal.setTimeInMillis(ts);


    }
}
