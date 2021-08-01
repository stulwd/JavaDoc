package com.lwdHouse;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 优先队列：PriorityQueue
 *         优先队列是用堆实现的
 */
public class Part10_PriorityQueue {
    public static void main(String[] args) {
        // 使用priorityQueue
        Queue<String> pq = new PriorityQueue<>();
        pq.offer("apple");
        pq.offer("pear");
        pq.offer("banana");
        System.out.println(pq.poll());  // apple
        System.out.println(pq.poll());  // banana
        System.out.println(pq.poll());  // pear
        // 入队列乱序，出队列按照优先级
        // PriorityQueue要求放入的元素必须实现Comparable接口
        // 如果没有Comparable接口，就提供一个Comparator

        Queue<User> UserPq = new PriorityQueue<>(new UserComparator());
        UserPq.offer(new User("Bob", "A1"));
        UserPq.offer(new User("Alice", "A2"));
        UserPq.offer(new User("Boss", "V1"));
        System.out.println(UserPq.poll());
        System.out.println(UserPq.poll());
        System.out.println(UserPq.poll());

    }
}

class User{
    String name;
    String number;

    public User(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}

class UserComparator implements Comparator<User>{
    @Override
    public int compare(User u1, User u2) {
        if (u1.number.charAt(0) == u2.number.charAt(0)){
            return u1.number.compareTo(u2.number);
        }else{
            if (u1.number.charAt(0) == 'V'){
                // v1的优先级最高
                return -1;
            }else {
                return 1;
            }
        }

    }
}
