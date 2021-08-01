package com.lwdHouse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Instant时刻
 */
public class Part05_Instant {
    public static void main(String[] args) {
        Instant now = Instant.now();
        System.out.println(now);
        // 秒级时间戳
        System.out.println(now.getEpochSecond());
        // 毫秒级时间戳
        System.out.println(now.toEpochMilli());
        // 和now.toEpochMilli()一样
        System.out.println(System.currentTimeMillis());

        // 对于某一个时间戳，给它关联上指定的ZoneId，就得到了ZonedDateTime
        Instant ins = Instant.ofEpochSecond(1622743981);
        ZonedDateTime zdt = ins.atZone(ZoneId.of("America/New_York"));
        System.out.println(zdt);
    }
}
