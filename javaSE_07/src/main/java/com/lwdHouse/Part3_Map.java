package com.lwdHouse;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用Map
 */
public class Part3_Map {
    public static void main(String[] args) {
        Student s = new Student("xiao ming", 99);
        Map<String, Student> map = new HashMap<>();
        map.put("xiao ming", s);
        Student target = map.get("xiao ming");
        System.out.println(s == target);    // true
        System.out.println(target.score);   // 99
        Student another = map.get("Bob");
        System.out.println(another);        // 返回null
        map.put("xiao hong", s);
        System.out.println(map.get("xiao hong") == target);     // value可重复

        // 遍历map
        // 方法1：Map.keySet()遍历键集合，返回的是一个set，set实现了Iterable接口，所以可以使用foreach
        for (String name : map.keySet()) {
            System.out.println(name + ":" + map.get(name));
        }
        // 方法2：map.entrySet()，遍历条目集合
        // entry在这里表示“条目”的意思
        for (Map.Entry<String, Student> entry : map.entrySet()) {
            String key = entry.getKey();
            Student student = entry.getValue();
        }


    }
}

class Student{
    public String name;
    public int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}