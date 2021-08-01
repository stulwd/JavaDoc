package com.lwdHouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Stream创建
 *  1.Stream.of
 *  2.基于数组或Collection
 *  3.基于supplier
 *  4.其他方法
 *  5.创建基本类型Stream
 */
public class Part02_Stream_Create {
    public static void main(String[] args) {
//        fun1();
//        fun2();
//        fun3();
//        fun4();
        fun5();
    }

    /**
     * 1.Stream.of创建
     */
    public static void fun1() {
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        // void forEach(Consumer<? super T> action);
        stream.forEach(System.out::println);
    }

    /**
     * 基于数组或Collection创建
     */
    public static void fun2() {
        Stream<String> stream1 = Arrays.stream(new String[]{"a", "b", "c", "d"});
        Stream<String> stream2 = List.of("x", "y", "z").stream();
        stream1.forEach(System.out::println);
        stream2.forEach(System.out::print);
    }

    /**
     * 基于supplier
     */
    public static void fun3() {
        LocalDateTime.now();
        Stream<LocalDateTime> stream1 = Stream.generate(LocalDateTime::now);
        Stream<Integer> stream2 = Stream.generate(new Supplier<Integer>() {
            Integer n = 0;
            @Override
            public Integer get() {
                return n++;
            }
        });
        stream1.limit(20).forEach(new Consumer<LocalDateTime>() {
            @Override
            public void accept(LocalDateTime localDateTime) {
                System.out.println(localDateTime.atZone(ZoneId.of("Asia/Shanghai")));
            }
        });
        stream2.limit(20).forEach(System.out::println);
    }

    /**
     * 其他方法
     */
    public static void fun4() {
        // Files.lines返回每一行组成的Stream<String>
        try(Stream<String> lines = Files.lines(Path.of("javaCE_14/pom.xml"))){
//            lines.forEach(System.out::println);
            getAll all = new getAll();
            lines.forEach(all);
            System.out.println(all.content);
        }catch (IOException e){
        }

        // Pattern.splitAsStream返回结果组成的Stream<String>
        Pattern pat = Pattern.compile("\\s+");
        Stream<String> stream = pat.splitAsStream("The quick brown fox jumps over the lazy dog");
        stream.forEach(System.out::println);
    }

    /**
     * 基本类型Stream
     */
    public static void fun5() {
        IntStream stream = Arrays.stream(new int[]{1, 2, 3, 4});
        stream.forEach(System.out::println);
        LongStream longStream = List.of("1", "2", "3", "4").stream().mapToLong(Long::parseLong);
        longStream.forEach(System.out::println);
    }
}

class getAll implements Consumer<String>{
    public String content;
    @Override
    public void accept(String s) {
        content += s + "\n";
    }
}