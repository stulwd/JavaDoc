package com.lwdHouse;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用Concurrent集合
 * 并发集合
 *   interface	non-thread-safe	            thread-safe
 *   List	    ArrayList	                CopyOnWriteArrayList
 *   Map	    HashMap	                    ConcurrentHashMap
 *   Set	    HashSet / TreeSet	        CopyOnWriteArraySet
 *   Queue	    ArrayDeque / LinkedList	    ArrayBlockingQueue / LinkedBlockingQueue
 *   Deque	    ArrayDeque / LinkedList	    LinkedBlockingDeque
 */
public class AppTest8 {
    @Test
    public void test01(){
        // 使用并发Map
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");
        map.put("D", "4");
        map.get("A");
    }

    @Test
    public void test02(){
        // 线程安全集合转换器
        Map unsafeMap = new HashMap();
        Map threadSafeMap = Collections.synchronizedMap(unsafeMap);
//        它实际上是用一个包装类包装了非线程安全的Map，然后对所有读写方法都用synchronized加锁，这样获得的线程安全集合的性能比java.util.concurrent集合要低很多，所以不推荐使用
    }
}
