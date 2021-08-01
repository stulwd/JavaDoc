package com.lwdHouse.database.dao;


import com.lwdHouse.database.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 使用Spring提供的jdbcDao支持
 * dao是专门用来访问数据库的接口：Data Access Object
 * 这样就可以把业务层和数据访问层分开了
 */

@Component
@Transactional
public class UserDao extends AbstractDao {
    public User getById(long id){
        return getJdbcTemplate().queryForObject(
                "SELECT * FROM users WHERE id = ?",
//                new Object[] {id},
                new BeanPropertyRowMapper<>(User.class),
                id
        );
    }
}
