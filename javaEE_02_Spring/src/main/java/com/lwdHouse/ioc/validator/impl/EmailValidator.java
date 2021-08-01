package com.lwdHouse.ioc.validator.impl;

import com.lwdHouse.ioc.validator.Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)   // 因为这写Component要被装配到bean中，所以可以用Order来指定装配顺序
public class EmailValidator implements Validator {
    @Override
    public void validate(String email, String password, String name) {
        if (!email.matches("^[a-z0-9]+\\@[a-z0-9]+\\.[a-z]{2,10}$")){
            throw new IllegalArgumentException("invalid email: "+email);
        }
    }
}
