package com.lwdHouse.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

@ComponentScan
@Configuration
@PropertySource("jdbc.properties")
@EnableTransactionManagement    // 启动声明式事务
public class AppConfig {

    @Value("${jdbc.url}")
    String jdbcUrl;
    @Value("${jdbc.username}")
    String jdbcUsername;
    @Value("${jdbc.password}")
    String jdbcPassword;

    /**
     * HikariConfig数据源
     * @return
     */
    @Bean
    DataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    /**
     * Spring提供的JdbcTemplate采用Template模式，
     * 提供了一系列以回调为特点的工具方法，目的是避免繁琐的try...catch语句。
     */
    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    /**
     * 事务管理器
     */
    @Bean
    PlatformTransactionManager createManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User liuwendi = userService.register("贝贝@163.com", "960427", "大瓜");
        System.out.println(liuwendi);

        User user = userService.getUserById(5);
        System.out.println(user);

        User adi = userService.getUserByName("adi");
        System.out.println(adi);

        User userByEmail = userService.getUserByEmail("stulwd@163.com");
        System.out.println(userByEmail);

        long amount = userService.getUsers();
        System.out.println(amount);

        // 从第一行开始
        List<User> users = userService.getUsers(1);
        users.forEach(user1 -> System.out.println(user1));

        userService.updateUser(new User(12, "gau@huawei.com", "960427", "小瓜"));

    }
}
