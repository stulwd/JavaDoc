package com.lwdHouse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream输出为集合
 */
public class Part06_toCollection {
    public static void main(String[] args) {
//        fun1();
//        fun2();
//        fun3();
        fun4();
    }

    /**
     * 收集到List/Set
     */
    public static void fun1() {
        // 收集到List
        Stream<String> fruitStream = Stream.of("Apple", "", null, "Pear", " ", "Orange");
        List<String> fruitList = fruitStream.filter(s -> {
            return s != null && !s.isBlank();
        }).collect(Collectors.toList());
        fruitList.forEach(f -> System.out.println(f));
        // 收集到Set
        Set<String> fruitSet = Stream.of("Apple", "", null, "Pear", " ", "Orange").filter(s -> {
            return s != null && !s.isBlank();
        }).collect(Collectors.toSet());
    }

    /**
     * 收集到数组
     */
    public static void fun2() {
        String[] fruits = List.of("Apple", "Banana", "Orange").stream().toArray(String[]::new);
        for (String f : fruits) {
            System.out.println(f);
        }
    }

    /**
     * 收集到Map
     */
    public static void fun3() {
        Stream<String> stream = Stream.of("APPL:Apple", "MSFT:Microsoft");
        Map<String, String> map = stream.collect(Collectors.toMap(s -> s.split(":")[0], s -> s.split(":")[1]));
        map.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }

    /**
     * 分组输出
     */
    public static void fun4() {
        List<String> f = List.of("Apple", "Banana", "Blackberry", "Coconut", "Avocado", "Cherry", "Apricots");
        // Map<K, D>
        // K: String
        // D: List<String>
        Map<String, List<String>> groups = f.stream().collect(Collectors.groupingBy(s -> s.substring(0, 1), Collectors.toList()));
        System.out.println(groups);
    }
}
