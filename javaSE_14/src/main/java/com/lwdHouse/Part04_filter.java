package com.lwdHouse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 使用filter
 */
public class Part04_filter {
    public static void main(String[] args) {
        int[] ints = {1};
        boolean b = Arrays.stream(ints).anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 1;
            }
        });
        System.out.println(b);
        fun1();
    }

    public static void fun1() {
        IntStream.of(1,2,3,4,5,6,7,8,9)
                .filter(n -> n % 2 != 0)
                .forEach(System.out::println);
        IntStream.of(1,2,3)
                .filter(new IntPredicate() {
                    @Override
                    public boolean test(int value) {
                        return value % 2 != 0;
                    }
                });

        Stream.generate(new Supplier<LocalDate>() {
            LocalDate curDay = LocalDate.of(2021, 1, 1);
            @Override
            public LocalDate get() {
                curDay = curDay.plusDays(1);
                return curDay;
            }
        })
        .limit(365)
        .filter(localDate -> localDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                             localDate.getDayOfWeek() == DayOfWeek.SUNDAY)
        .forEach(System.out::println);
    }
}
