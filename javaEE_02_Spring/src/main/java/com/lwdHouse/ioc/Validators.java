package com.lwdHouse.ioc;


import com.lwdHouse.ioc.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 注入一个List
 *     好处：这样一来，我们每新增一个Validator类型，就自动被Spring装配到Validators中了
 * 注入List的时候，List是有序的，要指定List中Bean的顺序，可以给Bean加上@Order注解：
 */
@Component
public class Validators {
    @Autowired
    List<Validator> validators;

    public void validate(String email, String password, String name){
        for (Validator v : validators) {
            v.validate(email, password, name);
        }
    }
}
