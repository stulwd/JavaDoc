package com.lwdHouse.mybatis.mapper;


import com.lwdHouse.mybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getById(@Param("id") Long id);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("SELECT * FROM users LIMIT #{offset}, #{maxResults}")
    List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO users (name, email, password, createdAt) VALUES (#{user.name}, #{user.email}, #{user.password}, #{user.createdAt})")
    void insert(@Param("user") User user);

    @Update("UPDATE users SET name = #{user.name}, email = #{user.email}, password = #{user.password} WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
}
