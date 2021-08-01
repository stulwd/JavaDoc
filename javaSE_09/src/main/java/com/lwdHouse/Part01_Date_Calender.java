package com.lwdHouse;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 广义时间概念：狭义时间（年月日时分秒） + 时区，不能抛开时区来谈时间
 *
 * Date，SimpleDateFormat，Clender三大类的作用
 * Date是时间对象：是包含时区信息的，new Date()返回一个当前时间，sout打印的话，是按照当前时区时间打印
 * SimpleDateFormat：时间格式化工具，可以设置转换的目标时区，不设置的话默认当前时区为目标时区，调用format(Date date)来转换，
 *      会自动检测date的时区，把date时区的时间转换到目标时区的时间并打印出来
 * Calender日期对象: getInstance()获取当前日期。getTime()获取对应的Date。可以设置时间和时区信息，不设置的话取当前时区为时区。
 *
 * 例：如何计算A时区的时间t1对应的B地时间？
 *  创建Calender calA, clear()清空, set()设置A地时间, setTimeZone(A时区),
 *  calA.getTime()获取一个Date date
 *  创建SimpleDateFormat sdf，设置时区为B，然后sdf.format(date),返回的就是A地t1对应的B地时间
 *
 * SimpleDateFormat的format永远不会改变真正的时间
 *
 */
public class Part01_Date_Calender
{
    public static void main(String[] args) {
//        fun1();
//        fun2();
        fun3();
    }

    /**
     * Date基本用法
     *  Date包含了时区信息
     * SimpleFormat的基本用法
     *  SimpleFormat包含了时区信息，要是不设置，取当地时区
     */
    public static void fun1() {
        Date date = new Date();
        System.out.println(date.getYear() + 1900);
        System.out.println(date.getMonth() + 1);
        System.out.println(date.getDate());
        // 转为String
        System.out.println(date.toString());
        // 转换为GMT时区
        System.out.println(date.toGMTString());
        // 转换为本地时区
        System.out.println(date.toLocaleString());

        // 自定义格式输出,如果不设置时区，默认为当地时区
        var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println(sdf.format(new Date()));

        SimpleDateFormat sdf1 = new SimpleDateFormat("E MMM dd, yyyy");
//        sdf1.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println(sdf1.format(date));
    }

    /**
     * Calender基本用法
     *  Calendat包含了时区信息, 要是不设置，默认为当地时区
     * Calender.getTime()返回的 date 会转换为当地时间
     */
    public static void fun2() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        int w = c.get(Calendar.DAY_OF_WEEK);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        TimeZone zone = c.getTimeZone();
        System.out.println(zone.getID() + ":" + y + "-" + m + "-" + d + " " + w + " " + hh + ":" + mm + ":" + ss + "." + ms);

        // 设置特定的时间
        c = Calendar.getInstance();
        // 清楚掉所有时间
        c.clear();
        c.set(Calendar.YEAR, 1997);
        c.set(Calendar.MONTH, 2);
        c.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        // 如果没有上面这一行，下面获得的时间就是美国时间0:0分对应的中国时间
        System.out.println(sdf.format(c.getTime()));
    }

    /**
     * TimeZone
     */
    public static void fun3() {
        // 当前时区
        TimeZone tzDefault = TimeZone.getDefault();
        // GMT+9时区
            TimeZone tzGMT9 = TimeZone.getTimeZone("GMT+09:00");
        // 纽约时区
        TimeZone tzNY = TimeZone.getTimeZone("America/New_York");
        System.out.println(tzDefault.getID());
        System.out.println(tzGMT9.getID());
        System.out.println(tzNY.getID());
        // 获取所有时区
        System.out.println(Arrays.toString(TimeZone.getAvailableIDs()));

        // 对时间进行转换
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        c.set(2019, 10/*实际为11月*/, 11, 8, 15, 30);
        // 北京时间
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime()));
        // 转换为对应的纽约时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println(sdf.format(c.getTime()));

    }
}
