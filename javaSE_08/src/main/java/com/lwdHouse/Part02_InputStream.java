package com.lwdHouse;

import java.io.*;

/**
 * InputStream
 */
public class Part02_InputStream {
    public static void main(String[] args) throws IOException {
        fun1(null);
//        fun2(null);
//        fun3(null);
//        fun4(null);
//        fun5(null);
    }

    public static void fun1(String[] args) throws IOException {
        InputStream input = new FileInputStream("./javaCE_08/pom.xml");
        for (;;){
            // read读取字节流,read是阻塞的
            int n = input.read();
            if (n == -1){
                break;
            }
            System.out.print((char) n);
        }
        input.close();
    }

    // 使用try(resource){}，因为InputStream是AutoCloseable的
    public static void fun2(String[] args) throws IOException {
        try(InputStream is = new FileInputStream("./javaCE_08/pom.xml")){
            int n;
            while ((n = is.read()) != -1){
                System.out.print((char)n);
            }
            System.out.println();
        }// 在结束的地方自动调用close方法
    }

    // 读到缓冲区, 一次读取多个字节，效率更高
    public static void fun3(String[] args) throws IOException {
        byte[] buffer = new byte[1000];
        int n;
        // 共有两千多字符，需要分三次读
        try(InputStream is2 = new FileInputStream("./javaCE_08/pom.xml")) {
            while ((n = is2.read(buffer)) != -1){
                System.out.println("read" + n + " bytes.");
                for (int i = 0; i < n; i++) {
                    System.out.print((char)buffer[i]);
                }
            }
        }
    }

    // 使用ByteArrayInputStream
    public static void fun4(String[] args) throws IOException {
        byte[] data = {72, 101, 108, 108, 111, 33 };
        try(InputStream is = new ByteArrayInputStream(data)){
            int n;
            while ((n = is.read()) != -1){
                System.out.print((char) n);
            }
        }
    }

    // 读取文件内容到字符串
    public static void fun5(String[] args) throws IOException {
        try(InputStream is = new FileInputStream("./javaCE_08/pom.xml")){
            String res = readAsString(is);
            System.out.println(res);
        }
    }

    public static String readAsString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[1000];
        int n;
        while ((n = is.read(bytes)) != -1){
            for (int i = 0; i < n; i++) {
                sb.append((char) bytes[i]);
            }
        }
        return sb.toString();
    }


}
