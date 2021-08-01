package com.lwdHouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 编写equals()和 hashCode 方法
 * 注意：本节中以Person作为key
 */
// HashMap之所以能根据key直接拿到value，
// 原因是它内部通过空间换时间的方法，
// 用一个大数组存储所有value，
// 并根据key直接计算出value应该存储在哪个索引：
// 通过key计算索引的方式就是调用key对象的hashCode()方法，
// 它返回一个int整数。HashMap正是通过这个方法直接定位key对应的value的索引，继而直接返回value。
//        ┌───┐
//      0 │   │
//        ├───┤
//      1 │ ●─┼───> Person("Xiao Ming")
//        ├───┤
//      2 │   │
//        ├───┤
//      3 │   │
//        ├───┤
//      4 │   │
//        ├───┤
//      5 │ ●─┼───> Person("Xiao Hong")
//        ├───┤
//      6 │ ●─┼───> Person("Xiao Jun")
//        ├───┤
//      7 │   │
//        └───┘
public class Part4_Equals_HashCode {
    public static void main(String[] args) {
        Map<Person1, String> map = new HashMap<>();
        map.put(new Person1("张", "3", 12), "zs");
        map.put(new Person1("李", "4", 12), "ls");
        map.put(new Person1("王", "5", 12), "ww");
        String target = map.get(new Person1("张", "3", 12));
        System.out.println(target);
        // map.keySet()或者map.entrySet()返回的set是都是可迭代对象
        // 包含了一个Iterator对象，里面的next()方法都是调用的当前Node的next()方法
        // 看来在当时put()一个node时候，也是以链表来存储的
        for (Person1 p : map.keySet()) {
            System.out.println(map.get(p));
        }
    }
}

class Person1{
    private String firstName;
    private String lastName;
    private int age;

    public Person1(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * 覆写hashcode()
     * 作用：map通过key计算索引的方式就是调用key对象的hashCode()方法
     * @return
     */
    @Override
    public int hashCode() {
        int h = 0;
//        String类型已经正确实现了hashCode()方法
        h = 31*h + firstName.hashCode();
//        h = 31*h + lastName.hashCode();
//        h = 31*h + age;

//        上面代码会有问题，如果firstName或者lastName为null，则会报空指针错误
//        所以一般直接调用Objects.hash()来计算
        h = Objects.hash(firstName, lastName, age);
        return h;
    }

    /**
     * 覆写equals()
     * 作用：在HashMap的数组中，每一元素实际存储的不是一个Value实例，而是一个List<Map.Entry<key, value>>
     *     在存放时，如果前后两个不同的key算得的hashcode一样，那么就会把这两个Map.Entry<key, value>都放在这个List中
     *     遍历这个List，并找到一个Entry，确定这个Entry就用到了key的equals方法
     */
    @Override
    public boolean equals(Object obj) {
        Person1 p = (Person1) obj;
        return Objects.equals(this.firstName, p.getFirstName()) &&
                Objects.equals(this.lastName, p.getLastName()) &&
                this.age == p.getAge();
    }
}
















