package com.lwdHouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class GenericType01
{
    public static void main( String[] args )
    {
        // 如果不定义泛型类型，就是Object，需要强转
        List list = new ArrayList();
        list.add("Hello");
        list.add("World");
        String first = (String) list.get(0);
        String second = (String) list.get(1);

        // 泛型接口的使用
        Person[] ps = new Person[]{
                new Person("Bob", 61),
                new Person("Alice", 88),
                new Person("Lily", 75)
        };
        // Arrays.sort接受的数组类型必须实现Comparable接口
        Arrays.sort(ps);
        System.out.println(Arrays.toString(ps));

    }


}


// 泛型接口
// Comparable就是一个泛型接口
// 用Person来实现这个接口
class Person implements Comparable<Person>{
    String name;
    int score;

    public Person(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(Person other) {
        // 字符串已经实现了Comparale接口，可以调用compareTo方法
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}

// 编写泛型
class Pair<T>{
    private T first;
    private T last;
    public Pair(T first, T last){
        this.first = first;
        this.last = last;
    }

    public T getFirst() {
        return first;
    }

    public T getLast(){
        return last;
    }

    // 对于静态方法，泛型要这样指定，这个<T>和P上面Pair<T>类型的<T>已经没有任何关系了。
    // 所以可以把T换位K： public static <K> Pair<K> create(K first, K last){return new Pair<K>(first, last);}
    public static <T> Pair<T> create(T first, T last){
        return new Pair<T>(first, last);
    }


}


/**
 * 擦拭法
 */
// 编译器把类型<T>转为Object
// 编译器根据<T>实现安全的强制转型
// 例如：
//
// private T first;
// public T getFirst(){
//     return first;
// }
// String first = p.getFirst();
//
// 编译完成后的实际代码是
// private Object first;
// public Object getFirst(){
//     return first;
// }
// String first = (String) p.getFirst();
//
/**
 * 擦拭法带来的局限
 */
// 1. <T>不能是基本类型，例如int，因为实际类型是Object，Object类型无法持有基本类型
// 2. 无法取得带泛型的Class
//        Pair<String> p1 = new Pair<>("Hello", "world");
//        Pair<Integer> p2 = new Pair<>(123, 456);
//        Class c1 = p1.getClass();
//        Class c2 = p2.getClass();
//        System.out.println(c1==c2);   // true
//        返回的c1和c2都是Pair<Object>.class
// 3. 无法判断带泛型的类型
//        Pair<Integer> p = new Pair<>(123, 456);
//        if (p instanceof Pair<String>) {} 会发生编译错误
// 4. 不能实例化T类型
//        public Pair() {
//            // Compile error:
//            first = new T();
//            last = new T();
//        }
//    解决办法：借助额外的Class<T>参数,通过反射来实例化T类型
//        public Pair(Class<T> clazz) {
//            first = clazz.newInstance();
//            last = clazz.newInstance();
//        }
//    解释：Class<T> clazz 被编译后为 Class<Object> clazz
//         first = clazz.newInstance(); 被编译后为 first = (T) clazz.newInstance();
//    使用的时候，也必须传入Class<T>, 例如Pair<String> pair = new Pair<>(String.class);
//    因为传入了Class<String>的实例，所以我们借助String.class就可以实例化String类型。
// 5. 不恰当的覆写方法
//        public class Pair<T> {
//            public boolean equals(T t) {
//                return this == t;
//            }
//        }
//    定义的equals(T t)方法实际上会被擦拭成equals(Object t)，而这个方法是继承自Object的
//    编译器会阻止一个实际上会变成覆写的泛型方法定义
//    解决办法：
//        换个方法名：
//        public boolean same(T t) {
//            return this == t;
//        }