package com.lwdHouse;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 使用 Callable 代替 Runnable，可以返回结果
 */
public class AppTest11 {
    @Test
    public void test01() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        Future<String> future = es.submit(new Task2("task 1"));
        String res = future.get();  // 该方法可能阻塞
//        String res2 = future.get(1, TimeUnit.SECONDS);  // 最多等待1秒
//        future.isDone(); // 判断是否完成
//        future.cancel(false);   // 取消当前任务
    }
}

class Task2 implements Callable<String>{
    private final String name;
    public Task2(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        return "ok";
    }
}
