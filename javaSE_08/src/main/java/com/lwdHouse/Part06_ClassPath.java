package com.lwdHouse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 获取classPath资源
 *      使用Class.getResourceAsStream("path")方法，
 *      相对路径是Class所在的路径
 */
public class Part06_ClassPath {
    public static void main(String[] args) throws IOException {
        // 在classpath中的资源文件，路径总是以 / 开头
        try(InputStream defaultConf = Part06_ClassPath.class.getResourceAsStream("/default.properties");
            InputStream userConf = new FileInputStream("./javaCE_08/user.properties")){
            // 如果我们把默认的配置放到jar包中，
            // 再从外部文件系统读取一个可选的配置文件，
            // 就可以做到既有默认的配置文件，
            // 又可以让用户自己修改配置：
            Properties props = new Properties();
            if (defaultConf != null){
                props.load(defaultConf);
            }
            if (userConf != null){
                props.load(userConf);
            }

            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                System.out.println( entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
