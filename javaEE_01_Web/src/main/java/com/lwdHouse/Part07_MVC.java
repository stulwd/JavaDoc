package com.lwdHouse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MVC:
 * 我们把UserServlet看作业务逻辑处理，把User看作模型，把user.jsp看作渲染，
 * 这种设计模式通常被称为MVC：Model-View-Controller，即
 * UserServlet作为控制器（Controller），
 * User作为模型（Model），
 * user.jsp作为视图（View）
 *                    ┌───────────────────────┐
 *              ┌────>│Controller: UserServlet│
 *              │     └───────────────────────┘
 *              │                 │
 * ┌───────┐    │           ┌─────┴─────┐
 * │Browser│────┘           │Model: User│
 * │       │<───┐           └─────┬─────┘
 * └───────┘    │                 │
 *              │                 ▼
 *              │     ┌───────────────────────┐
 *              └─────│    View: user.jsp     │
 *                    └───────────────────────┘
 */
//@WebServlet(urlPatterns = "/user")
public class Part07_MVC extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 假装从数据库读取:
        School school = new School("No.1 Middle School", "101 South Street");
        User user = new User(123, "Bob", school);
        // 放入Request中:
        req.setAttribute("user", user);
        // forward给user.jsp:
        req.getRequestDispatcher("/WEB-INF/user.jsp").forward(req, resp);
    }

    public class User {
        public User(long id, String name, School school) {
            this.id = id;
            this.name = name;
            this.school = school;
        }

        public long id;
        public String name;
        public School school;
    }

    public class School {
        public School(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public String name;
        public String address;
    }


}



