package com.lwdHouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 使用迭代器：
 *   自建一个ReverseList继承自ArrayList, 重写iterator()方法, 实现反转遍历
 */
public class Part13_Iterator {
    public static void main(String[] args) {
        List<String> list = new ReverseList<>();
        list.add("abc");
        list.add("def");
        list.add("ghi");
        for (String s : list) {
            System.out.println(s);
        }
        // 打印ghi def abc
    }
}

class ReverseList<T> extends ArrayList<T>{
    @Override
    public Iterator<T> iterator() {
        return new ReverseIterator();
    }

    private class ReverseIterator implements Iterator<T>{
        private int cur;

        public ReverseIterator() {
            cur = ReverseList.this.size();
        }

        @Override
        public boolean hasNext() {
            return cur != 0;
        }
        @Override
        public T next() {
            cur--;
            return ReverseList.this.get(cur);
        }
    }
}
