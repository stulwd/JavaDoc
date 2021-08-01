package com.lwdHouse;

import org.junit.Test;

import java.util.concurrent.locks.StampedLock;

/**
 * 乐观锁：StampedLock
 * 读的过程中也允许获取写锁后写入
 * StampedLock是不可重入锁
 */
public class AppTest7 {
    @Test
    public void test01(){

    }
}

class Point{
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY){
        long stamp = stampedLock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        }finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead();   // 获得一个乐观读锁
        double currentX = x;
        double currentY = y;
        if(!stampedLock.validate(stamp)){
            stamp = stampedLock.readLock(); // 获得一个悲观读锁
            try{
                currentX = x;
                currentY = y;
            }finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}