package com.lwdHouse;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.locks.*;

/**
 * 读写锁：ReentrantReadWriteLock: 允许多线程同时读
 */
public class AppTest6 {
    @Test
    public void test01(){
    }
}

// 前面讲到的ReentrantLock保证了只有一个线程可以执行临界区代码：
class Counter2{
    private final Lock lock = new ReentrantLock();
    private int[] counts = new int[10];

    public void inc(int index){
        lock.lock();
        try{
            counts[index] += 1;
        }finally {
            lock.unlock();
        }
    }
    public int[] get(){
        lock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        }finally {
            lock.unlock();
        }
    }
}
// 上面代码，不能使多线程同时读，影响效率
// 使用读写锁改进
// ReadWriteLock只允许一个线程写入,
// 允许多个线程在没有写入时同时读取,
// 适合读多写少的场景
class Counter3{
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock rlock = rwLock.readLock();
    private final Lock wlock = rwLock.writeLock();
    private int[] counts = new int[10];

    public void inc(int index){
        wlock.lock();
        try{
            counts[index] += 1;
        }finally {
            wlock.unlock();
        }
    }
    public int[] get(){
        rlock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        }finally {
            rlock.unlock();
        }
    }
}
