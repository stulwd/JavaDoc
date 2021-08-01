package com.lwdHouse;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 其他操作
 *  1.排序
 *  2.去重
 *  3.截取
 *  4.合并
 *  5.平铺
 *  6.并行
 *  7.count()返回元素个数
 *  8.max(Comparator<? super T> cp) 找出最大的元素
 *  9.min(Comparator<? super T> cp) 找出最小的元素
 *  10.针对IntStream、LongStream、DoubleStream的方法
 *      - allMatch(Predicate<? super T>) 测试是否所有元素均满足
 *      - anyMatch(Predicate<? super T>) 测试是否至少一个元素满足
 */
public class Part07_other {
    public static void main(String[] args) {
//        fun1();
//        fun2();
//        fun3();
//        fun4();
//        fun5();
        fun6();
    }

    /**
     * 排序
     */
    public static void fun1() {
        List<String> Sortedfruits = List.of("Orange", "apple", "Banana")
                .stream()
                .sorted() // 要求每个元素都实现了Comparable接口，或者传入一个Comparotor即可, 如.sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        System.out.println(Sortedfruits);
    }

    /**
     * 去重
     */
    public static void fun2() {
        List<String> list = List.of("a", "b", "a", "c").stream().distinct().collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * 截取
     */
    public static void fun3() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7).stream().skip(2).limit(3).collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * 合并
     */
    public static void fun4() {
        Stream<String> stream1 = List.of("a", "b", "c").stream();
        Stream<String> stream2 = List.of("d", "e").stream();
        Stream<String> stream = Stream.concat(stream1, stream2);
        System.out.println(stream.collect(Collectors.toList()));
    }

    /**
     * 平铺
     */
    public static void fun5() {
        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1,2,3), Arrays.asList(4,5,6), Arrays.asList(7,8,9));
        Stream<Integer> stream1 = stream.flatMap(new Function<List<Integer>, Stream<Integer>>() {
            @Override
            public Stream<Integer> apply(List<Integer> list) {
                return list.stream();
            }
        });
        stream1.forEach(System.out::println);
    }

    /**
     * 并行
     */
    public static void fun6() {
        Stream<String> s = Stream.of("c", "b", "d", "a");
        String[] res = s.parallel() // 经过parallel()转换后的Stream只要可能，就会对后续操作进行并行处理
                        .sorted()
                        .toArray(String[]::new);
        for (String re : res) {
            System.out.println(re);
        }
    }
}
