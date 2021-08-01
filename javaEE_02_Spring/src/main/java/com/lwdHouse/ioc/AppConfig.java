package com.lwdHouse.ioc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.ZoneId;

/**
 * 使用Annotation配合自动扫描大幅简化Spring的配置
 */
@Configuration  // 配合AnnotationConfigApplicationContext
@ComponentScan  // 自动搜索当前类所在的包以及子包，把所有标注为@Component的Bean自动创建出来，并根据@Autowired进行装配
@PropertySource("app.properties")   // 配置属性文件，在@Configuration配置类上添加, 之后可以使用@Value(${key})来获取值
@PropertySource("smtp.properties")
@EnableAspectJAutoProxy // 开启切面自动代理，会扫描所有带@Aspect注解的bean，并织入方法
/**
 * AOP原理：对接口类型使用JDK动态代理，对普通类使用CGLIB创建子类
 * 拦截器类型：@Before @After @AfterReturning @AfterThrowing @Around
 */
public class AppConfig {
    public static void main(String[] args) throws SQLException {
        // AnnotationConfigApplicationContext 必须传入一个标注了@Configuration的类名
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        LogoService logoService = context.getBean(LogoService.class);
        logoService.printLogo();

        UserService userService = context.getBean(UserService.class);
        User sam = userService.register("stulwd@163.com", "123456", "sam");
        System.out.println(sam);
    }

    // 创建一个第三方的Bean的方法是
    // 1.在Spring xml文件中定义bean
    // 2.在@Configuration类中编写一个Java方法创建并返回它
    // 注意：自己写的bean一般通过 @Component 注解来创建
    @Bean
        DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl,
                                @Value("${jdbc.username}") String username,
                                @Value("${jdbc.password}") String password){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        // 连接超时
        config.addDataSourceProperty("connectionTimeout", "1000");
        // 空闲超时
        config.addDataSourceProperty("idleTimeout", "6000");
        // 最大连接数
        config.addDataSourceProperty("maximumPoolSize", "10");
        return new HikariDataSource(config);
    }

    /**
     * 可以同时创建多个同类型的bean，但需要指定别名加以区分
     * 指定创建的Bean名称有两种方法（默认情况下创建的bean名称是类型名的首字母小写）
     *  1.@Bean("name")
     *  2.@Bean+@Qualifier("name")
     *
     * 装配的时候，同样要在@Autowired()旁加一个@Qualifier("")指定使用哪个bean装配
     * ，也可以在定义bean的时候，指定某一个为@Primary，这样在装配的时候，就会装配这个
     * Primary的bean，也不需要用@Qualifier("")指定了
     */
//    @Primary
    @Bean("z")
    ZoneId createZoneId(@Value("${app.zone:Z}") String zoneId){
        return ZoneId.of(zoneId);
    }

    @Bean
    @Qualifier("utc8")
    // 如果当前的Profile设置为test，
    // 则Spring容器会创建这个bean
    // @Profile("!test")表示Profile设置为非test，才会创建
    // 在运行程序时，加上-Dspring.profiles.active=test就可以指定test环境启动
    // @Profile({ "test", "master" })表示同时满足test环境，master分支代码
    // 启动时，加上-Dspring.profiles.active=test,master
    @Profile("test")
    ZoneId createZoneOfUTC8(){
        return ZoneId.of("UTC+08:00");
    }
}
