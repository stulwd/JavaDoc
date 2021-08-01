package com.lwdHouse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Lambda基础
 */
public class Lambda
{
    public static void main( String[] args ) throws IOException {
        byte[] byteArray = "K".getBytes(StandardCharsets.UTF_8);
        System.out.println("K的utf-8码");
        // 4b
        for (byte b : byteArray) {
            System.out.println(Integer.toHexString((int)b));
        }

        byte[] byteArray1 = "们".getBytes(StandardCharsets.UTF_8);
        System.out.println("们的utf-8码");
        // e4bbac
        for (byte b : byteArray1) {
            System.out.println(Integer.toHexString(b));
        }
        System.out.println("K的unicode:"+Integer.toHexString((int)'K'));     // 4b
        System.out.println("\"们\"的unicode:"+Integer.toHexString((int)'们'));     // 4eec
        // 在java1.8之前，如果想调用Arrays.sort()时，可以传入一个Comparator实例，
        // 以匿名类的方式编写如下
        String[] array = new String[10];
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // 从java1.8之后，可以用lambda表达式替换单方法接口。
        Arrays.sort(array, (s1, s2) -> {
            return s1.compareTo(s2);
        });
        System.out.println(String.join(", ", array));

        // 我们把只定义了单方法的接口称为 FunctionalInterface，
        // 用@FunctionalInterface注解标记

        List list = new ArrayList();
        list.add("Hello");
        String str = (String) list.get(0);

        List<Number> list1 = new ArrayList<>();
        list1.add(new Integer(123));

        String[] ss = new String[] { "Orange", "Apple", "Pear" };
        Arrays.sort(ss);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(buffer)) {
            // 写入int:
            output.writeInt(12345);
            // 写入String:
            output.writeUTF("Hello");
            // 写入Object:
            output.writeObject(Double.valueOf(123.456));
        }



    }
}


class CountInputStream extends FilterInputStream {
    private int count = 0;

    CountInputStream(InputStream in) {
        super(in);
    }

    public int getBytesRead() {
        return this.count;
    }

    public int read() throws IndexOutOfBoundsException, IOException {
        int n = in.read();
        if (n != -1) {
            this.count ++;
        }
        return n;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int n = in.read(b, off, len);
        if (n != -1) {
            this.count += n;
        }
        return n;
    }
}
