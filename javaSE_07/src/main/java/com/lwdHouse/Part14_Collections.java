package com.lwdHouse;

import java.util.*;

/**
 * 使用Collections
 */
public class Part14_Collections {
    public static void main(String[] args) {
        // 添加元素
        Set<Integer> set = new HashSet<>();
        set.add(new Integer(1));
        set.add(new Integer(2));
        // public static boolean addAll(Collection<? super T> c, T... elements) { ... }
        Collections.addAll(set, 1, 2, 3);

        // 创建空集合, 下面是等价的
        List<String> list2 = List.of();
        List<String> list3 = Collections.emptyList();

        // 创建单元素集合
        List<String> list4 = List.of("apple");
        List<String> list5 = Collections.singletonList("apple");

        // 排序
        List<String> list6 = new ArrayList<>();
        list6.add("apple");
        list6.add("pear");
        list6.add("orange");
        Collections.sort(list6);
        System.out.println(list6);
        // 不能对不可变集合(ImmutableCollections)排序
        try{
            List<String> list7 = List.of("a", "b", "c");
            Collections.sort(list7);
        }catch (UnsupportedOperationException uso){
            System.out.println("不可操作");
        }

        // 洗牌
        List<Integer> list8 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list8.add(i);
        }
        Collections.shuffle(list8);
        for (Integer i : list8) {
            System.out.println(i);
        }

        // 不可变集合
        // 可变 => 不可变
        List<String> list9 = new ArrayList<>();
        list9.add("a");
        List<String> list_imu = Collections.unmodifiableList(list9);

        Set<String> set1 = new HashSet<>();
        set1.add("a");
        Set<String> set_imu = Collections.unmodifiableSet(set1);

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        Map<String, Integer> map_imu = Collections.unmodifiableMap(map);

        // 然而对于原来的集合做修改，还是会影响不可变集合
        System.out.println(map_imu);    // {a=1}
        map.remove("a");
        System.out.println(map_imu);    // {}
        // 所以不要轻易修改原集合


    }
}
