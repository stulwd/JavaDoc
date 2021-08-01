package com.lwdHouse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 通过Filter修改HttpServletResponse响应,实现缓存：
 *
 * 创建一个过滤器Part09_05_02_CacheFilter，当缓存命中时，不会执行这个servlet
 */
public class Part09_05_01_HelloController {
    @Part08_05_GetMapping(path = "/slow/hello")
    public Part08_02_ModelAndView hello(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        try{
            // 模拟一个耗时很长的servlet
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>hello, world!</h1>");
        pw.flush();
        return null;
    }
}
