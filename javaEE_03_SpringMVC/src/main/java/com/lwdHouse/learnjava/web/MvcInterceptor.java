package com.lwdHouse.learnjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 给 modelView 添加 locale和MessageSource 信息的 Interceptor
 */
@Component
public class MvcInterceptor implements HandlerInterceptor {
    @Autowired
    LocaleResolver localeResolver;

    @Autowired
    @Qualifier("i18n")
    MessageSource messageSource;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            // 重要：
            // CookieLocaleResolver从HttpServletRequest中获取Locale时，
            // 首先根据一个特定的Cookie判断是否指定了Locale，
            // 如果没有，就从HTTP头的Accept-Language获取浏览器设置的语言，如果还没有，就返回默认的Locale，在创建bean时设置的。
            // 当用户第一次访问网站时，是注定没有Cookie的，
            // 所以通常CookieLocaleResolver就会获取到HTTP头的Accept-Language，即使用浏览器的默认语言
            // （HTTP规范规定了浏览器会在每次请求中携带Accept-Language头 用来指示用户浏览器设定的语言顺序
            //   如Accept-Language: zh-CN,zh;q=0.8,en;q=0.2
            //   localeResolver就可以根据request解析出用戶的locale）
            // 之后访问的时候，如果服务器不主动设置Cookie，还是没有Cookie的，还是使用的Accept-Language
            // 那服务器如何设置一个语言的Cookie呢？
            // 通过localeResolver.setLocale(request, response, locale);这条语句就可以给浏览器返回一个
            // Set-Cookie: org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh-CN;
            // 响应头来让浏览器设置Cookie，
            // 之后浏览器就会携带这个Cookie进行访问，直到Cookie失效
            // ( 在这一节中，我们在LocaleController以记录Cookie的方式来设置用户的语言。)
            // 服务器收到request后，就可通过localeResolver.resolveLocale(request)优先读取Cookie设置的语言，解析Locale
            Locale locale = localeResolver.resolveLocale(request);
            // 放入Model:
            modelAndView.addObject("__messageSource__", messageSource);
            modelAndView.addObject("__locale__", locale);
        }
    }
}
