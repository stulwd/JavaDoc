package com.lwdHouse.learnjava.web;


import com.lwdHouse.learnjava.entity.User;
import com.lwdHouse.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * spring 的 Rest API
 * 下面的rest api的get方法可以用浏览器输地址测试
 * post请求输入以下命令测试：
 * curl -v -H "Content-Type: application/json" -d '{"email":"yangxin2021678@163.com”,"password":"234234"}' http://localhost:8080/api/signin
 */

/**
 * 异步请求支持：
 * 必须要在web.xml的
 * url-pattern地址匹配到的servlet和filter
 * 的<servlet></servlet>和
 * <Filter></Filter>
 * 标签中加上
 * <async-supported>true</async-supported>（或者在相应的 Filter 上加 @EnableAsync注解）
 * 来启动异步支持
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
//    public List<User> users(){
//        return userService.getUsers();
//    }
    // 异步请求方法一：返回一个Callable
    // Spring MVC自动把返回的Callable放入线程池执行，等待结果返回后再写入响应：
    // 浏览器访问http://localhost:8080/api/users/后
    // 得到
    //2021-06-16 21:45:28 {http-nio-8080-exec-8} INFO  c.l.learnjava.web.LoggerInterceptor - preHandle /api/users/...
    //2021-06-16 21:45:28 {http-nio-8080-exec-8} INFO  c.l.learnjava.web.AuthInterceptor - pre authenticate /api/users/...
    //2021-06-16 21:45:31 {http-nio-8080-exec-9} INFO  c.l.learnjava.web.LoggerInterceptor - preHandle /api/users/...
    //2021-06-16 21:45:31 {http-nio-8080-exec-9} INFO  c.l.learnjava.web.AuthInterceptor - pre authenticate /api/users/...
    //2021-06-16 21:45:31 {http-nio-8080-exec-9} INFO  c.l.learnjava.web.LoggerInterceptor - postHandle /api/users/...
    //2021-06-16 21:45:31 {http-nio-8080-exec-9} INFO  c.l.learnjava.web.LoggerInterceptor - afterCompletion /api/users/: Exception = null
    // 可以看到是由http-nio-8080-exec-8 http-nio-8080-exec-9 两个线程来处理的
    public Callable<List<User>> users(){
        return () -> {
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
            }
            return userService.getUsers();
        };
    }

    @GetMapping("/users/{id}")
//    public User user(@PathVariable("id") long id){
//        return userService.getUserById(id);
//    }
    // 异步请求方法二：返回DeferredResult（推荐使用）
    // 在实际使用时，经常用到的就是DeferredResult，因为返回DeferredResult时，可以设置超时、正常结果和错误结果，易于编写比较灵活的逻辑。
    public DeferredResult<User> user(@PathVariable("id") long id){
        DeferredResult<User> result = new DeferredResult<>(3000L);// 3s超时，超时会自动返回超时错误响应
        new Thread(()->{
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
            }
            try{
                User user = userService.getUserById(id);
                result.setResult(user);
            }catch (Exception e){
                result.setErrorResult(Map.of("error", e.getClass().getSimpleName(), "message", e.getMessage()));
            }
        }).start();
        return result;
    }


    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SignInRequest signInRequest){
        try{
            User user = userService.signin(signInRequest.email, signInRequest.password);
            return Map.of("user", user);
        }catch (Exception e){
            return Map.of("error", "SIGNIN_FAILED", "message", e.getMessage());
        }
    }

    public static class SignInRequest {
        public String email;
        public String password;
    }
}
