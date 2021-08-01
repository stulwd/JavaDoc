package com.lwdHouse;

import static java.lang.Thread.State.TERMINATED;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * 创建新线程
     * 方法一：
     */
    @Test
    public void test01(){
        Thread t = new MyThread();
        t.start();
    }
    /**
     * 创建新线程
     * 方法二：
     */
    @Test
    public void test02(){
        Thread t = new Thread(new MyRunnable());
        t.start();
    }
    /**
     * 创建新线程
     * 方法三：
     */
    @Test
    public void test03(){
        Thread t = new Thread(() -> {
            System.out.println("start new thread!");
        });
        t.start();
    }

    /**
     * sleep()
     */
    @Test
    public void test04(){
        System.out.println("main run...");
        Thread t = new Thread(){
            public void run(){
                System.out.println("thread run...");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread end...");
            }
        };
        t.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main end...");
    }

    /**
     * join()
     * 线程 state
     * @throws InterruptedException
     */
    @Test
    public void test05() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("start new thread!");
        });
        System.out.println("start");
        t.start();
        System.out.println("state:"+t.getState().toString());
        t.join();
        System.out.println("state:"+t.getState().toString());
        System.out.println("end");
    }

    /**
     * 中断线程：使用interrupt
     * @throws InterruptedException
     */
    @Test
    public void test06() throws InterruptedException {
        Thread t = new Thread(){
            public void run(){
                int n = 0;
                while (!isInterrupted())
                {
                    n++;
                    System.out.println(n + "hello!");
                }
            }
        };
        t.start();
        Thread.sleep(100);
        t.interrupt();
        t.join();
        System.out.println("end");

    }

    /**
     * 中断线程：调用interrupt方法，子线程在join和sleep时，会抛出interrupt异常
     * 对目标线程调用interrupt()方法可以请求中断一个线程，目标线程通过检测isInterrupted()标志获取自身是否已中断。
     * 如果目标线程处于等待状态，该线程会捕获到InterruptedException；
     * 目标线程检测到isInterrupted()为true或者捕获了InterruptedException都应该立刻结束自身线程；
     * @throws InterruptedException
     */
    @Test
    public void test07() throws InterruptedException {

        Thread MyThread = new Thread(){
            public void run(){
                Thread hello = new Thread(){
                    public void run(){
                        int n = 0;
                        while (true) {
                            n++;
                            System.out.println(n + " hello!");
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                System.out.println("hello interrupted!");
                                break;
                            }
                        }
                    }
                };
                hello.start(); // 启动hello线程
                try {
                    hello.join(); // 等待hello线程结束
                } catch (InterruptedException e) {
                    System.out.println("Mythread interrupted!");
                }
                // 此时hello正在sleep; 调用interrupt()会让sleep抛出interrupt异常
                hello.interrupt();
            }
        };
        MyThread.start();
        Thread.sleep(10);
        // 此时Mythread正在hello.join();调用interrupt()会让join其抛出interrupt异常
        MyThread.interrupt();
        MyThread.join();
        System.out.println("end");
    }

    /**
     * 中断线程：共享标志位
     * @throws InterruptedException
     */
    @Test
    public void test08() throws InterruptedException {
        MyThread2 MyThread = new MyThread2();
        MyThread.start();
        Thread.sleep(10);
        // 此时Mythread正在hello.join();调用interrupt()会让join其抛出interrupt异常
        MyThread.running = false;
        System.out.println("end");
    }

    /**
     * 守护线程
     * 在守护线程中，编写代码要注意：
     * 守护线程不能持有任何需要关闭的资源，例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
     */
    @Test
    public void test09() {
        Thread t = new Thread(){
            public void run(){
                while (true){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        // 设置为守护线程，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出，守护线程会继续执行
        t.setDaemon(true);
        t.start();
    }

}

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("start new thread!");;
    }
}

class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("start new thread!");
    }
}

class MyThread2 extends Thread {
    /**为什么要对线程间共享的变量用关键字volatile声明？这涉及到Java的内存模型。
     * 在Java虚拟机中，变量的值保存在主内存中，但是，当线程访问变量时，它会先获取一个副本，并保存在自己的工作内存中。
     * 如果线程修改了变量的值，虚拟机会在某个时刻把修改后的值回写到主内存，
     * 但是，这个时间是不确定的！
     * 这会导致如果一个线程更新了某个变量，另一个线程读取的值可能还是更新前的。
     * 例如，主内存的变量a = true，线程1执行a = false时，它在此刻仅仅是把变量a的副本变成了false，主内存的变量a还是true，
     * 在JVM把修改后的a回写到主内存之前，其他线程读取到的a的值仍然是true，这就造成了多线程之间共享的变量不一致。
     * 因此，volatile关键字的目的是告诉虚拟机：
     *  1. 每次访问变量时，总是获取主内存的最新值；
     *  2. 每次修改变量后，立刻回写到主内存。**/
    public volatile boolean running = true;
    @Override
    public void run() {
        int n = 0;
        while (running){
            n++;
            System.out.println(n + " hello");
        }
        System.out.println("end");
    }
}
