package com.lwdHouse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Part08_05_PostMapping {
    String path();
}
