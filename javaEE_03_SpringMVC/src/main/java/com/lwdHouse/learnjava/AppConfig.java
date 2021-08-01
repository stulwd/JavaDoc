package com.lwdHouse.learnjava;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.web.chat.ChatHandShakeInterceptor;
import com.lwdHouse.learnjava.web.chat.ChatHandler;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.jms.ConnectionFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.util.*;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource(
        {
                "classpath:/jdbc.properties",
                "classpath:/smtp.properties",
                "classpath:/jms.properties",
                "classpath:/task.properties"    // 定时任务配置参数
        }
)
@EnableWebMvc   // 启动WebMvc支持
@EnableWebSocket  // 启动WebSocket支持
@EnableJms          // 启动JMS 让spring自动扫描 @JmsListener
                    // 即enables detection of JmsListener annotations on any Spring-managed bean in the container
@EnableScheduling   // 开启定时任务的支持
@EnableMBeanExport // 自动注册MBean
// JMX是在命令行里直接输入jconsole命令来启动的
// 使用jconsole直接通过Local Process连接JVM有个限制，就是jconsole和正在运行的JVM必须在同一台机器
// 如果要远程连接，首先要打开JMX端口。我们在启动AppConfig时，需要传入以下JVM启动参数
// -Dcom.sun.management.jmxremote.port=19999  表示在19999端口监听JMX连接
// -Dcom.sun.management.jmxremote.authenticate=false 表示不使用SSL连接
// -Dcom.sun.management.jmxremote.ssl=false 表示不使用SSL连接
// 第2，3个参数在开发测试阶段比较方便，生产环境必须指定验证方式并启用SSL
// 这样jconsole可以用ip:19999的远程方式连接JMX。连接后的操作是完全一样的
// 许多JavaEE服务器如JBoss的管理后台都是通过JMX提供管理接口，并由Web方式访问，对用户更加友好。
public class AppConfig {

    public static void main(String[] args) throws LifecycleException {
        // 启动嵌入式tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("", new File("javaEE_03_SpringMVC/src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File("javaEE_03_SpringMVC/target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
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

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // WebMvcConfigurer并不是必须的，但我们在这里创建一个默认的WebMvcConfigurer
    @Bean
    WebMvcConfigurer createWebMvcConfigure(@Autowired HandlerInterceptor[] interceptors){
        return new WebMvcConfigurer(){
            // 覆写addResourceHandlers()，目的是让Spring MVC自动处理静态文件，并且映射路径为/static/**
            // 相当于我们javaEE_01_Web的FileServlet

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry){
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }

            // 覆写addInterceptors，注册所有的拦截器，让拦截器生效
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                for (var interceptor : interceptors) {
                    registry.addInterceptor(interceptor);
                }
            }

            /**
             * 跨域请求：
             * 通常情况下，浏览器的javaScript访问服务器时，
             * 要求本站点（地址栏中的地址）和服务器的协议名，域名，端口要一致才能访问，也就是自己才能访问自己。
             * 要是别的站点要访问这个服务器，就是跨域请求，服务器则需要添加允许的地址
             * 有三种方法添加：
             * 1. 可以在@RestController的class级别或方法级别定义一个@CrossOrigin
             *    // 指定了只允许来自local.liaoxuefeng.com跨域访问
             *      @CrossOrigin(origins = "http://local.liaoxuefeng.com:8080")
             *      @RestController
             *      @RequestMapping("/api")
             *      public class ApiController {
             *          ...
             *      }
             * 2. 如下
             * 3. 使用Spring提供的CorsFilter，需要将Spring容器内置的Bean暴露为Servlet容器的Filter的方法，由于这种配置方式需要修改web.xml，也比较繁琐，所以推荐使用第二种方式。
             */
            // 处理跨域请求
            // 允许"https://www.baidu.com"站
            // 使用javaScript对本服务器的/api/**进行get，post，head访问
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("https://www.baidu.com")
                        .allowedMethods("GET", "POST", "HEAD").maxAge(3600);
            }
        };
    }

    // 指定视图解析器
    // 在learnSSM spring-mvc-02中，视图解析器定义在springmvc.xml中，用org.springframework.web.servlet.view.InternalResourceViewResolver
    // ViewResolver通过指定prefix和suffix来确定如何查找View
    // 使用Pebble引擎，指定模板文件存放在/WEB-INF/templates/目录下
    @Bean
    ViewResolver createViewResolver(@Autowired ServletContext servletContext,
                                    @Autowired @Qualifier("i18n") MessageSource messageSource){
        PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true)
                .cacheActive(false)
//                ServletLoader：Loader that uses a servlet context to find templates.
//                for example: servletContext.getResourceAsStream("/WEB-INF/templates")
//                servletContext的Resource目录就是在webapp下
                .loader(new ServletLoader(servletContext))
//                .extension(new SpringExtension())
                .extension(createExtension(messageSource))
                .build();
        PebbleViewResolver viewResolver = new PebbleViewResolver();
        viewResolver.setPrefix("/WEB-INF/templates");
        viewResolver.setSuffix("");
        viewResolver.setPebbleEngine(engine);
        return viewResolver;
    }

    private Extension createExtension(MessageSource messageSource) {
        return new AbstractExtension() {
            @Override
            public Map<String, Function> getFunctions() {
                return Map.of("_", new Function() {
                    @Override
                    public List<String> getArgumentNames() {
                        return null;
                    }

                    @Override
                    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context,
                                          int lineNumber) {
                        String key = (String) args.get("0");
                        List<Object> arguments = this.extractArguments(args);
                        Locale locale = (Locale) context.getVariable("__locale__");
                        return messageSource.getMessage(key, arguments.toArray(), "???" + key + "???", locale);
                    }

                    private List<Object> extractArguments(Map<String, Object> args) {
                        int i = 1;
                        List<Object> arguments = new ArrayList<>();
                        while (args.containsKey(String.valueOf(i))) {
                            Object param = args.get(String.valueOf(i));
                            arguments.add(param);
                            i++;
                        }
                        return arguments;
                    }
                });
            }
        };
    }

    /**
     * 语言国际化
     * @return
     */
    @Bean
    LocaleResolver createLocaleResolver() {
        var clr = new CookieLocaleResolver();
        // CookieLocaleResolver从HttpServletRequest中获取Locale时，
        // 首先根据一个特定的Cookie判断是否指定了Locale，
        // 如果没有，就从HTTP头获取，如果还没有，就返回默认的Locale
        // 当用户第一次访问网站时，CookieLocaleResolver只能从HTTP头获取Locale，即使用浏览器的默认语言
        // 通常网站也允许用户自己选择语言，此时，CookieLocaleResolver就会把用户选择的语言存放到Cookie中，
        // 下一次访问时，就会返回用户上次选择的语言而不是浏览器默认语言。
        clr.setDefaultLocale(Locale.ENGLISH);
        clr.setDefaultTimeZone(TimeZone.getDefault());
        return clr;
    }

    // 创建MessageSource，它自动读取所有的.properties文件，并提供一个统一接口来实现“翻译”
    // 例如：String text = messageSource.getMessage("signin", null, locale);
    //      就会返回对应locale的资源文件里的signin的值
    // signin是我们在.properties文件中定义的key
    // 第二个参数是Object[]数组作为格式化时传入的参数
    // 最后一个参数就是获取的用户Locale实例，见MvcInterceptor
    // .properties资源文件定义：
    // 默认语言，文件名必须为messages.properties；
    // 简体中文，Locale是zh_CN，文件名必须为messages_zh_CN.properties；
    // 日文，Locale是ja_JP，文件名必须为messages_ja_JP.properties；
    @Bean("i18n") //Spring容器会创建不只一个MessageSource实例，我们自己创建的这个MessageSource是专门给页面国际化使用的，因此命名为i18n，不会与其它MessageSource实例冲突。
    MessageSource createMessageSource() {
        var messageSource = new ResourceBundleMessageSource();
        // 指定文件是UTF-8编码:
        messageSource.setDefaultEncoding("UTF-8");
        // 指定主文件名
        // 注意ResourceBundleMessageSource会自动根据主文件名自动把所有相关语言的资源文件
        // （messages_zh_CN.properties, messages_ja_JP.properties...）都读进来。
        messageSource.setBasename("messages");
        return messageSource;
    }


    @Bean
    ObjectMapper createObjectMapper() {
        var om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return om;
    }

    /**
     * 创建webSocket
     */
    @Bean
    WebSocketConfigurer createWebSocketConfigurer(@Autowired ChatHandler chatHandler,
                                                  @Autowired ChatHandShakeInterceptor chatInterceptor){
        return new WebSocketConfigurer() {
            @Override
            public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                registry.addHandler(chatHandler, "/chatRoom")
                        .addInterceptors(chatInterceptor);
            }
        };
    }

    /**
     * 创建mailSender
     */
    @Bean
    JavaMailSender createJavaMailSender(
            @Value("${smtp.host}")  String host,
            @Value("${smtp.port}")  int port,
            @Value("${smtp.auth}")  String auth,
            @Value("${smtp.username}")  String username,
            @Value("${smtp.password}")  String password,
            @Value("${smtp.debug:true}")  String debug
    ){
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPassword(password);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        if (port == 587){
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (port == 465){
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        props.put("mail.debug", debug);
        return mailSender;
    }

    /**
     * 创建Jms的ConnectionFactory
     * 即连接消息服务器的连接池
     * 相当于jdbc 的 DataSource
     */
    @Bean
    ConnectionFactory createJMSConnectionFactory(
            @Value("${jms.url:tcp://localhost:61616}") String uri,
            @Value("${jms.username:admin}") String username,
            @Value("${jms.password:password}") String password
    ){
        return new ActiveMQJMSConnectionFactory(uri, username, password);
    }

    /**
     * 创建JmsTemplate
     * 相当于jdbc 的 JdbcTemplate
     * 可以用来简化代码
     */
    @Bean
    JmsTemplate createJmsTemplate(@Autowired ConnectionFactory connectionFactory){
        return new JmsTemplate(connectionFactory);
    }

    /**
     * 创建JmsListenerContainerFactory
     * 这个Bean的作用Consumer容器，即 is responsible to create the listener container responsible for a particular endpoint
     * 就是为@JmsListener方法创建的Listener 的容器
     * 作用就是为每个MessageListener创建MessageConsumer并启动消息接收循环
     */
    @Bean("jmsListenerContainerFactory")
    DefaultJmsListenerContainerFactory createJmsListenerContainerFactory(@Autowired ConnectionFactory connectionFactory){
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    /**
     * 重新创建一个TaskScheduler，来解决spring定时任务和webSocket同时存在，启动报错的问题
     * 报错：
     *     Bean named ‘defaultSockJsTaskScheduler’ is expected to be
     *     of type ‘org.springframework.scheduling.TaskScheduler’
     *     but was actually of type ‘org.springframework.beans.factory.support.NullBean’
     * 原因：（参考：https://www.codenong.com/cs106517168/）
     *      webSocket在初始化的时候，会创建一个null的TaskScheduler类型，而@EnableScheduling
     * 注解会开启spring任务调度器，进而会加载这个TaskScheduler，而这个TaskScheduler是null的，所以
     * 报错了。解决办法就是再创建一个TaskScheduler Bean覆盖那个null的。不太明白
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
