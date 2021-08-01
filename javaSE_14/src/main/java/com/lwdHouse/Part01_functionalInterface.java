package com.lwdHouse;

import java.util.Arrays;
import java.util.Comparator;

/**
 * FunctionalInterface的使用
 */
public class Part01_functionalInterface {
    public static void main(String[] args) {

    }

    // FunctionalInterface的使用
    public static void fun1() {
        String[] array = new String[]{"Apple", "Orange", "Banana", "Lemon"};
        // 方法1. 定义匿名接口实现
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 方法2. 定义lambda函数
        Arrays.sort(array, (s1, s2) -> {
            return s1.compareTo(s2);
        });
        // 方法3. 使用某个方法
        Arrays.sort(array, String::compareTo);
        // 方法4. 使用某个方法
        Arrays.sort(array, Part01_functionalInterface::cmp);
    }

    public static int cmp(String s1, String s2){
        return s1.compareTo(s2);
    }
}
