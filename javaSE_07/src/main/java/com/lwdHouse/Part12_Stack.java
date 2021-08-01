package com.lwdHouse;

import java.util.Deque;
import java.util.LinkedList;

/**
 * stack 栈
 * Java中没有对stack的实现
 * 是能用Deque来模拟一下，性能完全一样
 */
public class Part12_Stack {
    // 利用stack把给定的整数转换为十六进制
    public static void main(String[] args) {
        String hex = toHex(12500);
        if (hex.equalsIgnoreCase("30D4")) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }

    static String toHex(int n) {
        Deque<String> deque = new LinkedList<>();
        int remain;
        while ( n != 0 ){
            deque.addFirst(Integer.toHexString(n % 16));
            n /= 16;
        }
        StringBuilder sb = new StringBuilder();
        while (!deque.isEmpty()){
            sb.append(deque.pollFirst());
        }
        return sb.toString();
    }
}
