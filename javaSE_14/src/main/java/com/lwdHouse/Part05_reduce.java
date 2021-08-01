package com.lwdHouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * reduce聚合
 */
public class Part05_reduce {
    public static void main(String[] args) {
//        fun1();
        fun2();
    }

    /**
     * reduce聚合的用法
     */
    public static void fun1() {
        Integer sum = Stream.of(1,2,3,4,5,6).reduce(0, new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });
        System.out.println(sum);

        // 如果没有给初始值，返回一个Optional
        Optional<Integer> sum2 = Stream.of(1,2,3,4,5,6).reduce((n1, n2) -> n1 + n2);
        // 因为有可能stream为空, 例如
        // Optional<Integer> sum2 = Stream.of(1,2,3,4,5,6).filter(n -> n > 10).reduce((n1, n2) -> n1 + n2);
        if (sum2.isPresent()){
            System.out.println(sum2.get());
        }else {
            System.out.println("没有值");
        }
    }

    /**
     * 聚合一个map
     */
    public static void fun2() {
        List<String> props = List.of("profile=native", "debug=true", "logging=warn", "interval=500");


        Map<String, String> map = props.stream().map(s -> {
            String[] kv = s.split("=");
            return Map.of(kv[0], kv[1]);
        }).reduce(new HashMap<String, String>(), (m, kv) -> {
            m.putAll(kv);
            return m;
        });
        map.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }
}
