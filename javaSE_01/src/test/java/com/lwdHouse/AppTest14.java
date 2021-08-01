package com.lwdHouse;

import org.junit.Test;

/**
 * 使用ThreadLocal
 * 实际上，可以把ThreadLocal看成一个全局Map<Thread, Object>：
 * 每个线程获取ThreadLocal变量时，总是使用Thread自身作为key：
 * 因此，ThreadLocal相当于给每个线程都开辟了一个独立的存储空间，各个线程的ThreadLocal关联的实例互不干扰
 */
public class AppTest14 {
    static ThreadLocal<User> threadLocalUser = new ThreadLocal<>();
    @Test
    public void test01(){
        try{
            User user = new User("lwd", "123");
            threadLocalUser.set(user);      // 给主线程设置user
            processUser();
        }finally {
            threadLocalUser.remove();
        }

        new Thread(){
            @Override
            public void run() {
                step1();      // 其他线程获取不到主线程的user
                try{
                    User user = new User("mts", "456"); // 给其他线程设置user
                    threadLocalUser.set(user);
                    processUser();
                }finally {
                    // ThreadLocal一定要在finally中清除
                    // 因为当前线程执行完相关代码后，很可能会被重新放入线程池中，
                    // 如果ThreadLocal没有被清除，该线程执行其他代码时，会把上一次的状态带进去
                    threadLocalUser.remove();
                }
            }
        }.start();

        // 为了保证能释放ThreadLocal关联的实例，我们可以通过AutoCloseable接口配合try (resource) {...}结构，
        // 让编译器自动为我们关闭,见下面UserContext类
        // 使用的时候，我们借助try (resource) {...}结构
        new Thread(){
            @Override
            public void run() {
                try(var ctx = new UserContext(new User("lwd", "666"))){
                    processUser2();
                } // 在此自动调用UserContext.close()方法释放ThreadLocal关联对象
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void processUser(){
        step1();
        step2();
        step3();
    }

    private void step1(){
        User user = threadLocalUser.get();
        System.out.println("step1: user = " + user);
    }

    private void step2(){
        User user = threadLocalUser.get();
        System.out.println("step2: user.name = " + user.getName());
    }

    private void step3(){
        User user = threadLocalUser.get();
        System.out.println("step3: user.id = " + user.getId());
    }

    private void processUser2(){
        User user = UserContext.currentUser();
        System.out.println("step1: user = " + user);
    }
}

class UserContext implements AutoCloseable{
    // 此成员是静态的, 表示所有的实例都共享同一个ThreadLocal
    static final ThreadLocal<User> ctx = new ThreadLocal<>();

    public UserContext(User user) {
        ctx.set(user);
    }

    public static User currentUser(){
        return ctx.get();
    }

    @Override
    public void close() throws Exception {
        ctx.remove();
    }
}

class User{
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}