package com.lwdHouse;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * fork/join
 * Fork/Join是一种基于“分治”的算法：通过分解任务，并行执行，最后合并结果得到最终结果。
 */
public class AppTest13 {
    @Test
    public void test01(){
        long[] array = new long[2000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = random();
            expectedSum += array[i];
        }
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length-1);
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in" + (endTime - startTime)  + " ms.");
    }

    static Random random = new Random(0);
    static long random(){
        return random.nextInt(10000);
    };

}

class SumTask extends RecursiveTask<Long>{

    static final int THRESHOLD = 500;
    long[] array;
    int start;
    int end;

    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long res = 0;
        if (end - start <= THRESHOLD) {
            for (int i = start; i <= end; i++) {
                res += array[i];
            }
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return res;
        }

        int middle = (end + start) / 2;
        SumTask task1 = new SumTask(array, start, middle);
        SumTask task2 = new SumTask(array, middle+1, end);
        invokeAll(task1, task2);
        Long res1 = task1.join();
        Long res2 = task2.join();
        Long result = res1 + res2;
        return result;
    }
}