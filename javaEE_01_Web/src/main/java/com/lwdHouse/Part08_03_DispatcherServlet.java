package com.lwdHouse;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/")
public class Part08_03_DispatcherServlet extends HttpServlet {
    private Map<String, GetDispatcher> getMappings = null;
    private Map<String, PostDispatcher> postMappings = null;
    private Part08_06_ViewEngine viewEngine;

    @Override
    public void init() throws ServletException {
        try {
            this.getMappings = scanGetInControllers();
            this.postMappings = scanPostInControllers();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.viewEngine = new Part08_06_ViewEngine(getServletContext());
    }

    private Map<String, GetDispatcher> scanGetInControllers() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Class<?>[] claList = new Class[]{Class.forName("com.lwdHouse.Part08_04_UserController"),
                                         Class.forName("com.lwdHouse.Part08_04_IndexController"),
                                         Class.forName("com.lwdHouse.Part09_04_UploadController"),
                                         Class.forName("com.lwdHouse.Part09_05_01_HelloController")};
        Map<String, GetDispatcher> mappingsConfig = new HashMap<>();
        for (Class<?> cla : claList) {
            // 新建Controller实例
            Constructor<?> controllerConstructor = cla.getConstructor();
            Object controller = controllerConstructor.newInstance();
            // 新建map
            // 获取Controller类下所有方法的类对象
            Method[] declaredMethods = cla.getDeclaredMethods();
            // 扫描出所有的get方法
            for (Method med : declaredMethods){
                if(med.isAnnotationPresent(Part08_05_GetMapping.class)){
                    String[] parameterNames = Arrays.stream(med.getParameters()).map(pmt -> {
                        return pmt.getName();
                    }).toArray(String[]::new);
                    Class[] parameterClasses = Arrays.stream(med.getParameters()).map(pms -> {
                        return pms.getType();
                    }).toArray(Class[]::new);
                    String path = med.getAnnotation(Part08_05_GetMapping.class).path();
                    GetDispatcher dispatcher = new GetDispatcher(controller, med, parameterNames, parameterClasses);
                    mappingsConfig.put(path, dispatcher);
                }
            }
            
        }
        return mappingsConfig;
    }

    private Map<String, PostDispatcher> scanPostInControllers() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        Class<?>[] claList = new Class[]{Class.forName("com.lwdHouse.Part08_04_UserController"),
                                         Class.forName("com.lwdHouse.Part08_04_IndexController"),
                                         Class.forName("com.lwdHouse.Part09_04_UploadController"),
                                         Class.forName("com.lwdHouse.Part09_05_01_HelloController")};
        Map<String, PostDispatcher> mappingsConfig = new HashMap<>();
        for (Class<?> cla : claList) {
            // 新建Controller实例
            Constructor<?> controllerConstructor = cla.getConstructor();
            Object controller = controllerConstructor.newInstance();
            // 新建map
            // 获取Controller类下所有的方法对象
            Method[] declaredMethods = cla.getDeclaredMethods();
            // 扫描出所有的post方法
            for (Method med : declaredMethods){
                if (med.isAnnotationPresent(Part08_05_PostMapping.class)){
                    Class[] parameterClasses = Arrays.stream(med.getParameters()).map(pms -> {
                        return pms.getType();
                    }).toArray(Class[]::new);
                    String path = med.getAnnotation(Part08_05_PostMapping.class).path();
                    PostDispatcher dispatcher = new PostDispatcher(controller, med, med.getParameterTypes(), objectMapper);
                    mappingsConfig.put(path, dispatcher);
                }
            }
        }
        return mappingsConfig;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        GetDispatcher dispatcher = getMappings.get(path);
        if (dispatcher == null){
            resp.sendError(404);
            return;
        }
        Part08_02_ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(req, resp);
        } catch (Exception e) {}
        if (mv == null){
            return;
        }
        if (mv.view.startsWith("redirect:")){
            resp.sendRedirect(mv.view.substring(9));
            return;
        }
        // 将模板引擎渲染的内容写入响应
        PrintWriter pw = resp.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        PostDispatcher dispatcher = postMappings.get(path);
        if (dispatcher == null){
            resp.sendError(404);
            return;
        }
        Part08_02_ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(req, resp);
        } catch (Exception e) {}
        if (mv == null){
            return;
        }
        if (mv.view.startsWith("redirect:")){
            resp.sendRedirect(mv.view.substring(9));
            return;
        }
        // 将模板引擎渲染的内容写入响应
        PrintWriter pw = resp.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();
    }
}

class GetDispatcher {
    Object instance; // Controller实例
    Method method; // Controller方法
    String[] parameterNames; // 方法参数名称
    Class<?>[] parameterClasses; // 方法参数类型

    public GetDispatcher(Object instance, Method method, String[] parameterNames, Class<?>[] parameterClasses) {
        this.instance = instance;
        this.method = method;
        this.parameterNames = parameterNames;
        this.parameterClasses = parameterClasses;
    }

    public Part08_02_ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else if (parameterClass == int.class) {
                arguments[i] = Integer.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == long.class) {
                arguments[i] = Long.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));
            } else if (parameterClass == String.class) {
                arguments[i] = getOrDefault(request, parameterName, "");
            } else {
                throw new RuntimeException("Missing handler for type: " + parameterClass);
            }
        }
        return (Part08_02_ModelAndView) this.method.invoke(this.instance, arguments);
    }

    private String getOrDefault(HttpServletRequest request, String name, String defaultValue) {
        String s = request.getParameter(name);
        return s == null ? defaultValue : s;
    }
}


class PostDispatcher {
    Object instance; // Controller实例
    Method method; // Controller方法
    Class<?>[] parameterClasses; // 方法参数类型
    ObjectMapper objectMapper; // JSON映射

    public PostDispatcher(Object instance, Method method, Class<?>[] parameterClasses, ObjectMapper objectMapper) {
        this.instance = instance;
        this.method = method;
        this.parameterClasses = parameterClasses;
        this.objectMapper = objectMapper;
    }

    public Part08_02_ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else {
                // 读取JSON并解析为JavaBean:
                BufferedReader reader = request.getReader();
                // 血的教训: objectMapper.readValue 传入的bean.class字段必须显式定义为public,
                // 要是不定义权限修饰符，就是包权限，那objectMapper拿不到属性。
                arguments[i] = this.objectMapper.readValue(reader, parameterClass);
            }
        }
        return (Part08_02_ModelAndView) this.method.invoke(instance, arguments);
    }
}