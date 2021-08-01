package com.lwdHouse.mybatis;

import com.lwdHouse.mybatis.entity.User;
import com.lwdHouse.mybatis.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

//    JDBC	        Hibernate   	JPA                 	MyBatis
//    DataSource	SessionFactory	EntityManagerFactory	SqlSessionFactory
//    Connection	Session	        EntityManager	        SqlSession

@ComponentScan
// 创建出所有的mapper bean，这里不用ComponentScan
// 有了@MapperScan，就可以让MyBatis自动扫描指定包的所有Mapper并创建实现类
@MapperScan("com.lwdHouse.mybatis.mapper")
@Configuration
@PropertySource("jdbc.properties")
@EnableTransactionManagement
public class AppConfig {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User lisa = userService.register("lisa@mybatis.com", "lisa", "123456");
        System.out.println("lisa: " + lisa);
        User user = userService.fetchUserByEmail("lisa@mybatis.com");
        System.out.println("lisa: " + lisa);
        List<User> users = userService.getUsers(1);
        for (User u : users) {
            System.out.println("userList:" + u);
        }
        userService.updateUser(lisa.getId(), "LISA");
        User lisaLogin = userService.login("lisa@mybatis.com", "123456");
        System.out.println("lisa login: " + lisaLogin);
        User lisa2 = userService.getUserById(lisa.getId());
        System.out.println("lisa2: "+lisa2);
        userService.deleteUser(lisa2.getId());
    }

    @Bean
    DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl,
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
    SqlSessionFactoryBean createFactoryBean(@Autowired DataSource dataSource){
        var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations(xxx); 如果sql语句定义在mapper.xml中，用此方法设置mapper路径
        return sqlSessionFactoryBean;
    }

    /* MyBatis可以直接使用Spring管理的声明式事务,因此，创建事务管理器和使用JDBC是一样的 */
    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }






}
