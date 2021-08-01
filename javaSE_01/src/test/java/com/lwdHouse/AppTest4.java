package com.lwdHouse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AppTest4 {

    /**
     * 使用wait() 和 notify()
     * @throws InterruptedException
     */
    @Test
    public void test01() throws InterruptedException {
        TaskQueue2 q = new TaskQueue2();
        var ts = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++){
            int finalI = i;
            Thread t = new Thread(){
                private int num = finalI;
                public void run(){
                    try {
                        String task = q.getTask();
                        System.out.println("thread " + num + ", execute task :" + task);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            };
            ts.add(t);
        }
        Thread add = new Thread(){
            public void run(){
                for (int i = 0; i < 10; i++){
                    System.out.println("add task "+ i);
                    q.addTask("task "+ i);
                }
            }
        };
        add.start();
        add.join();
        for (var t : ts) {
            t.start();
        }
        Thread.sleep(100);
        for (var t : ts) {
            t.interrupt();
        }

    }
}


//  这个TaskQueue是有问题的：
//  while()循环永远不会退出。因为线程在执行while()循环时，已经在getTask()入口获取了this锁，其他线程根本无法调用addTask()
class TaskQueue{
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String task){
        this.queue.add(task);
    }

    public synchronized String getTask(){
        while(this.queue.isEmpty()){
        }
        return this.queue.remove();
    }
}


// 改造后的版本
class TaskQueue2{
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String task){
        this.queue.add(task);
        this.notifyAll();
    }

    public synchronized String getTask() throws InterruptedException {
        while(this.queue.isEmpty()){
            this.wait();
        }
        return this.queue.remove();
    }
}


