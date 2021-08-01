package com.lwdHouse;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Queue
 *  常用LinkList创建Queue，实际上，LinkList既实现了List接口
 *  又实现了Queue接口。
 *  在使用时，如果把它当作List，就获取List的引用
 *  如果把它当作Queue，就获取Queue的引用
 *  始终按照面向抽象编程的原则编写代码，可以大大提高代码的质量。
 */
// 常用方法
//                          throw Exception	        返回false或null
//    添加元素到队尾	        add(E e)	            boolean offer(E e)
//    取队首元素并删除	        E remove()	            E poll()
//    取队首元素但不删除	    E element()	            E peek()
public class Part9_Queue {
    // 使用Queue
    public static void main(String[] args) {
        Queue<String> q = new LinkedList<>();

        // add和offer对比
        try{
            q.add("apple");
            System.out.println("添加成功");
        } catch (Exception e){
            System.out.println("添加失败");
        }
        if(q.offer("apple")){
            System.out.println("添加成功");
        }else {
            System.out.println("添加失败");
        }

        // remove和poll对比
        try {
            q.remove("apple");
            System.out.println("获取成功");
        } catch (IllegalStateException e){
            System.out.println("获取失败");
        }
        String s = q.poll();
        if (s != null){
            System.out.println("获取成功");
        }else {
            // 注意：不要把null添加到队列中，否则poll()方法返回null时，很难确定是取到了null元素还是队列为空。
            System.out.println("获取失败");
        }

    }
}
