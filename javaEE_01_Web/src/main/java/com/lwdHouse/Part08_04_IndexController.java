package com.lwdHouse;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class Part08_04_IndexController {

    @Part08_05_GetMapping(path = "/")
    public Part08_02_ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return new Part08_02_ModelAndView(map,"/index.html");
    }

    @Part08_05_GetMapping(path = "/hello")
    public Part08_02_ModelAndView hello(String name) {
        if (name == null) {
            name = "World";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return new Part08_02_ModelAndView(map, "/hello.html");
    }
}
