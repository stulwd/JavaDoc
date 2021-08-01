package com.lwdHouse.jpa;

import com.lwdHouse.jpa.entity.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = userService.fetchUserByEmail("ben4@163.com");
        System.out.println(user);
        User user2 = userService.getUserByEmail("ben4@163.com");
        System.out.println(user2);
        User user3 = userService.login("stulwd@163.com", "960427");
        System.out.println(user3);
        User user4 = userService.register("qaq3@163.com", "007", "123");
        System.out.println(user4);
        userService.delete(23);
        // 这个有问题，不生效
        userService.update(20, "宝宝");
    }


    @Bean
    DataSource createDataSource(    @Value("${jdbc.url}") String jdbcUrl,
                                    @Value("${jdbc.username}") String jdbcUsername,
                                    @Value("${jdbc.password}") String jdbcPassword )
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean createEntityManagerFactoryBean(@Autowired DataSource dataSource){
        var entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.lwdHouse.jpa.entity");
        // 指定JPA的提供商是Hibernate:
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        // 设定特定提供商自己的配置
        var props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        props.setProperty("hibernate.show_sql", "true");
        entityManagerFactoryBean.setJpaProperties(props);
        return entityManagerFactoryBean;
    }

    @Bean
    PlatformTransactionManager createTransactionManager(@Autowired EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
