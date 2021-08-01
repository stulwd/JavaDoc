package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 使用Properties
 */
public class Part7_Properties {
    public static void main(String[] args) throws IOException {
        // Properties用法
        // 1. 从文件流获取属性
        // 相对路径是javaCE
        System.out.println(new File("./").getAbsolutePath());
        String f = "./javaCE_07/src/main/java/com/lwdHouse/setting.properties";
        Properties props = new Properties();
        props.load(new FileInputStream(f));
        String filePath = props.getProperty("last_open_file");
        System.out.println(filePath);
        // 设置默认值
        String interval = props.getProperty("auto_save_interval", "120");
        System.out.println(interval);

        // 2. 从资源流获取属性
        // 要加上 / 才是classpath
        InputStream is = Part7_Properties.class.getResourceAsStream("/setting2.properties");
        Properties props2 = new Properties();
        props2.load(is);
        System.out.println(props2.getProperty("last_open_file"));

        // 3. 从内存中读取字节流
        String settings = "# setting.properties\n\n" +
                          "last_open_file=/data/hello世界.txt\n" +
                          "auto_save_interval=60";
        InputStream bis = new ByteArrayInputStream(settings.getBytes(StandardCharsets.UTF_8));
        Properties props3 = new Properties();
        props3.load(bis);
        System.out.println(props3.getProperty("last_open_file"));

        // 上面方法，如果有中文，会产生乱码
        // 4. 可以直接load(Reader)
        InputStream ris = Part7_Properties.class.getResourceAsStream("/setting2.properties");
        Reader rd = new InputStreamReader(ris, StandardCharsets.UTF_8);
        Properties props4 = new Properties();
        props4.load(rd);
        System.out.println(props4.getProperty("last_open_file"));

        // 注意：仅使用getProperty()和setProperty()方法，不要调用继承而来的get()和put()等方法。

    }
}
