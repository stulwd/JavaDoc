package com.lwdHouse;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File对象
 *
 */
public class Part01_File
{
    public static void main( String[] args ) throws IOException {
        File f = new File(".\\sub\\..\\javac");
        System.out.println(f);
        System.out.println(f.getPath());                // 路径
        System.out.println(f.getAbsolutePath());        // 绝对路径
        System.out.println(f.getCanonicalPath());       // 标准路径
        // 文件分隔符，windows是\ linux是/
        System.out.println(File.separator);
        // 判断目录还是文件
        System.out.println(new File(".\\").isDirectory());              // true
        System.out.println(new File(".\\javaCE_08\\pom.xml").isFile());  // true
        // 文件权限获取
        System.out.println(new File(".\\").canRead());  // true
        System.out.println(new File(".\\").canWrite()); // true
        System.out.println(new File(".\\").canExecute());  // true  目录可执行代表是否可以使用ls或者dir命令查看

        // 创建和删除文件
        File file = new File(".\\javaCE_08\\new.txt");
        if (file.createNewFile()){
            System.out.println("文件创建成功");
        }
        if (file.delete()){
            System.out.println("删除成功");
        }
        // 创建删除临时文件, 只需提供前缀和后缀
        File tmpFile = File.createTempFile("tmp-", ".txt");
        f.deleteOnExit();   // 在JVM退出时删除该文件
        System.out.println(tmpFile.isFile());
        System.out.println(tmpFile.getAbsolutePath());
        // 临时文件创建在了 C:\Users\wendi\AppData\Local\Temp\tmp-12500290760759682138.txt

        // 遍历文件和目录
        String res = printFiles(new File(".\\"), 0, 2, null);
        System.out.println(res);
        // 过滤,只留下xml文件
        String res2 = printFiles(new File(".\\"), 0, -1, new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });
        System.out.println(res2);

        // 创建目录
        File dir = new File(".\\javaCE_08\\dir");
        if (dir.mkdir()){
            System.out.println("创建成功");
        }
        if (dir.delete()){
            System.out.println("删除成功");
        }
        // 多级目录都要创建，可用mkdirs

        // 使用Path
        Path p1 = Paths.get("a", "b", "c", "..");
        System.out.println(p1);                         // a\b\c\..
        System.out.println(p1.toAbsolutePath());        // C:\Users\wendi\Desktop\javaCE\a\b\c\..
        System.out.println(p1.normalize());             // a\b
        File f1 = p1.toFile();
        System.out.println(f1);                         // a\b\c\..
        for (Path p : Paths.get("..").toAbsolutePath()) {
            System.out.print("  " + p);
        }



    }

    private static String printFiles(File file, int layer, int layerLimit, FilenameFilter filter){
        StringBuilder sb = new StringBuilder("");
        if (layer == layerLimit){
            return sb.toString();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isDirectory()){
                String res = printFiles(f, layer+1, layerLimit, filter);
                // String的判断一定要用equals，而不是==
                if (!res.equals("")){
                    int sps = layer;
                    while (sps != 0){
                        sps--;
                        sb.append("  ");
                    }
                    sb.append(f.getName());
                    sb.append("\n");
                    sb.append(res);
                }
            }else {
                if (filter == null || filter.accept(null, f.getName())){
                    int sps = layer;
                    while (sps != 0){
                        sps--;
                        sb.append("  ");
                    }
                    sb.append(f.getName());
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}
