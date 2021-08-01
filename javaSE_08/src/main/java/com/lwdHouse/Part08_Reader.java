package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Reader: 输入字符流
 */
// InputStream和Reader比较
//    InputStream	                       Reader
//    字节流，以byte为单位                    字符流，以char为单位
//    读取字节（-1，0~255）：int read()       读取字符（-1，0~65535）：int read()
//    读到字节数组：int read(byte[] b)        读到字符数组：int read(char[] c)



public class Part08_Reader {
    public static void main(String[] args) throws IOException {
//        fun1();
//        fun2();
//        fun3();
//        fun4("hello world!");
//        fun5("hello world!");
        fun6("abcd1234哈哈哈");
    }

    // 使用Reader读文件
    public static void fun1() throws IOException {
        // 为了防止中文出现乱码，最好加上编码格式
        Reader reader = new FileReader("./javaCE_08/src/main/java/com/lwdHouse/Part08_Reader.java",
                                        StandardCharsets.UTF_8);
        for (;;){
            int n = reader.read();
            if (n == -1){
                break;
            }
            System.out.print((char)n);
        }
        reader.close();
    }

    // 配合try(resource){...}使用Reader
    public static void fun2() throws IOException {
        // 为了防止中文出现乱码，最好加上编码格式
        try(Reader reader = new FileReader("./javaCE_08/src/main/java/com/lwdHouse/Part08_Reader.java",
                                            StandardCharsets.UTF_8))
        {
            for (;;){
                int n = reader.read();
                if (n == -1){
                    break;
                }
                System.out.print((char)n);
            }
        }// 自动执行reader.close();
    }

    // 读取到缓冲区
    public static void fun3() throws IOException {
        try(Reader reader = new FileReader("./javaCE_08/src/main/java/com/lwdHouse/Part08_Reader.java",
                                            StandardCharsets.UTF_8)){
            char[] buffer = new char[1000];
            for (;;){
                int n = reader.read(buffer);
                if (n == -1){
                    return;
                }
                System.out.println("\n读取了" + n + "字符");
                for (int i = 0; i < n; i++) {
                    System.out.print((char) buffer[i]);
                }
            }
        }
    }

    // 使用CharArrayReader
    public static void fun4(String str) throws IOException {
        try(Reader reader = new CharArrayReader(str.toCharArray())){
            char[] buf = new char[100];
            while (true){
                int n = reader.read(buf);
                if (n == -1){
                    break;
                }
                for (int i = 0; i < n; i++) {
                    System.out.print((char) buf[i]);
                }
            }
        }
    }

    // 使用StringReader
    public static void fun5(String str) throws IOException {
        try(Reader reader = new StringReader(str)){
            char[] buf = new char[100];
            while (true){
                int n = reader.read(buf);
                if (n == -1){
                    break;
                }
                for (int i = 0; i < n; i++) {
                    System.out.print((char) buf[i]);
                }
            }
        }
    }

    // 使用InputStreamReader
    public static void fun6(String str) throws IOException {
        InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        try(Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){
            char[] buf = new char[100];
            while (true){
                int n = reader.read(buf);
                if (n == -1){
                    break;
                }
                for (int i = 0; i < n; i++) {
                    System.out.print((char) buf[i]);
                }
            }
        }

    }
}
