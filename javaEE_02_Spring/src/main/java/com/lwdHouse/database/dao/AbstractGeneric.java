package com.lwdHouse.database.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractGeneric<T> extends JdbcDaoSupport {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String table;
    private Class<T> entityClass;
    private RowMapper<T> rowMapper;

    public AbstractGeneric(){
        this.entityClass = getParameterizedType();
        this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
        this.table = entityClass.getSimpleName().toLowerCase() + "s";
    }

    @PostConstruct
    public void init() {
        super.setJdbcTemplate(jdbcTemplate);
    }

    public T getById(long id){
        return getJdbcTemplate().queryForObject("SELECT * FROM " + table + " WHERE id = ?", this.rowMapper, id);
    }

    public List<T> getAll(int pageIndex){
        int limit = 100;
        int offset = 100 * (pageIndex - 1);
        return getJdbcTemplate().query("SELECT * FROM " + table + " LIMIT ? OFFSET ?", new Object[]{limit, offset}, this.rowMapper);
    }

    public void deleteById(long id){
        getJdbcTemplate().update("DELETE FROM " + table + " WHERE id = ?", id);
    }

    public RowMapper<T> getRowMapper(){
        return this.rowMapper;
    }

    /**
     * 这里可以参考javaCE_06:泛型
     * @return
     */
    public Class<T> getParameterizedType(){
        // getGenericSuperclass方法返回的是Type类型，
        // getSuperclass返回的是Class类型
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)){
            throw new IllegalArgumentException("Class" + getClass().getName() + " dose not have parameterized type.");
        }

        ParameterizedType pt = (ParameterizedType) type;
        Type[] types = pt.getActualTypeArguments();
        if (types.length != 1){
            throw new IllegalArgumentException("Class" + getClass().getName() + " has more than 1 parameterized type.");
        }

        Type r = types[0];
        if (!(r instanceof Class<?>)){
            throw new IllegalArgumentException("Class" + getClass().getName() + "does not have parameterized type of class");
        }
        return (Class<T>) r;
    }

}
