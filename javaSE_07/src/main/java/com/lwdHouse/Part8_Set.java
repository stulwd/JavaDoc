package com.lwdHouse;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 使用Set
 *  HashSet(常用)：
 *      HashSet仅仅是对HashMap的简单封装，HashSet持有一个HashMap，
 *      每次put时，都调用map的put，value放入的是统一的空Object。
 *      因为是使用了HashMap，所以和HashMap的存储结构，查询插入效率，有序性都是一致的。
 *      性质：
 *          1.对于Key，需要实现equals和hashCode方法，否则不能正常工作
 *          2.遍历是无序的
 *  TreeSet：
 *      持有一个NavigableMap。行为和TreeSet一致。是用树实现的。
 *      添加的元素必须支持compare，实现Comparable接口，或者传入Compartor排序算法
 */
public class Part8_Set {
    public static void main(String[] args) {
        // 使用HashSet
        Set<String> set = new HashSet<>();
        System.out.println(set.add("abc"));     // true, 添加成功
        System.out.println(set.add("abc"));     // false, 不能添加重复的
        System.out.println(set.contains("abc"));    // true
        System.out.println(set.size());
        System.out.println(set.remove("abc"));  // true

        // 遍历
        set.add("apple");
        set.add("banana");
        set.add("pear");
        set.add("orange");
        for (String s : set) {
            System.out.println(s);
        }
        // 依次输出
        // banana
        // orange
        // apple
        // pear
        // 可以发现，遍历时无需的


        // 如果使用TreeSet，遍历就是有序的
        Set<String> tset = new TreeSet<>(set);
        for (String s : tset) {
            System.out.println(s);
        }
        // 依次输出
        // apple
        // banana
        // orange
        // pear

    }
}
