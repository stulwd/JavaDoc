package com.lwdHouse;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 使用Deque：double ended queue(双端队列)
 *      实现类有 ArrayDeque 和 （相当于C++的Deque）
 *             LinkedList（常用）
 * 实际上LinkedList实现了Deque接口，而Deque接口继承了Queue接口
 */
//  将元素添加到队尾或队首：      addLast()/offerLast()/addFirst()/offerFirst()；
//  从队首／队尾获取元素并删除：   removeFirst()/pollFirst()/removeLast()/pollLast()；
//  从队首／队尾获取元素但不删除： getFirst()/peekFirst()/getLast()/peekLast()；

public class Part11_Dequeue {
    public static void main(String[] args) {
//        Deque<String> deque2 = new ArrayDeque<>();
        Deque<String> deque = new LinkedList<>();
        deque.offerLast("A");
        deque.offerLast("B");
        deque.offerFirst("C");
        System.out.println(deque.pollFirst());      // C
        System.out.println(deque.pollLast());       // B
        System.out.println(deque.pollFirst());      // A
        System.out.println(deque.pollFirst());      // null
    }
}
