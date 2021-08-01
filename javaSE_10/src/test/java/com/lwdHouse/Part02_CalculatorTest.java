package com.lwdHouse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 使用Fixture
 *
 * @BeforeEach: 在每个@Test方法之前执行
 * @AferEach: 在每个@Test方法之后执行
 * @BeforeAll: 在这个类测试前执行    只能修饰public static的方法
 * @AfterAll: 在这个类测试后执行     只能修饰public static的方法
 *
 * 每次运行一个@Test方法前，JUnit首先创建一个XxxTest实例，
 * 因此，每个@Test方法内部的成员变量都是独立的，不能也无法把成员变量的状态从一个@Test方法带到另一个@Test方法
 *
 * invokeBeforeAll(CalculatorTest.class);
 * for (Method testMethod : findTestMethods(CalculatorTest.class)) {
 *     var test = new CalculatorTest(); // 创建Test实例
 *     invokeBeforeEach(test);
 *         invokeTestMethod(test, testMethod);
 *     invokeAfterEach(test);
 * }
 * invokeAfterAll(CalculatorTest.class);
 */
public class Part02_CalculatorTest {

    Calculator calculator;

    @BeforeEach
    public void test01(){
        this.calculator = new Calculator();
    }

    @AfterEach
    public void test02() {
        this.calculator = null;
    }

    @Test
    public void test03() {
        assertEquals(100, calculator.add(100));
        assertEquals(150, calculator.add(50));
        assertEquals(130, calculator.add(-20));
    }

    @Test
    public void test04() {
        assertEquals(-100, calculator.sub(100));
        assertEquals(-150, calculator.sub(50));
        assertEquals(-130, calculator.sub(-20));
    }
}
