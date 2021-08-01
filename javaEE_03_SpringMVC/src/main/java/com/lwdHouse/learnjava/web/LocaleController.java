package com.lwdHouse.learnjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 用户设置语言
 */
@Controller
public class LocaleController {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LocaleResolver localeResolver;

    @GetMapping("/locale/{lo}")
        public ModelAndView setLocale(@PathVariable("lo") String lo, HttpServletRequest request, HttpServletResponse response){
        // 根据传入的lo创建locale实例
        Locale locale = null;
        int pos = lo.indexOf('_');
        if (pos > 0) {
            String lang = lo.substring(0, pos);
            String country = lo.substring(pos + 1);
            locale = new Locale(lang, country);
        } else {
            locale = new Locale(lo);
        }
        // 设定此Locale
        // 这里会给response设置语言Cookie，返回给浏览器响应头，
        // set-Cookie: org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh-CN;
        // 让浏览器之后携带Cookie访问
        localeResolver.setLocale(request, response, locale);
        logger.info("locale is set to {}.", locale);
        String referer = request.getHeader("Referer");
        return new ModelAndView("redirect:" + (referer == null ? "/" : referer));
    }
}
