package com.lwdHouse;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * TreeMap
 *  TreeMap是使用 树 来实现的
 * 对比HashMap：
 *            TreeMap           HashMap
 * 插入   比较快（树越大越慢）       特别快（直接算的索引）
 * 排序         支持              不支持
 */
//            ┌───┐
//            │Map│
//            └───┘
//              ▲
//         ┌────┴─────┐
//         │          │
//     ┌───────┐ ┌─────────┐
//     │HashMap│ │SortedMap│ 接口
//     └───────┘ └─────────┘
//                    ▲
//                    │
//               ┌─────────┐
//               │ TreeMap │
//               └─────────┘
public class Part6_TreeMap {
    public static void main(String[] args) {
        // 使用TreeMap
        // TreeMap的key必须实现 Comparable 接口
        Map<String, Integer> map = new TreeMap<>();
        map.put("orange", 1);
        map.put("apple", 2);
        map.put("pear", 3);
        // 这个keySet()对象使用的迭代器的next()方法，就是在遍历树的结点，所以是有序的
        for (String key : map.keySet()) {
            System.out.println(key);
        }
        // apple
        // orange
        // pear

        // 如果key对象没有实现Compareble接口，则需要指定一个排序算法
        Map<Person2, String> map1 = new TreeMap<>(new Comparator<Person2>() {
            @Override
            public int compare(Person2 o1, Person2 o2) {
                // 必须要有返回0的时候，因为get()方法中，就是用compare来做判断的，
                // 要是返回的值是0，就是要找的元素
                // 如果没有返回0，那么get任务元素都会找不到
                if (o1.score == o2.score){
                    return 0;
                }
                return o1.score > o2.score ? 1 : -1;

                // 或者直接调用下面方法
                // return Integer.compare(o1.score, o2.score);
            }
        });

        // 知识点：可以发现TreeMap中的Key不用覆写equals()和hashCode()，
        //        因为TreeMap不使用equals()和hashCode()存取
        //       但是要支持compare，实现Comparable接口，或者传入Compartor排序算法

    }
}

class Person2{
    String name;
    int score;
    public Person2(String name, int score) {
        this.name = name;
        this.score = score;
    }
}