package com.lwdHouse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配规则
 *   . 匹配任意一个字符，不能匹配空字符
 *   \d 可以匹配一个数字
 *   \w 匹配一个字母、数字、下划线
 *   \s 匹配一个空格、一个tab字符
 *   \t 匹配一个tab
 *   \D 非数字
 *   \W 非\w
 *   \S 非\s
 *   * 匹配多个
 *   + 匹配至少一个
 *   ？ 匹配0个或1个
 *   {n, m} 匹配n到m个
 *   {n, } 匹配至少n个
 *   [1-9] 匹配1到9的一个字符
 *   [0-9a-fA-F] 匹配一个16进制字符
 *   [^1-9]匹配3个非1-9的字符
 *   | 或匹配。 AB|CD 匹配AB或者CD
 *   learn (java|php|go), 用括号包括或
 * 分组匹配：
 *   使用括号，详情fun2()
 */
public class regex {
    public static void main(String[] args) throws IOException {
//        fun1();
//        fun2();
//        fun3();
        fun4();
    }

    /**
     * java正则匹配的用法
     */
    public static void fun1() {
        // 正则表达式中特殊字符需要用\
        // 字符串的\是用\\反斜杠表示，所以正则表达式a\&b，要表示成
        String re = "a\\&b";
        System.out.println("a&b".matches(re));
        System.out.println("a-c".matches(re));
        System.out.println("a&&c".matches(re));
        System.out.println("a和c".matches("a和c"));
    }

    /**
     * 分组匹配
     */
    public static void fun2() {
        // String.matches()方法内部调用的就是Pattern和Matcher类的方法
        System.out.println("(go)".matches("\\((go|java|)\\)"));
        Pattern p = Pattern.compile("(\\d{3,4})\\-(\\d{7,8})");
        Matcher m = p.matcher("010-12345678");
        if (m.matches()){
            String g1 = m.group(1);
            String g2 = m.group(2);
            System.out.println(g1);
            System.out.println(g2);
        }else {
            System.out.println("匹配失败");
        }
    }

    /**
     * 非贪婪匹配（默认都是贪婪匹配）
     */
    public static void fun3() {
        // 默认都是贪婪匹配
        Pattern p = Pattern.compile("(\\d+)(0*)");
        Matcher m = p.matcher("123000");
        if (m.matches()){
            System.out.println("第一组" + m.group(1));
            // 第一组123000
            System.out.println("第二组" + m.group(2));
            // 第二组
        }else {
            System.out.println("匹配失败");
        }

        // 非贪婪匹配，加?即可表示非贪婪
        Pattern p1 = Pattern.compile("(\\d+?)(0*)");
        Matcher m1 = p1.matcher("1230000");
        if (m1.matches()){
            System.out.println("第一组" + m1.group(1));
            // 第一组123
            System.out.println("第二组" + m1.group(2));
            // 第二组000
        }else {
            System.out.println("匹配失败");
        }
    }

    /**
     * 分割、搜索、替换、反向引用
     */
    public static void fun4() throws IOException {
        // 分割字符串
        System.out.println(Arrays.toString("a, b;c".split("[\\,\\;\\s]+")));

        // 搜索字符串
        String content = "The test in designated sites is required for the registered population and temporary residents, on the basis of their registered addresses or communities.\n" +
                "\n" +
                "While waiting in line for the test, residents should wear a mask all the time, and keep a distance of more than 1 meter from others, as well as avoid conversation and gathering in groups.\n" +
                "\n" +
                "If people just receive the COVID-19 vaccine (either the first or second dose) , they should take their nucleic acid test at least 24 hours after the vaccine.\n" +
                "\n" +
                "Suspension of dine-in services expanded to whole Panyu District\n" +
                "\n" +
                "According to the latest notice by Panyu authority on June 3, the district has decided to suspend dine-in services of all restaurants in the district for the next 14 days.\n" +
                "\n" +
                "Previously, the district had suspended dine-in services of all restaurants at Shibi Street (石壁街), Luopu Street (洛浦街)，Nancun Community (南村镇南村社区), Meishan Village (梅山村) and Kengtou Village (坑头村) from June 2.";
        Pattern p = Pattern.compile("(\\wo\\w)");
        Matcher m = p.matcher(content);
        System.out.println(m.matches()); // 搜索的时候，肯定会返回false
        while (m.find()){
            System.out.println(m.group(1));
//            String sub = content.substring(m.start(), m.end());
//            System.out.println(sub);
        }

        // 替换字符串
        String str2 = content.replaceAll("\\s+", "__");
        System.out.println(str2);

        // 反向引用
        String str3 = content.replaceAll("\\s+([a-z]{3,4})\\s+", " <$1> ");
        System.out.println(str3);

        // 模板引擎
        String cont = "Hello, ${name}! You are learning ${lang}!";
        System.out.println(cont);
//        Map<String, String> map = Map.of("name", "Bob",
//                                         "lang", "Java");
        Properties prop = new Properties();
        prop.load(regex.class.getResourceAsStream("/user.properties"));
//        for (String key : map.keySet()) {
//            cont = cont.replaceAll("\\$\\{"+key+"\\}", map.get(key));
//        }
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            cont = cont.replaceAll("\\$\\{"+entry.getKey()+"\\}", (String) entry.getValue());
        }
        System.out.println(cont);
    }
}
