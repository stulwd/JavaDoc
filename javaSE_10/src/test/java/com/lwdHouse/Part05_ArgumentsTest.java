package com.lwdHouse;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;

/**
 * 参数化测试
 */
public class Part05_ArgumentsTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 100})
    public void testAbs(int x){
        assertEquals(x, Math.abs(x));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -100})
    public void testAbsNegative(int x){
        assertEquals(-x, Math.abs(x));
    }

    @ParameterizedTest
    // 如果静态方法和测试方法同名，则不需要指定方法名，如果不一样，要使用@MethodSource("function name")指定
    @MethodSource
    public void testCaptialize(String input, String result){
        assertEquals(result, StringUtils.captialize(input));
    }

    static List<Arguments> testCaptialize(){
        return List.of(Arguments.of("abc", "Abc"),
                        Arguments.of("APPLE", "Apple"),
                        Arguments.of("gooD", "Good"));
    }

    @ParameterizedTest
    @CsvSource({"abc, Abc","APPLE, Apple","gooD, Good"})
    public void testCapitalizeUsingCsv(String input, String result){
        assertEquals(result, StringUtils.captialize(input));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/test-capitalize.csv"})
    public void testCapitalizeUsingCsvFile(String input, String result){
        assertEquals(result, StringUtils.captialize(input));
    }

}
