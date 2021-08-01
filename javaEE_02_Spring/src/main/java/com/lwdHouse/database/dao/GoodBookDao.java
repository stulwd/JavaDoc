package com.lwdHouse.database.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class GoodBookDao extends AbstractGeneric<Book> {
    // 已经有了如下方法：
    // Book getNyId(long id)
    // List<Book> getAll(int pageIndex)
    // deleteById(long id)
}


class Book{
    String name;
    long id;
    String type;
}