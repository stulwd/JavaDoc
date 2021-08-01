package com.lwdHouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Part1_List
{
    public static void main( String[] args )
    {
//        List用法
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add(null);
        list.add("pear");
        list.add("apple");
        System.out.println(list.size());
        String str = list.get(1);
        System.out.println(str);
//        创建List
        List<Integer> list2 = List.of(1, 2, 5);
//        List.of()方法不接受null值，如果传入null，会抛出NullPointerException异常

//        遍历List，坚持使用迭代器来遍历, ArrayList和LinkedList的迭代器的接口标准是一样的，但实现都不一样，但都是最高效的
        for (Iterator<Integer> it = list2.iterator(); it.hasNext(); ){
            Integer item = it.next();
            System.out.println(item);
        }
//        简化版如下
        for (Integer item : list2) {
            System.out.println(item);
        }
//        只要实现了Iterable接口的集合都可以直接使用foreach来遍历


//        List和Array的转换
//        List => Array
//        方法1. List.toArray()
        System.out.println("List.toArray()方法");
        Object[] array = list2.toArray();
//        这里不能把返回的Object[]类型强转为Integet[], 运行时会报错
        for (Object item : array) {
            System.out.println(item);
        }
//        方法1会丢失类型信息，所以实际应用很少。


//        方法2. List.toArray(T[] a), 返回的元素就会存储在a里
        System.out.println("List.toArray(T[] a)方法");
        Integer[] array2 = list2.toArray(new Integer[3]);
        for (Integer item : array2){
            System.out.println(item);
        }

//        方法3. List.toArray(IntFunction<T[]> generator)方法
        System.out.println("List.toArray(IntFunction<T[]> generator)");
        Integer[] array3 = list2.toArray(Integer[]::new);
        for (Integer item : array3){
            System.out.println(item);
        }


//        Array => List
//        方法1. 使用List.of()
//        这个方法返回的是只读的List，类型是ImmutableCollections.ListN
        Integer[] array4 = {1, 2, 3};
        List<Integer> list4 = List.of(array4);
        list4.add(999);
//        上面add会抛出UnsupportedOperationException异常，
//        因为List.of()方法返回的是一个ImmutableCollections.ListN的实现了List接口的对象，他的add方法会抛出这个异常
//        remove()方法也会抛出这个异常
    }
}
