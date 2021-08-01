package com.lwdHouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * super通配符
 */
public class GenericType03 {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.set(0, 12);
        list.set(0, new Number(12))
    }
    public static void set(List<? super Integer> list){
        list.set(0, new Integer(11));
        // 以下会报错，原因如Generic02，实参是Number，大于可能形参
        list.set(1, (Number) new Double(12.0));
        // 报错原因是找不到一个最终接受类型，超越Integer所有的超类
        Integer res = list.get(1);
        // 除非使用Object类型
        Object res1 = list.get(1);
        // 所以在下界通配符中，不允许使用T xxx()方法
    }
}

/**
 * 对比extends和super通配符
 */
// <? extends T>允许调用读方法T get()获取T的引用，但不允许调用写方法set(T)传入T的引用（传入null除外）
// <? super T>允许调用写方法set(T)传入T的引用，但不允许调用读方法T get()获取T的引用（获取Object除外）

/**
 * PECS原则:
 *    Producer Extends Consumer Super
 *    producer就是返回T的方法
 *    Consumer就是需要T为参数的方法
 */
