package com.lwdHouse;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class Part08_04_UserController {

    private static final Map<String, User> userMap =
            Map.of("Bob", new User("Bob", "Bob123"),
                   "Sam",new User("Sam",  "Sam123"),
                   "Lily",new User("Lily", "Lily123"));

    @Part08_05_GetMapping(path = "/signin")
    public Part08_02_ModelAndView signin() {
        return new Part08_02_ModelAndView("/signin.html");
    }

    /**
     * 响应了signin.html中的ajax请求
     */
    @Part08_05_PostMapping(path = "/signin")
    public Part08_02_ModelAndView signin(SignInBean bean, HttpSession session, HttpServletResponse response) throws IOException {
        User user = userMap.get(bean.username);
        if (user == null || !user.password.equals(bean.password)){
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write("{\"error\":\"Bad email or password\"}");
            pw.flush();
        }else {
            session.setAttribute("user", user);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write("{\"result\":true}");
            pw.flush();
        }
        return null;
    }

    @Part08_05_GetMapping(path = "/signout")
    public Part08_02_ModelAndView signout(HttpSession session) {
        session.removeAttribute("user");
        return new Part08_02_ModelAndView("redirect:/");
    }

    @Part08_05_GetMapping(path = "/user/profile")
    public Part08_02_ModelAndView profile(HttpSession session) throws IOException {
         User user = (User) session.getAttribute("user");
//        因为Part09_03_AuthFilter中对用户身份已经进行了检查，所以不再需要检测用户是否已经登录
//         if (user == null){
//             return new Part08_02_ModelAndView("redirect:/signin");
//         }
         return new Part08_02_ModelAndView(Map.of("user", user), "/profile.html");
    }
}

class User{
    String name;
    String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }
}

class SignInBean{
    public String username;
    public String password;
}

