package com.lwdHouse;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 使用map
 */
public class Part03_map {
    public static void main(String[] args) {
        fun1();
    }

    public static void fun1() {
        Stream<Integer> s = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> s2 = s.map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * integer;
            }
        });
        s2.forEach(System.out::println);
//        Stream<Integer> s3 = s.map((i) -> i*i);
//        s3.forEach(System.out::println);
        // 上面s2和s3的获取只能留一个，因为s只能处理一次，
        // 处理完再处理就会报错：stream has already been operated upon or closed

        List.of(" apple", " pear ", "Orange", "BanAna ")
                .stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .forEach(System.out::println);
    }
}
