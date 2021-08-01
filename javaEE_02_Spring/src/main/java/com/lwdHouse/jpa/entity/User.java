package com.lwdHouse.jpa.entity;

import com.lwdHouse.hibernate.entity.AbstractEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * 如下数据表所对应的JavaBean
 * CREATE TABLE user
 *     id BIGINT NOT NULL AUTO_INCREMENT,
 *     email VARCHAR(100) NOT NULL,
 *     password VARCHAR(100) NOT NULL,
 *     name VARCHAR(100) NOT NULL,
 *     createdAt BIGINT NOT NULL,
 *     PRIMARY KEY (`id`),
 *     UNIQUE KEY `email` (`email`)
 * );
 */
@NamedQueries(
        @NamedQuery(
            name = "login",
            query = "SELECT u FROM User u WHERE u.email= :e AND u.password= :p"
        )
)
@Entity // 如果一个JavaBean被用于映射，我们就标记一个@Entity
@Table(name = "users")      // 如果实际的表名不同，例如实际表名是users，可以追加@Table
@Proxy(lazy = false)  // 设置为懒加载，解决getUserById之后session立即结束，获取不到user的问题，目前不太懂这里的逻辑
public class User extends AbstractEntity {

    private String email;
    private String password;
    private String name;

    public void setEmail(String email) {
        this.email = email;
        System.out.println("设置email");
    }

    public void setPassword(String password) {
        this.password = password;
        System.out.println("设置password");
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("设置name");
    }

    // 每个属性到数据库列的映射用@Column()标识
    @Column(nullable = false, unique = true, length = 100)
    public String getEmail() {
        System.out.println("获取email");
        return email;
    }

    @Column(nullable = false, length = 100)
    public String getPassword() {
        System.out.println("获取password");
        return password;
    }

    @Column(nullable = false, length = 100)
    public String getName() {
        System.out.println("获取name");
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
