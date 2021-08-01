package com.lwdHouse.hibernate;

//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
import com.lwdHouse.hibernate.entity.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.orm.hibernate5.HibernateTemplate;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);

        User user = userService.register("stulwd5@163.com", "960427", "liuwendi");
//        设置createdAt       // 持久化之前 @PrePersist
//        获取createdAt       // 获取所有属性
//        获取email
//        获取name
//        获取password
//        Hibernate: insert into users (createdAt, email, name, password) values (?, ?, ?, ?)
//        设置id              // 设置id
//        获取id              // 获取所有属性，并返回
//        获取createdAt
//        获取email
//        获取name
//        获取password
        System.out.println(user);

        User u1 = userService.getUserById(Long.valueOf(14));
//        Hibernate: select user0_.id as id1_1_0_, user0_.createdAt as createdA2_1_0_, user0_.email as email3_1_0_, user0_.name as name4_1_0_, user0_.password as password5_1_0_ from users user0_ where user0_.id=?
//        设置id
//        设置createdAt
//        设置email
//        设置name
//        设置password
        System.out.println("u1: "+u1);

        User u2 = userService.fetchUserByEmail("stulwd@163.com");
        System.out.println("u2: "+u2);
//        同上

        List<User> us = userService.getUsers(1);
        for (User u : us) {
            System.out.println("user list: "+u);
        }

        boolean isDel = userService.deleteUser(Long.valueOf(6));
        System.out.println("isDel: " + isDel);

        userService.updateUser(Long.valueOf(10), "BABA111");
        User u9 = userService.login("stulwd@163.com", "960427");
        System.out.println("u9: "+u9);
        User u4 = userService.login2("stulwd@163.com", "960427");
        System.out.println("u4: "+u4);
        User u5 = userService.login3("stulwd@163.com", "qqq", "960427");
        System.out.println("u5: "+u5);
        User u6 = userService.login4("stulwd@163.com", "960427");
        System.out.println("u6: "+u6);
        User u7 = userService.login5("stulwd@163.com", "960427");
        System.out.println("u7: "+u7);
        User u8 = userService.login6("stulwd@163.com", "960427");
        System.out.println("u8: "+u8);
    }

    @Value("${jdbc.url}")
    String jdbcUrl;
    @Value("${jdbc.username}")
    String jdbcUsername;
    @Value("${jdbc.password}")
    String jdbcPassword;
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
     * SessionFactory是用来创建session的工厂
     * SessionFactory是封装了JDBC DataSource的实例
     * Session是封装了一个JDBC Connection的实例
     * SessionFactory持有连接池，每次需要操作数据库的时候，SessionFactory创建一个新的Session，相当于从连接池获取到一个新的Connection
     * @param dataSource
     * @return
     */
    @Bean
    LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource){
        var props = new Properties();
        // 表示自动创建数据库的表结构，注意不要在生产环境中启用；
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        // 指示Hibernate使用的数据库是HSQLDB，
        // Hibernate使用一种HQL的查询语句，它和SQL类似，但真正在“翻译”成SQL时，
        // 会根据设定的数据库“方言”来生成针对数据库优化的SQL；
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        // 让Hibernate打印执行的SQL，这对于调试非常有用，
        // 我们可以方便地看到Hibernate生成的SQL语句是否符合我们的预期
        props.setProperty("hibernate.show_sql", "true");
        var sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // 指示Hibernate扫描这个包下面的所有Java类，自动找出能映射为数据库表记录的JavaBean
        sessionFactoryBean.setPackagesToScan("com.lwdHouse.hibernate.entity");
        sessionFactoryBean.setHibernateProperties(props);
        return sessionFactoryBean;
    }

    @Bean
    HibernateTemplate createHibernateTemplate(@Autowired SessionFactory sessionFactory){
        return new HibernateTemplate(sessionFactory);
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }
}
