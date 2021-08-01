package com.lwdHouse;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 使用线程池
 */
public class AppTest10 {
    /**
     * newFixedThreadPool
     * 实现：
     *     public static ExecutorService newFixedThreadPool(int nThreads) {
     *         return new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     *     }
     * @throws InterruptedException
     */
    @Test
    public void test01() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("" + i));
        }
        Thread.sleep(3000);
        // 关闭线程池
        es.shutdown();
    }

    /**
     * newCachedThreadPool
     * 实现：
     *     public static ExecutorService newCachedThreadPool() {
     *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     *     }
     * @throws InterruptedException
     */
    @Test
    public void test02() throws InterruptedException {
        // 这个线程池的实现会根据任务数量动态调整线程池的大小，所以6个任务可一次性全部同时执行
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("" + i));
        }
        Thread.sleep(3000);
        // 关闭线程池
        es.shutdown();
    }

    /**
     * 如果我们想把线程池的大小限制在4～10个之间动态调整怎么办？
     */
    @Test
    public void test03() throws InterruptedException {
        int min = 4;
        int max = 10;
        ExecutorService es = new ThreadPoolExecutor(min,
                                                    max,
                                       60L,
                                                    TimeUnit.SECONDS,
                                                    new SynchronousQueue<Runnable>());
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("" + i));
        }
        Thread.sleep(3000);
        // 关闭线程池
        es.shutdown();
    }

    /**
     * 定时任务线程池 ScheduledThreadPool
     */
    @Test
    public void test04(){
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
        // 1秒后执行一次性任务:
        ses.schedule(new Task("one-time"), 1, TimeUnit.SECONDS);
        // 2秒后开始执行定时任务，每3秒执行一次
        ses.scheduleAtFixedRate(new Task("fixed-rate"), 2, 3, TimeUnit.SECONDS);
        // 以3秒为固定的间隔执行
        ses.scheduleWithFixedDelay(new Task("fixed-delay"), 2, 3, TimeUnit.SECONDS);




    }
}

class Task implements Runnable{
    private final String name;
    public Task(String name) {
        this.name = name;
    }
    @Override
    public void run() {
        System.out.println("start task " + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end Task " + name);
    }
}
