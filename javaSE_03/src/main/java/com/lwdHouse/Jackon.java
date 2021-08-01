package com.lwdHouse;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * 使用Jackon解析xml
 *   需要这两个依赖：
 *      com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.10.1
 *      org.codehaus.woodstox:woodstox-core-asl:4.4.1
 */
public class Jackon {
    public static void main(String[] args) throws IOException {
        InputStream input = Jackon.class.getResourceAsStream("/book.xml");
        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper mapper = new XmlMapper(module);
        Book book = mapper.readValue(input, Book.class);
        System.out.println("book: " + book);
    }
}

class Book{
    public long id;
    public String name;
    public String author;
    public String isbn;
    public List<String> tags;
    public String pubDate;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", tags=" + tags +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}
