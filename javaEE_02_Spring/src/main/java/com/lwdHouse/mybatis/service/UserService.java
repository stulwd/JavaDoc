package com.lwdHouse.mybatis.service;

import com.lwdHouse.mybatis.entity.User;
import com.lwdHouse.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    UserMapper userMapper;  // [注意]: 这里注入的不是编写的UserMapper，因为编写的是一个UserMapper接口，而是mybatis为我们创建的实现类

    public User getUserById(Long id){
        User user = userMapper.getById(id);
        if (user == null){
            throw new RuntimeException("User not found by id.");
        }
        return user;
    }

    public User fetchUserByEmail(String email){
        return userMapper.getByEmail(email);
    }

    public User getUserByEmail(String email){
        User user = fetchUserByEmail(email);
        if (user == null){
            throw new RuntimeException("User not found by email.");
        }
        return user;
    }

    public List<User> getUsers(int pageIndex){
        int pageSize = 200;
        return userMapper.getAll((pageIndex - 1)*pageSize, pageSize);
    }

    public User login(String email, String password){
        User user = fetchUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)){
            throw new RuntimeException("login failed.");
        }
        return user;
    }

    public User register(String email, String name, String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        userMapper.insert(user);
        // 不知道这里返回的user带不带id
        return user;
    }

    public void updateUser(Long id, String name){
        User user = userMapper.getById(id);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        userMapper.update(user);
    }

    public void deleteUser(Long id){
        userMapper.deleteById(id);
    }

}
