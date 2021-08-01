package com.lwdHouse;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 使用Files
 */
public class Part11_Files {
    public static void main(String[] args) throws IOException {
        // 读取二进制文件的内容到byte[]
        byte[] data = Files.readAllBytes(Paths.get("./javaCE_08/pom.xml"));
        // 等效于  byte[] data = new FileInputStream("./javaCE_08/pom.xml").readAllBytes();

        // 读取文本文件到String
        String content = Files.readString(Paths.get("./javaCE_08/pom.xml"));
        // 等效于  String content = new String(new FileInputStream("./javaCE_08/pom.xml").readAllBytes());

        // 按行读取
        List<String> lines = Files.readAllLines(Paths.get("./javaCE_08/pom.xml"));

        // 写入二进制文件
        Files.write(Paths.get("./javaCE_08/FilesTmp"), new byte[]{0x01, 0x7f, 0x1f, 0x34});
        // 等效于new FileOutputStream("./javaCE_08/FilesTmp").write(new byte[]{0x01, 0x7f, 0x1f, 0x34});

        // 写入文本文件
        Files.writeString(Paths.get("./javaCE_08/FilesTmp2"), "文本文件", StandardCharsets.UTF_8);
        // 等效于  new FileOutputStream("./javaCE_08/FilesTmp2").write("文本文件".getBytes(StandardCharsets.UTF_8));
        // 等效于  new FileWriter("./javaCE_08/FilesTmp2").write("文本文件");

    }
}
