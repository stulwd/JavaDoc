package com.lwdHouse;

/**
 * 单元测试
 */
public class Factorial {
    public static long fact(int n) {
        if (n < 0){
            throw new IllegalArgumentException("不能计算小于0的数的阶乘");
        }
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }
}
