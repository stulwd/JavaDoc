package com.lwdHouse.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



// 使用注解代替xml文件配置bean，创建的bean名称默认是首字母小写的同名
@Component
public class MailService {
    // 有bean的话，就装配bean，没有bean的话使用现定义的
    @Autowired(required = false)
    @Qualifier("z")
    private ZoneId zoneId = ZoneId.systemDefault();

    // #{}表示从JavaBean读取属性
    // "#{smtpConfig.host}"的意思是，从名称为smtpConfig的Bean读取host属性
    @Value("#{smtpConfig.host}")
    private String smtpHost;
    @Value("#{smtpConfig.port}")
    private int smtpPort;

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public String getTime(){
        return ZonedDateTime.now(zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public void sendLoginMail(User user){
        System.err.println(String.format("Hi, %s! you are logged in at %s", user.getName(), getTime()));
    }

    public void sendRegistrationMail(User user) {
        System.err.println(String.format("Welcome, %s!", user.getName()));
    }

    /**
     * @PostConstruct 和 @PreDestroy定义了创建bean之后和销毁bean之前要进行的操作
     * 使用这两个注解需要引入依赖：JSR-250定义的Annotation
     * <dependency>
     *      <groupId>javax.annotation</groupId>
     *      <artifactId>javax.annotation-api</artifactId>
     *      <version>1.3.2</version>
     *  </dependency>
     *
     * spring容器对bean进行的创建顺序是：
     *  1.调用构造方法创建bean实例；
     *  2.根据@Autowired进行注入；
     *  3.调用标记有@PostConstruct的init()方法进行初始化。
     */
    @PostConstruct
    public void init(){
        System.out.println("Init MailService with zoneId = " + zoneId);
    }
    @PreDestroy
    public void shutdown(){
        System.out.println("shutdown mail service");
    }
}
