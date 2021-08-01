package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 序列化
 *  是指把java对象变为二进制byte[]
 * 反序列化
 *  恢复成java对象
 */
public class Part07_Serializable {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 使用ByteArray存放序列
        byte[] res = serialize(null);
        deSerialize(res);

        // 使用文件存放序列
        serializeToFile();
        deSerializeFromFile();
    }

    // 序列化
    public static byte[] serialize(String[] args) throws IOException {
        System.out.println("序列化：");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try(ObjectOutputStream output = new ObjectOutputStream(buffer)){
            output.writeInt(12345);
            output.writeUTF("Hello");
            output.writeObject(Double.valueOf(123.456));
        }
        System.out.println(Arrays.toString(buffer.toByteArray()));
        return buffer.toByteArray();
    }

    public static void deSerialize(byte[] data) throws IOException, ClassNotFoundException {
        System.out.println("反序列化");
        InputStream is = new ByteArrayInputStream(data);
        int resInt;
        String resStr;
        Double resDb;
        try (ObjectInputStream buffer = new ObjectInputStream(is)){
            // 反序列化时，顺序要与序列化一致
            resInt = buffer.readInt();
            resStr = buffer.readUTF();
            // 序列化对象时，对象必须实现Serializable接口
            resDb = (Double) buffer.readObject();
        }
        System.out.println(resInt);
        System.out.println(resStr);
        System.out.println(resDb);
    }


    // 序列化
    public static void serializeToFile() throws IOException {
        System.out.println("序列化：");

        OutputStream buffer = new FileOutputStream("./javaCE_08/serializeContent.bin");
        try(ObjectOutputStream output = new ObjectOutputStream(buffer)){
            output.writeInt(12345);
            output.writeUTF("Hello");
            output.writeObject(Double.valueOf(123.456));
        }
    }

    public static void deSerializeFromFile() throws IOException, ClassNotFoundException {
        System.out.println("反序列化");
        InputStream is = new FileInputStream("./javaCE_08/serializeContent.bin");
        int resInt;
        String resStr;
        Double resDb;
        try (ObjectInputStream buffer = new ObjectInputStream(is)){
            // 反序列化时，顺序要与序列化一致
            resInt = buffer.readInt();
            resStr = buffer.readUTF();
            resDb = (Double) buffer.readObject();
        }
        System.out.println(resInt);
        System.out.println(resStr);
        System.out.println(resDb);
    }
}
