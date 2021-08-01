package com.lwdHouse.learnjava.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class User {
    private Long id;

    private String email;
    private String password;
    private String name;

    private long createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // 在处理rest请求时，返回的user对象，
    // 被@ResponseBody序列化为json串，但我们希望不要把密码也传给前端
    // 所以加上JsonIgnore
    // 但是这样一来，如果写一个register(User user)方法，那么该方法的User对象也拿不到注册时用户传入的密码了
    // 所以最好使用@JsonProperty,只允许反序列化（写），不允许序列化（读）
//    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getImageUrl() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(this.email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
            return "https://www.gravatar.com/avatar/" + String.format("%032x", new BigInteger(1, hash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
