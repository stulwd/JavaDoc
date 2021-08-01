package com.lwdHouse;

public class Calculator {
    private long n = 0;
    public long add(int x){
        n += x;
        return n;
    }
    public long sub(int x){
        n -= x;
        return n;
    }
}
