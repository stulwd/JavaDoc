package com.lwdHouse.jpa;

import com.lwdHouse.jpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteTimeoutException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

// JDBC、Hibernate和JPA提供的接口，实际上，它们的关系如下
//   JDBC	            Hibernate	        JPA
//   DataSource	        SessionFactory	    EntityManagerFactory
//   Connection	        Session	            EntityManager
// SessionFactory和EntityManagerFactory相当于DataSource，
// Session和EntityManager相当于Connection。
// 每次需要访问数据库的时候，需要获取新的Session和EntityManager，用完后再关闭

@Component
@Transactional
public class UserService {
    // 这里不要使用@Autowired，而是使用@PersistenceContext
    // 注入的不是EntityManagerFactory，而是EntityManager
    @PersistenceContext
    EntityManager em;

    public User getUserById(long id){
        User user = em.find(User.class, id);
        if (user == null){
            throw new RemoteTimeoutException("User not found by id: " + id);
        }
        return user;
    }

    // SELECT * FROM user WHERE email = ?
    public User fetchUserByEmail(String email){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> r = q.from(User.class);
        q.where(cb.equal(r.get("email"), cb.parameter(String.class, "e")));
        TypedQuery<User> query = em.createQuery(q);
        query.setParameter("e", email);
        List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    // 直接写HQL语句
    public User getUserByEmail(String email){
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :e", User.class);
        query.setParameter("e", email);
        List<User> list = query.getResultList();
        if (list.isEmpty()){
            throw new RuntimeException("User not found by email.");
        }
        return list.get(0);
    }

    // 使用namedQuery
    public User login(String email, String password){
        TypedQuery<User> query = em.createNamedQuery("login", User.class);
        query.setParameter("e", email);
        query.setParameter("p", password);
        List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    // 增添
    public User register(String email, String name, String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        em.persist(user);
        return user;
    }
    // 删除
    public void delete(long id){
        User user = getUserById(id);
        em.remove(user);
    }


    // 修改
    public void update(long id, String name){
        User user = getUserById(id);
        user.setName(name);
        em.refresh(user);
        em.merge(user);

    }
}
