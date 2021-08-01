package com.lwdHouse;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 使用Atomic: 所有的操作都是原子的
 */
public class AppTest9 {
}

class IdGenerator{
    AtomicLong var = new AtomicLong(0);

//    在高度竞争的情况下，还可以使用Java 8提供的LongAdder和LongAccumulator
//    LongAdder var1 = new LongAdder();

    public long getNextId(){
        return var.incrementAndGet();
    }
}

