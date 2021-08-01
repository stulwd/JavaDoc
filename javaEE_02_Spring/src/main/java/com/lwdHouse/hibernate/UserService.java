package com.lwdHouse.hibernate;

import com.lwdHouse.hibernate.entity.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional
public class UserService {
    // 使用Spring提供的Hibernate接口hibernateTemplate
    @Autowired
    HibernateTemplate hibernateTemplate;
    // 使用Hibernate原生接口
    // 实际上hibernateTemplate封装了sessionFactory
    @Autowired
    SessionFactory sessionFactory;

    public User getUserById(Long id){
            return hibernateTemplate.load(User.class, id);
    }

    public User fetchUserByEmail(String email){
        User example = new User();
        example.setEmail(email);
        List<User> list = hibernateTemplate.findByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<User> getUsers(int pageIndex){
        int pageSize = 100;
        return (List<User>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(User.class),
                (pageIndex - 1) * pageSize, pageSize);
    }

    // Insert插入
    public User register(String email, String password, String name){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        // 不要设置id，因为使用了自增主键
        hibernateTemplate.save(user);
        // 现在已经获取了ID
//        System.out.println(user.getId());
        return user;
    }

    // Delete操作
    // hibernate总是用id来删除记录
    // 过主键删除记录时，一个常见的用法是先根据主键加载该记录，再删除。
    // load()和get()都可以根据主键加载记录，它们的区别在于，当记录不存在时，get()返回null，而load()抛出异常
    public boolean deleteUser(Long id){
        User user = hibernateTemplate.get(User.class, id);
        if (user != null){
            hibernateTemplate.delete(user);
            return true;
        }
        return false;
    }

    // Update操作
    public void updateUser(Long id, String name){
        User user = hibernateTemplate.load(User.class, id);
        user.setName(name);
        // 只会把@Column(updatable=true)的属性加入到UPDATE语句中
        hibernateTemplate.update(user);
    }

    // 使用Example查询
    // 因为example实例只有email和password两个属性为非null，所以最终生成的WHERE语句就是WHERE email = ? AND password = ?。
    // 如果我们把User的createdAt的类型从Long改为long，findByExample()的查询将出问题，
    // 原因在于example实例的long类型字段有了默认值0，导致Hibernate最终生成的WHERE语句意外变成了
    // WHERE email = ? AND password = ? AND createdAt = 0。
    // 显然，额外的查询条件将导致错误的查询结果。
    // [注意]：使用findByExample()时，注意基本类型字段总是会加入到WHERE条件！
    public User login(String email, String password){
        User example = new User();
        example.setEmail(email);
        example.setPassword(password);
        List<User> list = hibernateTemplate.findByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    // 使用Criteria查询
    public User login2(String email, String password){
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password));
        List<User> list = (List<User>) hibernateTemplate.findByCriteria(criteria);
        return list.isEmpty() ? null : list.get(0);
    }

    // 使用Criteria实现更复杂的查询：SELECT * FROM user WHERE (email = ? OR name = ?) AND password = ?
    // 这种没法用findByExample()实现
    public User login3(String email, String name, String password){
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(
                Restrictions.and(
                        Restrictions.or(
                                Restrictions.eq("email", email),
                                Restrictions.eq("name", name)),
                        Restrictions.eq("password", password)
                )
        );
        List<User> list = (List<User>) hibernateTemplate.findByCriteria(criteria);
        return list.isEmpty() ? null : list.get(0);
    }

    // 使用HQL查询
    // 和SQL相比，HQL使用类名和属性名，由Hibernate自动转换为实际的表名和列名
    public User login4(String email, String password){
        List<User> list = (List<User>) hibernateTemplate.find("FROM User WHERE email=?0 AND password=?1", email, password);
        return list.isEmpty() ? null : list.get(0);
    }

    // 使用NamedQuery
    public User login5(String email, String password){
        List<User> list = (List<User>) hibernateTemplate.findByNamedQuery("login", email, password);
        return list.isEmpty() ? null : list.get(0);
    }

    // 使用Hibernate原生接口
    public User login6(String email, String password){
        User LoggedUser;
        Session session = null;
        boolean isNew = false;
        try{
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e){
            session = sessionFactory.openSession();
            isNew = true;
        }

        try{
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Example.create(user));
            int pageIndex = 1;
            int pageSize = 100;
            criteria.setFirstResult((pageIndex - 1) * pageSize);
            criteria.setMaxResults(pageSize);
            List<User> list = criteria.list();
            LoggedUser = list.isEmpty() ? null : list.get(0);
        }
        finally {
            if (isNew){
                session.close();
            }
        }
        return LoggedUser;
    }

}
