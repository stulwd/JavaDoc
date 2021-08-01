package com.lwdHouse;

/**
 * extends通配符
 * extends通配符的作用：
 *      1. 解决实参和形参的T类型不一样（但为继承关系）的情况下的不能传参问题
 *      2. obj<? extends Integer> 表明了该方法内部只会读取obj的元素，
 *         不会修改obj的元素, 因为无法调用fun(obj<? extends Integer>),
 *         原因参考add1()内部为甚么不能调用set方法
 *      3. 使用extends限定T类
 *         如 public class Pair<T extends Number> { ... }
 *         我们只能定义 Pair<Number的子类> 这种Pair
 */
public class GenericType02 {
    public static void main(String[] args) {
//        背景：一般情况下，要是参数是泛型，实参和形参的<T>类型要完全一致，如
        Pair01<Integer> p = new Pair01<>(1, 2);
//        下面代码会报错，实参类型Pair01<Integer>和形参Pair01<Number>不兼容
//        int n = add(p);
//        需要使用extends上界通配符改写形参，表示Number的子类都可以
        int n = add1(p);
    }

//    public static int add(Pair01<Number> pair01){
    public static int add1(Pair01<? extends Number> pair01){
        Number first = pair01.getFirst();
        Number last = pair01.getLast();
//        最终返回值接受实际返回值：最终返回类型要 >= 所有可能实际返回类型(最大为上界Number), 所以报错
        Integer res = pair01.getLast();
//        形参接受实参：所有可能形参要 >= 实参，因为行参没有下界，所以传任何实参都不行，所以这种xxx(T)的方法在
//        这里就不能使用
        pair01.setFirst(new Integer(first.intValue()+100));
        return first.intValue() + last.intValue();
    }

}

class Pair01<T>{
    private T first;
    private T last;
    public Pair01(T first, T last){
        first = first;
        last = last;
    }

    public T getFirst(){
        return first;
    }

    public T getLast() {
        return last;
    }

    public void setFirst(T first){
        first = first;
    }

    public void setLast(T last){
        last = last;
    }
}