package com.lwdHouse.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;

/**
 * Spring 也提供了 JdbcDaoSupport，
 * JdbcDaoSupport维护了一个JdbcTemplate
 * dao可以通过getJdbcTemplate()方法获取JdbcTemplate，再操作数据库。
 * （其实JdbcDaoSupport做的工作很少，就是简单封装了一下dataSource和jdbcTemplate
 * ，用户还是需要获取jdbcTemplate来操作数据库）
 */
public abstract class AbstractDao extends JdbcDaoSupport {

    // JdbcDaoSupport需要设置jdbcTemplate才能正常工作，jdbcTemplate需要dataSource才能正常工作
    // 我们已经在AppConfig里创建好了dataSource和jdbcTemplate的bean，并已经把dataSource设置到了
    // jdbcTemplate，所以这里我们只需要沿用jdbcTemplate就行，装配一下，再init进行设置。
    // 补充：查阅JdbcDaoSupport的源码可以看到，也可以直接调用setDataSource()进行设置dataSource，jdbcTemplate就会自动创建
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init(){
        super.setJdbcTemplate(jdbcTemplate);
    }

}
