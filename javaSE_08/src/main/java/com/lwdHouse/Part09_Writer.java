package com.lwdHouse;

import java.io.*;

/**
 * Writer
 */
//        OutputStream	                    Writer
//        字节流，以byte为单位	                字符流，以char为单位
//        写入字节（0~255）：void write(int b)	写入字符（0~65535）：void write(int c)
//        写入字节数组：void write(byte[] b)	写入字符数组：void write(char[] c)
//        无对应方法	                        写入String：void write(String s)
public class Part09_Writer {
    public static void main(String[] args) throws IOException {
        fun1();
        fun2();
        fun3();
        fun4();
    }

    // FileWriter
    public static void fun1() throws IOException {
        try(Writer writer = new FileWriter("./javaCE_08/FileWriterTmp.txt")){
            writer.write((int)'h');
            writer.write("ello world!".toCharArray());
            writer.write("\nthanks");
        }
    }

    // CharArrayWriter
    public static void fun2() throws IOException {
        try(CharArrayWriter writer = new CharArrayWriter()){
            writer.write("hello Wrold");
            char[] res = writer.toCharArray();
            System.out.println(new String(res));
        }
    }

    // StringWriter
    public static void fun3() throws IOException {
        try(StringWriter writer = new StringWriter()){
            writer.write("hello world");
            String res = writer.toString();
            System.out.println(res);
        }
    }

    // OutputStreamWriter
    public static void fun4() throws IOException {
        OutputStream os = new FileOutputStream("./javaCE_08/FileWriterTmp2.txt");
        try(Writer writer = new OutputStreamWriter(os)){
            writer.write("hahahaha");
        }
    }
}
