package com.lwdHouse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 泛型和反射
 */
public class GenericType04 {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 这个clazz是Class<Object>类型
        Class clazz = String.class;
        String str = (String) clazz.newInstance();

        // 调用Class的getSuperclass()方法返回的Class类型是Class<? super Number>
        Class<? super Number> sup = Number.class.getSuperclass();
        Class<? super Integer> sup2 = Integer.class.getSuperclass();
        sup2 = sup;
//        反过来不可以
//        sup = sup2;

        // 构造方法Constructor<T> 也是泛型
        Class<Integer> claz = Integer.class;
        // getConstructor方法返回的是Constructor<T>类型，T是由claz的类型Class<T>的T决定,
        // 两个一样，所以要是 claz是Class<Object>,则创建出来的Constructor也是Constructor<Object>
        Constructor<Integer> constructor = claz.getConstructor(int.class);
        // newInstance返回的是T类型，T是由constructor的Constructor<T>的T决定
        constructor.newInstance(123);



    }
}
