package com.lwdHouse.learnjava;


import com.lwdHouse.learnjava.config.MasterDataSourceConfiguration;
import com.lwdHouse.learnjava.config.RoutingDataSourceConfiguration;
import com.lwdHouse.learnjava.config.SlaveDataSourceConfiguration;
import com.lwdHouse.learnjava.service.redis.RedisConfiguration;
import com.lwdHouse.learnjava.web.chat.ChatHandShakeInterceptor;
import com.lwdHouse.learnjava.web.chat.ChatHandler;
import com.mitchellbosecke.pebble.boot.autoconfigure.PebbleProperties;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// SpringBootApplication注解包含了如下注解
//- @SpringBootConfiguration
//  - @Configuration
//- @EnableAutoConfiguration
//  - @AutoConfigurationPackage
//- @ComponentScan
@SpringBootApplication
@EnableWebSocket  // 启动WebSocket支持
@EnableScheduling   // 定时任务支持在SpringBoot应用里这样写也生效啦
@EnableMBeanExport  // MBean在SpringBoot应用里这样写是生效的
// 禁用自动配置（这里我们举例禁用DataSourceAutoConfiguration自动配置）
// 启动自动配置，但排除指定的自动配置:
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
// 这些class并没有添加Component或者Configuration，要使之生效，需要使用@Import导入
// 要么在要导入的类使用@Configuration注解，要么在这里使用@Import进行导入
@Import({MasterDataSourceConfiguration.class, SlaveDataSourceConfiguration.class,
        RoutingDataSourceConfiguration.class, /*redis配置类*/RedisConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * application.yml中各个模块的配置类，点进去可以查看
     */
    public static void propertiesLocation(){
        KafkaProperties.Consumer c = null;
        RabbitProperties.Cache cache = null;
        MailProperties mp = null;
        RedisProperties rp = null;
        ArtemisProperties ap = null;
        DataSourceProperties dp = null;
        HikariConfig hc = null;
        PebbleProperties pp = null;
        ServerProperties sp = null;
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors){
        return new WebMvcConfigurer() {

            /**
             * 配置处理静态文件的的Handlers：
             * 题外话：
             * 在一般的开发中，我们通过下面这种方式，或者编写一个FileServlet（javaEE_01_web 的 Part08_04_FileServlet），
             * 或者通过Filter的方式，来处理静态文件。
             * 但是tomcat并不擅长处理静态文件，也不适合直接暴露给用户，
             * 通常，我们在生产环境部署时，总是使用类似Nginx这样的服务器充当反向代理和静态服务器，
             * 只有动态请求才会放行给应用服务器，所以，部署架构如下
             *              ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
             *
             *              │  /static/*            │
             * ┌───────┐      ┌──────────> file
             * │Browser├────┼─┤                     │    ┌ ─ ─ ─ ─ ─ ─ ┐
             * └───────┘      │/          proxy_pass
             *              │ └─────────────────────┼───>│  Web Server │
             *                        Nginx
             *              └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘    └ ─ ─ ─ ─ ─ ─ ┘
             * 使用Nginx配合Tomcat服务器，可以充分发挥Nginx作为网关的优势，既可以高效处理静态文件，
             * 也可以把https、防火墙、限速、反爬虫等功能放到Nginx中，
             * 使得我们自己的WebApp能专注于业务逻辑。
             * 如何配置nginx反向代理？
             * 1.下载nginx包，这里我们解压放到了项目根目录
             * 2.修改conf/nginx.conf文件，如何修改参考how-to-use-nginx.md
             * 3.启动nginx，一定要在nginx的一级目录下，在命令行直接敲 \nginx.exe 启动，配置文件默认就会使用conf/nginx.conf
             *   log文件会使用logs/下的两个，一个是访问记录log，一个是错误log
             * 【注意】
             * 1. nginx对于使用者不太友好，不要额外指定配置文件路径，在conf\nginx.conf的基础上修改即可
             * 2. 启动后，cmd卡住，就是正常启动，想要结束，重新开一个cmd，在 nginx 一级目录下输入 nginx.exe -s quit即可,
             *    一定要在nginx一级目录下操作，因为quit的时候，也要找logs/nginx.pid文件
             * 3. 也可以手动杀掉nginx进程，windows下，使用命令 tasklist | findstr "nginx"查看所有进程的pid，然后kill pid
             *    把nginx所有进程杀掉
             *
             * 结论：我们下面加的静态文件处理器，只是方便我们开发测试，在生产环境中，有nginx，
             *      静态请求根本不会交给tomcat来处理
             * @param registry
             */
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 映射路径`/static/`到classpath路径:
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                for (var interceptor : interceptors) {
                    registry.addInterceptor(interceptor);
                }
            }
        };
    }

    /**
     * AMQP的MessageConverter
     * 用于将Java对象转换为 RabbitMQ 能处理的消息
     * 默认情况下，Spring Boot使用SimpleMessageConverter，
     * 只能发送String和byte[]类型的消息，不太方便。
     * 使用Jackson2JsonMessageConverter，我们就可以发送JavaBean对象，
     * 由Spring Boot自动序列化为JSON并以文本消息传递
     * @return
     */
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
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

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
