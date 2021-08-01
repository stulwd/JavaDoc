package com.lwdHouse;


import org.junit.Test;

public class AppTest3 {
    /**
     * 线程同步
     * @throws InterruptedException
     */
    @Test
    public void test01() throws InterruptedException {
        AddThread1 t1 = new AddThread1();
        DecThread1 t2 = new DecThread1();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("cnt="+Counter.cnt);
    }

    /**
     * 使用线程安全类
     * @throws InterruptedException
     */
    @Test
    public void test02() throws InterruptedException {
        SafeCounter sc = new SafeCounter();
        Thread t1 = new Thread(() -> {
            sc.add2(5);
        });
        Thread t2 = new Thread(() -> {
            sc.desc(5);
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("getCount: "+sc.getCount());
    }

    /**
     * 使用可重入锁
     */
    @Test
    public void test03(){
        ReEnterCounter rc = new ReEnterCounter();
        rc.add(-5);
        System.out.println("getCount: "+ rc.getCount());
    }

}

class Counter1{
    public static Object lock = new Object();
    public static int cnt = 0;
}

class AddThread1 extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++){
            synchronized(Counter1.lock){
                Counter.cnt++;
            }
        }
    }
}

class DecThread1 extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++){
            synchronized(Counter1.lock){
                Counter.cnt--;
            }
        }
    }
}

/**
 * 线程安全类
 */
class SafeCounter{
    public int count = 0;
    public void add(int n){
        synchronized(this){
            count += n;
        }
    }
    // 如果我们锁住的是this，等效于如下
    public synchronized void add2(int n){
        count += n;
    }
    // 如果一个方法是静态的，用synchronized修饰，锁住的就是class对象
    // 例如 public static synchronized void function(){...}
    // 等效于 public static void function(){ synchronized(SafeCounter.class){...} }

    public void desc(int n){
        synchronized(this){
            count -= n;
        }
    }
    public int getCount(){
        return count;
    }
}

/**
 * 可重入锁类，默认可重入
 */
class ReEnterCounter{
    public int count = 0;
    public synchronized void add(int n){
        if(n < 0){
            // 同一个线程已经获取了锁后，还可以接着获取
            desc(-n);
        }else {
            count += n;
        }
    }
    public synchronized void desc(int n){
            count -= n;
    }
    public int getCount(){
        return count;
    }
}
