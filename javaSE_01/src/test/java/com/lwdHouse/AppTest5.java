package com.lwdHouse;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AppTest5 {
    @Test
    public void test01(){

    }
}
/**
 * 使用ReentrantLock锁
 */
class ReentrantCounter{
    private final Lock lock = new ReentrantLock();
    private int count;

    public void add(int n){
        lock.lock();    // 锁上（ReentrantLock是可重入锁，即同一个线程可以多次获取锁
        try{
            count += n;
        } finally {
            lock.unlock();  // 解锁
        }
    }

    // ReentrantLock可以尝试获取锁，获取不到不会无限等待
    public void add2(int n) throws InterruptedException {
        if (lock.tryLock(1, TimeUnit.SECONDS)){
            try{
                count += n;
            } finally {
                lock.unlock();  // 解锁
            }
        }
    }
}

/**
 * 使用condition实现wait()和notify()功能
 * 联合使用ReentrantLock和Condition实现一个BlockingQueue
 * BlockingQueue就是当一个线程调用这个TaskQueue的getTask()方法时，
 * 该方法内部可能会让线程变成等待状态，直到队列条件满足不为空，线程被唤醒后，getTask()方法才会返回
 */

class TaskQueue3{
    public final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    Queue<String> queue = new LinkedList<>();

    void addTask(String task){
        lock.lock();
        try {
            queue.add(task);
            condition.signalAll();  // 相当于notifyall
        } finally {
            lock.unlock();
        }
    }

    public String getTask() throws InterruptedException {
        lock.lock();
        try {
            while(queue.isEmpty()){
                condition.await();      // 相当于await
//                condition.await(1, TimeUnit.SECONDS);
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }

    }
}