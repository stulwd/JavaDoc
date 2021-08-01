package com.lwdHouse;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Part01_FactorialTest {

    @Test
    void TestFact() {
        assertEquals(1, Factorial.fact(1));
        assertEquals(2, Factorial.fact(2));
        assertEquals(3628800, Factorial.fact(10));
        assertEquals(2432902008176640000L, Factorial.fact(20));
        // 1-0.9为0.0999999...
        // 可以设置误差范围内即正确
        assertEquals(0.1, Math.abs(1 - 0.9), 0.000001);

        assertTrue(1==1);
        assertFalse(1 != 1);
        assertNotNull(new Object());
        assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});
    }
}
