package com.lwdHouse.ioc;

import com.lwdHouse.ioc.aop.MetricTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)   // 指定scope为PROTOTYPE，即每次调用getBean返回新的实例，默认不指定的话，是sington单例模式
public class UserService {
    @Autowired // Autowired可以写在set方法上，也可以写在字段上，也可以写在构造方法的参数前
//    @Autowired(required = false)    // 要是找不到bean，可以不装配，不报错
    private MailService mailService;
    @Autowired // 这个dataSource bean来自于第三方包，创建方式有两种
    // 1. 在spring xml文件中创建bean
    // 2. 在@Configuration类中编写一个Java方法创建并返回它，详见 AppConfig.java
    private DataSource dataSource;

    public void setMailService( /* 写在构造方法参数前装配bean，类似于xml配置文件中的构造注入 @Autowired*/ MailService mailService) {
        this.mailService = mailService;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /* 模拟数据库 */
    private List<User> users = new ArrayList<>(List.of( // users:
            new User(1, "bob@example.com", "password", "Bob"), // bob
            new User(2, "alice@example.com", "password", "Alice"), // alice
            new User(3, "tom@example.com", "password", "Tom"))); // tom

    /**
     * 使用注解，让MetricAspect为这个方法添加通知
     */
    @MetricTime("login")
    public User login(String email, String password) throws SQLException {
        /* 模拟登录 */
//        for (User u : users) {
//            if (u.getEmail().equals(email)){
//                if (u.getPassword().equals(password)){
//                    mailService.sendLoginMail(u);
//                    return u;
//                }
//            }
//        }
        /* 真实登录 */
        User loggedUser = null;
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT id, name, email, password FROM users WHERE email=?")){
                ps.setObject(1, email);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    loggedUser = new User(rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"));
                    if (loggedUser.getPassword().equals(password)){
                        mailService.sendLoginMail(loggedUser);
                        return loggedUser;
                    }else {
                        throw new RuntimeException("wrong password");
                    }
                }

            }
        }
        throw new RuntimeException("login failed.");
    }

    public User getUser(long id){
        return users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
    }


    /**
     * 使用注解，让MetricAspect为这个方法添加通知
     */
    @MetricTime("register")
    public User register(String email, String password, String name) throws SQLException {
        // 模拟注册
//        users.forEach(user -> {
//            if (user.getEmail().equals(email)){
//                throw new RuntimeException("email Exists");
//            }
//        });
//        User u = new User(users.stream().mapToLong(user -> user.getId()).max().getAsLong()+1, email, password, name);
//        users.add(u);
//        mailService.sendRegistrationMail(u);
//        return u;

        // 真实注册
        User registeredUser = null;
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, email, password) values (?, ?, ?)",
                                                                    Statement.RETURN_GENERATED_KEYS)){
                ps.setObject(1, name);
                ps.setObject(2, email);
                ps.setObject(3, password);
                int n = ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()){
                        long id = rs.getLong(1);
                        registeredUser = new User(id, name, email, password);
                        mailService.sendRegistrationMail(registeredUser);
                        return registeredUser;
                    }
                }
            }
        }
        throw new RuntimeException("registered failed!");
    }
}


class User{
    private long id;
    private String name;
    private String email;
    private String password;

    public User(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}