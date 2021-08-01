package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * PrintStream
 *  是一种 FilterOutputStream，在OutputStream的基础上，额外提供了写入各种数据类型的方法
 *  print(int)
 *  print(booleam)
 *  print(String)
 *  print(Object)
 *  以及println()
 *
 * System.out是系统默认提供的 PrintStream，表示标准输出
 * System.err是系统默认提供的 PrintStream，表示标准错误输出
 */
public class Part10_PrintStream_PrintWriter {
    public static void main(String[] args) throws IOException {
        // PrintStream
        // out 也是一种 OutputStream
        System.out.write("hello世界\n".getBytes(StandardCharsets.UTF_8));
//        System.err.write("error\n".getBytes(StandardCharsets.UTF_8));
        System.out.write("你好\n".getBytes(StandardCharsets.UTF_8));
        // 建议使用print()和println(), 好处是不会抛出错误
        System.out.println("哈哈哈");
        OutputStream os = System.out;
//        try (PrintStream ps = new PrintStream(os)){
//            ps.println("你好呀");
//        }// 这里会关闭System.out, 所以不要乱包装System.out
        System.out.println("ssss");

        // PrintWriter
        StringWriter buffer = new StringWriter();
        try (PrintWriter pw = new PrintWriter(buffer)) {
            pw.println("Hello");
            pw.println(12345);
            pw.println(true);
        }
        System.out.println(buffer.toString());


    }

}
