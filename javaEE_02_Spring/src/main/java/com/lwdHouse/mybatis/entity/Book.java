package com.lwdHouse.mybatis.entity;

public class Book extends AbstractEntity{
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                "} " + super.toString();
    }
}
