package com.lwdHouse;


import org.junit.Test;

/**
 * 线程不安全模拟
 */
public class AppTest2 {

    @Test
    public void test01() throws InterruptedException {
        AddThread t1 = new AddThread();
        DecThread t2 = new DecThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("cnt="+Counter.cnt);
    }
}

class Counter{
    public static int cnt = 0;
}

class AddThread extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++){
            Counter.cnt++;
        }
    }
}

class DecThread extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++){
            Counter.cnt--;
        }
    }
}