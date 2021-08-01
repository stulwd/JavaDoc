package com.lwdHouse;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 反射：
 *      1. 动态代理
 *      2. 获取父类，接口，继承关系
 *      3. 构造方法
 *      4. 方法，字段
 *      5. 反射获取注解
 *
 */
public class Part01_Reflection {
    public static void main(String[] args) {

        // 动态代理
//        fun();
        // 获取父类继承
        fun2();
    }

    /**
     * 动态代理
     */
    public static void fun(){
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                if (method.getName().equals("morning")){
                    System.out.println("Good morning, "+ args[0]);
                }
                return null;
            }
        };

        Hello hello = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, handler);
        hello.morning("Bob");
    }

    public static void fun2() {
        // 获取父类
        Class i = Integer.class;
        Class n = i.getSuperclass();
        System.out.println(n);
        // class java.lang.Number
        Class o = n.getSuperclass();
        // class java.lang.Object
        System.out.println(o);
        System.out.println(o.getSuperclass());
        // null

        // 获取接口
        // 只返回当前类的接口，不包括父类
        Class[] itfs = i.getInterfaces();
        for (Class itf : itfs) {
            System.out.println(itf);
        }// interface java.lang.Comparable
        // 虽然接口的父接口是继承关系，但获取接口的父接口要用getInterfaces，不能用getsuperclass
        Class[] itfs2 = Closeable.class.getInterfaces();
        for (Class itf : itfs2) {
            System.out.println(itf);
        }

        // 判断继承关系
        System.out.println(Number.class.isAssignableFrom(Integer.class));
        System.out.println(Integer.class.isAssignableFrom(Number.class));
    }
}


interface Hello{
    void morning(String name);
}