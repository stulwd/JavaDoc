package com.lwdHouse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Json数据格式
 * 常用于解析Json的第三方库有
 * Jackon、Gson、Fastjson
 * 本节我们继续使用Jackon解析Json
 * 需要的依赖：com.fasterxml.jackson.core:jackson-databind:2.10.0
 */
public class Json {
    public static void main(String[] args) throws IOException {
        InputStream input = Json.class.getResourceAsStream("/book.json");
//        ObjectMapper mapper = new ObjectMapper();
        // 注册310模块，支持LocalDate数据类型
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        // 关闭DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES功能
        // 使得解析时如果JavaBean不存在该属性时解析不会报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 把JSON解析为JavaBean的过程称为反序列化
        Book2 book = mapper.readValue(input, Book2.class);
        System.out.println("book: "+book);

        // 如果把JavaBean变为JSON，那就是序列化
        String json = mapper.writeValueAsString(book);
        System.out.println("序列化：" + json);

    }
}

class Book2{
    public long id;
    public String name;
    public Map<String, String> author;
    public String isbn;
    public List<String> tags;
    // 要把JSON的某些值解析为特定的Java对象，例如LocalDate，也是完全可以的
    // 只需要引入标准的JSR 310关于JavaTime的数据格式定义至Maven
    // 然后，在创建ObjectMapper时，注册一个新的JavaTimeModule
    // ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public LocalDate pubDate;
    // 对于对象类型，直接解析，肯定报错。这时，我们需要自定义一个numberDeserializer作为解析规则，用于解析含有非数字的字符串
    // 然后，使用注解标注
    // 类似的，自定义序列化时我们需要自定义一个numberSerializer，然后在Book类中标注@JsonSerialize(using = ...)即可
    @JsonDeserialize(using = numberDeserializer.class)
    @JsonSerialize(using = numberSerializer.class)
    public BigInteger number;

    @Override
    public String toString() {
        return "Book2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", isbn='" + isbn + '\'' +
                ", tags=" + tags +
                ", pubDate=" + pubDate +
                ", number=" + number +
                '}';
    }
}

class numberDeserializer extends JsonDeserializer<BigInteger>{
    @Override
    public BigInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String s = p.getValueAsString();
        if (s != null){
            try {
                return new BigInteger(s.replace("-", ""));
            } catch (NumberFormatException e){
                throw new JsonParseException(p, s, e);
            }
        }
        return null;
    }
}

class numberSerializer extends JsonSerializer<BigInteger> {
    @Override
    public void serialize(BigInteger bigInteger, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(String.valueOf(bigInteger));
    }
}
