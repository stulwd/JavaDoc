package com.lwdHouse.ioc;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.sql.SQLException;

/**
 * 使用xml文件配置Spring容器
 */
public class App {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("application_01.xml");
        UserService service = (UserService) context.getBean("userService");
        User registeredUser = service.register("stulwd@163.com", "1800435", "liuwendi");
        System.out.println(registeredUser);
        User user = service.login("stulwd@163.com", "1800435");
        System.out.println(user.getName());
        
        // 根据bean的类型获得bean
        MailService mailService = context.getBean(MailService.class);

        // 另一种ioc容器BeanFactory（不推荐）
        // BeanFactory的实现是按需创建，即第一次获取Bean时才创建这个Bean
        // 而ApplicationContext会一次性创建所有的Bean
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("application_01.xml"));
        MailService service1 = factory.getBean(MailService.class);
    }
}
