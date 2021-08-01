package com.lwdHouse.learnjava.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdHouse.learnjava.entity.User;
import com.lwdHouse.learnjava.service.UserService;
//import com.lwdHouse.learnjava.web.jms.MessagingService;
//import com.lwdHouse.learnjava.web.mail.MailMessage;
//import com.lwdHouse.learnjava.web.mail.MailService;
import com.lwdHouse.learnjava.service.artemis.MessagingService;
import com.lwdHouse.learnjava.service.kafka.KfkMsgService;
import com.lwdHouse.learnjava.service.mail.MailMessage;
import com.lwdHouse.learnjava.service.mail.MailService;
import com.lwdHouse.learnjava.service.rabbitMQ.RabbitMsgService;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.LoginMessage;
import com.lwdHouse.learnjava.service.rabbitMQ.messaging.RegistrationMessage;
import com.lwdHouse.learnjava.service.redis.RedisService;
import com.lwdHouse.learnjava.web.mbean.UserRegistrationStatisticMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping("/user") // 可以对URL进行分组
public class UserController {

    public static final String KEY_USER = "__user__";
    public static final String KEY_USER_ID = "__userid__";

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    MessagingService messagingService;

    @Autowired
    UserRegistrationStatisticMBean userRegistrationStatisticMBean;

    @Autowired
    RedisService redisService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RabbitMsgService rabbitMsgService;

    @Autowired
    KfkMsgService kfkMsgService;

    /**
     * 处理Rest请求
     * @PostMapping使用consumes声明能接收的类型，使用produces声明输出的类型
     * 直接用Spring的Controller配合一大堆注解写REST太麻烦了，
     * 因此，Spring还额外提供了一个@RestController注解，
     * 使用@RestController替代@Controller后，每个方法自动变成API接口方法
     * 参考ApiController.java
     */
    @PostMapping(value = "/rest",
                 consumes = "application/json;charset=utf-8",
                 produces = "application/json;charset=utf-8")
    // 加了@ResponseBody表示返回的String无需额外处理，直接作为输出内容写入HttpServletResponse
    @ResponseBody
    // 输入的JSON则根据注解@RequestBody直接被Spring反序列化为User这个JavaBean
    public String rest(@RequestBody User user){
        return "{\"restSupport\": true}";
    }

    /**
     * 在以前，登录之后，把user对象直接放入httpSession，这样不好
     * 优化为：
     * 登录之后，把userid放入httpSession，并调用putUserIntoRedis把user放入redis，这样节省Session空间
     * 获取user时，调用getUserFromRedis也能迅速获取到user。
     */
    public boolean putUserIntoRedis(User user) throws JsonProcessingException {
        return redisService.hset(KEY_USER_ID, user.getId().toString(), objectMapper.writeValueAsString(user));
    }
    public User getUserFromRedis(HttpSession session) throws JsonProcessingException {
        Long id = (Long) session.getAttribute(KEY_USER_ID);
        if (id == null){
            return null;
        }
        String json = redisService.hget(KEY_USER_ID, id.toString());
        return json == null ? null : objectMapper.readValue(json, User.class);
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession session) throws JsonProcessingException {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        Map<String, Object> model = new HashMap<>();
        if (user != null){
            model.put("user", user);
        }
        return new ModelAndView("index.html", model);
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@RequestParam("email") String email, @RequestParam("password") String password,
                                   @RequestParam("name") String name) throws JsonProcessingException {
        try {
            User user = userService.register(email, password, name);
            logger.info("user registered: {}", user.getEmail());
            // 发送邮件
            // 方法一：直接开启一个线程用mailService发出去
//            new Thread(() -> {
//                mailService.sendRegistrationMail(user);
//            }).start();
            // 方法二：把消息发到MQ中，Consumer来调用mailService发邮件.
            messagingService.sendMailMessage(MailMessage.registration(user.getEmail(), user.getName()));
            userRegistrationStatisticMBean.updateAmount();
            // 将消息发送到rabbitMQ中
            rabbitMsgService.sendRegistrationMessage(RegistrationMessage.of(user.getEmail(), user.getName()));
            // 将消息发送到kafka MQ中
            kfkMsgService.sendRegistrationMessage(RegistrationMessage.of(user.getEmail(), user.getName()));
        } catch (RuntimeException e) {
            return new ModelAndView("register.html", Map.of("email", email, "error", "Register failed"));
        }
        return new ModelAndView("redirect:/signin");
    }

    @GetMapping("/signin")
    public ModelAndView signin(HttpSession session) throws JsonProcessingException {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        if (user != null) {
            return new ModelAndView("redirect:/profile");
        }
        return new ModelAndView("signin.html");
    }

    @PostMapping("/signin")
    public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password") String password,
                                 HttpSession session) throws JsonProcessingException {
        try {
            User user = userService.signin(email, password);
//            session.setAttribute(KEY_USER, user);
            // session只存user id，把user对象放到redis里
            session.setAttribute(KEY_USER_ID, user.getId());
            putUserIntoRedis(user);
            // 将消息发送到rabbitMQ中
            rabbitMsgService.sendLoginMessage(LoginMessage.of(user.getEmail(), user.getName(), true));
            // 将消息发送到kafka MQ中
            kfkMsgService.sendLoginMessage(LoginMessage.of(user.getEmail(), user.getName(), true));
        } catch (RuntimeException | IOException e) {
            // 将登录失败消息发送到rabbitMQ中
            rabbitMsgService.sendLoginMessage(LoginMessage.of(email, "(unknow)", false));
            // 将登录失败消息发送到kafka MQ中
            kfkMsgService.sendLoginMessage(LoginMessage.of(email, "(unknow)", false));
            return new ModelAndView("signin.html", Map.of("email", email, "error", "Signin failed"));
        }
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/changePassword")
    public ModelAndView changePwd(HttpSession session) throws JsonProcessingException {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        if (user != null){
            return new ModelAndView("/changePassword.html", Map.of("user", user));
        }
        return new ModelAndView("redirect:/signin");
    }

    @PostMapping("/changePassword")
    public ModelAndView doChangePwd(HttpSession session,
                                    @RequestParam("OldPassword") String OldPassword,
                                    @RequestParam("NewPassword") String NewPassword,
                                    @RequestParam("RepeatNewPassword") String RepeatNewPassword) throws JsonProcessingException {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        if (user == null){
            return new ModelAndView("redirect:/signin");
        }

        if (!OldPassword.equals(user.getPassword())){
            return new ModelAndView("/changePassword.html", Map.of("error", "wrong OldPassword!"));
        }
        if (!NewPassword.equals(RepeatNewPassword)){
            return new ModelAndView("/changePassword.html", Map.of("error", "wrong RepeatNewPassword!"));
        }
        if (NewPassword.equals(user.getPassword())){
            return new ModelAndView("/changePassword.html", Map.of("error", "new password is same as old."));
        }

        // 修改密码
        user.setPassword(NewPassword);
        try{
            userService.updatePassword(user);
        }catch (RuntimeException e){
            return new ModelAndView("/changePassword.html", Map.of("error", e.getMessage()));
        }
//        session.removeAttribute(KEY_USER);
        session.removeAttribute(KEY_USER_ID);
        return new ModelAndView("redirect:/signin");
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) throws JsonProcessingException {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }
        return new ModelAndView("profile.html", Map.of("user", user));
    }

    // 返回重定向时既可以写new ModelAndView("redirect:/xxx")，
    // 也可以直接返回String：
    @GetMapping("/signout")
    public String signout(HttpSession session) {
//        session.removeAttribute(KEY_USER);
        session.removeAttribute(KEY_USER_ID);
        return "redirect:/signin";
    }

    // 直接操作HttpResponse发送响应，返回null表示不需要再处理
    @GetMapping("/download")
    public ModelAndView download(HttpServletResponse response) throws IOException {


        byte[] data = "hello,world!".getBytes(StandardCharsets.UTF_8);
        // octet-stream表示下载文件
        response.setContentType("application/octet-stream");
        OutputStream output = response.getOutputStream();
        output.write(data);
        output.flush();
        return null;
    }

    // 模拟抛出异常
    @GetMapping("/game")
    public ModelAndView game(HttpServletResponse response){
        int i = 192 / 0;
        return null;
    }

    // 处理这个UserController异常的方法
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnknowException(Exception ex) {
        return new ModelAndView("500.html", Map.of("error", ex.getClass().getSimpleName(), "message", ex.getMessage()));
    }

    @GetMapping("/chat")
    public ModelAndView chat(HttpServletResponse response){
        return new ModelAndView("chat.html");
    }
}
