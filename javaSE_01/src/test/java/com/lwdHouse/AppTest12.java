package com.lwdHouse;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 使用CompletableFuture
 * 背景：使用Future获得异步执行结果时，要么调用阻塞方法get，要么轮询查看isDone是否为true，都不是很友好，因为主线程也会被迫等待，
 *      CompletableFuture可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法
 *
 * CompletableFuture的命名规则：
 *      xxx()：表示该方法将继续在已有的线程中执行； 如 thenAccept,exceptionally
 *      xxxAsync()：表示将异步在线程池中执行。 如 supplyAsync，thenApplyAsync
 */
public class AppTest12 {
    @Test
    public void test01() throws InterruptedException {
        // 创建异步执行任务
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(AppTest12::FetchPrice);
        // 执行成功
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 执行异常
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        // 主线程不要立刻关闭，否则CompletableFuture默认使用的线程会立刻关闭
        Thread.sleep(200);
    }

    /**
     * CompletableFuture可以串行执行
     * @return
     */
    @Test
    public void test02() throws InterruptedException {
       CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
           return QueryCode2("中国石油");
       });
       CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync((code) -> {
           return FetchPrice2(code);
       });
       cfFetch.thenAccept((result) -> {
           System.out.println("price: " + result);
       });
       Thread.sleep(2000);
    }

    /**
     *
     * CompletableFuture可以并行执行
     * ┌─────────────┐ ┌─────────────┐
     * │ Query Code  │ │ Query Code  │
     * │  from sina  │ │  from 163   │
     * └─────────────┘ └─────────────┘
     *        │               │
     *        └───────┬───────┘
     *                ▼
     *         ┌─────────────┐
     *         │    anyOf    │
     *         └─────────────┘
     *                │
     *        ┌───────┴────────┐
     *        ▼                ▼
     * ┌─────────────┐  ┌─────────────┐
     * │ Query Price │  │ Query Price │
     * │  from sina  │  │  from 163   │
     * └─────────────┘  └─────────────┘
     *        │                │
     *        └────────┬───────┘
     *                 ▼
     *          ┌─────────────┐
     *          │    anyOf    │
     *          └─────────────┘
     *                 │
     *                 ▼
     *          ┌─────────────┐
     *          │Display Price│
     *          └─────────────┘
     * @return
     */
    @Test
    public void test03() throws InterruptedException {
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return QueryCode3("中国石油", "www.sina.com");
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return QueryCode3("中国石油", "www.163.com");
        });
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);
        // 除了anyof, allOf()可以实现“所有CompletableFuture都必须成功”
        // CompletableFuture<Void> cfQuery2 = CompletableFuture.allOf(cfQueryFrom163, cfQueryFromSina);
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
            return FetchPrice3((String) code, "www.sina.com");
        });
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
            return FetchPrice3((String) code, "www.163.com");
        });
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFrom163, cfFetchFromSina);
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        Thread.sleep(2000);
    }


    static Double FetchPrice(){
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
        if(Math.random() < 0.3){
            throw new RuntimeException("fatch price failed!");
        }
        return 5 + Math.random() * 20;
    }

    static String QueryCode2(String name){
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
        return "601857";
    }

    static Double FetchPrice2(String code){
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
        if(Math.random() < 0.3){
            throw new RuntimeException("fatch price failed!");
        }
        return 5 + Math.random() * 20;
    }


    static String QueryCode3(String name, String Url){
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
        return "601857";
    }

    static Double FetchPrice3(String code, String Url){
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
        if(Math.random() < 0.3){
            throw new RuntimeException("fatch price failed!");
        }
        return 5 + Math.random() * 20;
    }
}
