package com.lwdHouse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 异常测试
 */
public class Part03_ExceptionTest {

    @Test
    public void test01() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Factorial.fact(-1);
            }
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Factorial.fact(-2);
        });
    }
}
