package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * outputStream
 */
public class Part03_OutputStream {
    public static void main(String[] args) throws IOException {
//        OutputStream os = new FileOutputStream("./javaCE_08/test/");
//        os.write(72);
//        os.write(101);
//        os.write(108);
//        os.write(108);
//        os.write(111);
//        os.close();

        try(OutputStream os2 = new FileOutputStream("./javaCE_08/test")){
            os2.write("hello世界".getBytes(StandardCharsets.UTF_8));
        }// 自动close

        // ByteArrayOutputStream
        byte[] buffer;
        try(ByteArrayOutputStream os3 = new ByteArrayOutputStream()){
            os3.write("你好world".getBytes(StandardCharsets.UTF_8));
            buffer = os3.toByteArray();
            System.out.println(new String(buffer, "UTF-8"));
        }

        // 复制文件
        File origin = new File("./javaCE_08/test");
        File dest = new File("./javaCE_08/test_bak");
        try(InputStream is = new FileInputStream(origin);
            OutputStream os = new FileOutputStream(dest)){
            os.write(is.readAllBytes());
        }
    }
}
