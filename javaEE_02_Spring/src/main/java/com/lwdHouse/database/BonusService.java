package com.lwdHouse.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BonusService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void addBonus(long userId, int bonus){
        if (1 != jdbcTemplate.update("INSERT INTO bonus (bonus, user_id) VALUES (?, ?)", bonus, userId)){
            throw new RuntimeException("add bonus failed");
        }
    }
}
