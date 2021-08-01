package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 操作zip：使用ZipInputStream
 *         ZipInputStream是一种FilterInputStream
 */
public class Part05_Zip {
    public static void main(String[] args) throws IOException {
//        readZip(null);
        writeZip(null);
    }

    // 写入zip
    // 把当前目录./压缩成abc.zip
    public static void writeZip(String[] args) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream("./javaCE_08/abc.zip"))){
            writeZip(zip, new File("./"));
        }
    }

    private static void writeZip(ZipOutputStream zip, File file) throws IOException {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()){
                ZipEntry entry = new ZipEntry(f.getParent() + "/" + f.getName() + "/");
                zip.putNextEntry(entry);
                zip.closeEntry();
                writeZip(zip, f);   // 递归写入
            }else {
                ZipEntry entry = new ZipEntry(f.getParent() + "/" + f.getName());
                zip.putNextEntry(entry);
                zip.write(new FileInputStream(f).readAllBytes());
                zip.closeEntry();
            }
        }
    }

    // 读取zip
    public static void readZip(String[] args) throws IOException {
        try(ZipInputStream zip = new ZipInputStream(new CountInputStream(new FileInputStream("./javaCE_08/abc.zip")))){
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null){
                String name = entry.getName();
                System.out.println();
                System.out.println("文件名："+name);
                if(!entry.isDirectory()){
//                    byte[] bytes = zip.readAllBytes();
//                    FileOutputStream output = new FileOutputStream("c:\\dest\\" + entry.getName());
//                    output.write(zip.readAllBytes());
                    int n;
                    while ((n = zip.read()) != -1){
                        System.out.print((char) n);
                    }
                }
            }
        }
    }
}

