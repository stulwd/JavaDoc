package com.lwdHouse;

import java.util.List;
import java.util.Objects;

/**
 * 编写equals方法
 *  List的contains()、indexOf()这些方法，都调用了equals()方法进行计算
 */
public class Part2_Equals {
    public static void main(String[] args) {
        List<Person> list = List.of(
                new Person("xiao ming"),
                new Person("xiao hong"),
                new Person("Bob"));
        // 如果Person类没有覆写equals方法，那么这个结果是false，
        // 原因：AbstractImmutableList.contains方法将会调用AbstractImmutableList.indexOf()方法, indexOf
        //      方法会调用Object.equals方法，Object.equals方法里是用 == 判断是否相等的（是否为同一个对象）
        //      换句话说，这个判断相等的条件太严格了，要根据我们认为两者相等的条件来重写equals
        // 如果覆写了equals方法，将会用Person的equals方法做判断
        System.out.println(list.contains(new Person("Bob")));

    }
}

class Person{
    private String name;
    private int age;

    public Person(String name) {
        this.name = name;
    }

    // 覆写equals方法
    public boolean equals01(Object obj) {
        if (obj instanceof Person){
            Person p = (Person) obj;
            boolean nameEquals = false;
            // 这里要判断null是因为，如果this.name为null，
            // 下面的this.name.equals() 会报空指针错误，所以要排除这种情况
            if (this.name == null && p.name == null){
                nameEquals = true;
            }
            if (this.name != null){
                // 这里String已经实现了equals方法了，直接调用就行
                nameEquals = this.name.equals(p.name);
            }
//            对引用类型用Objects.equals()比较，对基本类型直接用==比较
            return nameEquals && this.age == p.age;
        }
        return false;
    }

    // 如果Person字段有好几个字段，要判断的话就太复杂了，
    // 可以直接使用Objects.equals()静态方法
    public boolean equals(Object obj) {
        if (obj instanceof Person){
            return Objects.equals(this.name, ((Person) obj).name) && this.age == ((Person) obj).age;
        }
        return false;
    }
}
