package com.lwdHouse.database.dao;

import com.lwdHouse.database.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class GoodUserDao extends AbstractGeneric<User> {
    // 已经有了如下方法：
    // User getNyId(long id)
    // List<User> getAll(int pageIndex)
    // deleteById(long id)
}
