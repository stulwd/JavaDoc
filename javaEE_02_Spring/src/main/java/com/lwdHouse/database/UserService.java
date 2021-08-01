package com.lwdHouse.database;

import com.lwdHouse.database.dao.GoodUserDao;
import com.lwdHouse.database.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
// @Component bean上加Transactional，表示这个类下的所有public方法都要有事务
// @Transactional
// spring的事务功能的原理还是AOP代理，即通过自动创建bean的Proxy实现：
//  public class UserService$$EnhancerBySpringCGLIB extends UserService{
//      UserService target = ...
//      PlatformTransactionManager txManager = ...
//
//      public User register(String email, String password, String name) {
//          TransactionStatus tx = null;
//          try {
//              tx = txManager.getTransaction(new DefaultTransactionDefinition());
//              target.register(email, password, name);
//              txManager.commit(tx);
//          } catch (RuntimeException e) {
//              txManager.rollback(tx);
//              throw e;
//          }
//      }
//      ...
//  }
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    // 我们如果编写了UserDao，那么Service层就注入dao而不是jdbcTemplate来操作数据库了
    // 这样做的意义就是，把所有数据库的操作都放到了dao层，Service层去调用dao层，而不直接操作jdbcTemplate
    @Autowired
    UserDao userDao;
//    GoodUserDao userDao;

    @Autowired
    BonusService bonusService;
    /* T execute(ConnectionCallback<T> action) */
    public User getUserById(long id){
        // 使用jdbcTemplate操作数据库
        return jdbcTemplate.execute((Connection conn) -> {
            try (var ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")){
                ps.setObject(1, id);
                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return new User(rs.getLong("id"),
                                        rs.getString("email"),
                                        rs.getString("password"),
                                        rs.getString("name"));
                    }
                    throw new RuntimeException("user not found by id.");
                }
            }
        });

        // 直接使用userDao操作数据库
        return userDao.getById(id);
    }
    /* T execute(String sql, PreparedStatementCallback<T> action) */
    public User getUserByName(String name){
        return jdbcTemplate.execute("SELECT * FROM users WHERE name = ?", (PreparedStatement ps) -> {
            ps.setObject(1, name);
            try (var rs = ps.executeQuery()){
                if (rs.next()){
                    return new User(rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name"));
                }
                throw new RuntimeException("user not found by id.");
            }
        });
    }


    /**
     * 查询：
     * queryForObject: 查询一行数据（当查询到了多行时，会报错）
     *
     * 传入SQL以及SQL参数后，
     * JdbcTemplate会自动创建PreparedStatement，
     * 自动执行查询并返回ResultSet
     * 我们提供的RowMapper需要做的事情就是把ResultSet的当前行映射成一个JavaBean并返回
     * 整个过程中，使用Connection、PreparedStatement和ResultSet都不需要我们手动管理
     */
    /* T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) */
    public User getUserByEmail(String email){
        // queryForObject方法返回的类型就是第三个参数RowMapper<T>的T,
        // RowMapper<T>是一个FunctionalInterface
        // 定义的方法签名是： T mapRow(ResultSet var1, int var2)
        // 所以mapRow实际返回的类型T就是RowMapper<T>的T，
        // RowMapper<T>的T就是queryForObject方法返回的T
        // 所以如果第三个参数传lambda表达式，lambda表达式的返回类型就是queryForObject的返回类型
        // 在此处，经过查阅源码，
        // 发现：
        // queryForObject()只能查询返回一个行数据的sql，如果这个sql返回了多行
        // 那么会抛出IncorrectResultSizeDataAccessException异常
        // 而query()能查询返回多行的sql，对查询结果ResultSet，调用while(rs.next())进行遍历，
        // 在while内部调用用户的回调（第三个参数），并拿到回调的结果，放入List中，最终返回。
        // 源码参考
        // queryForObject 和 query 的区别参考源码：
        //  JdbcTemplate.class:506 521行，两个调用了相同的this.query方法，
        //  一直点进去query方法，最后到420行的根query方法，这个方法的这两行很重要
        //  rs = ps.executeQuery();
        //  var3 = rse.extractData(rs);
        // 第一行执行sql查询，第二行用rse解析数据，可以发现queryForObject 和 query
        // 的rse是不一样的，这个rse就是一直点进去的每一个query方法的第三个参数。
        // 这个rse是在507 522行即时创建的RowMapperResultSetExtractor类型
        // 创建的时候，第一个参数就是用户定义的的回调，第二个参数就是期望的查询行数，
        // query方法期望的行数没有设置，queryForObject设置了1.
        // 可以发现如果queryForObject方法528行返回了多元素List，那么529行就会检查失败，报异常
        // 所以queryForObject 和 query两个方法的底层都是一样的，不同的是，
        // 最后对queryForObject进行检查返回的结果是不是single的
        /**
         *     public List<T> extractData(ResultSet rs) throws SQLException {
         *         List<T> results = this.rowsExpected > 0 ? new ArrayList(this.rowsExpected) : new ArrayList();
         *         int var3 = 0;
         *
         *         while(rs.next()) {
         *             results.add(this.rowMapper.mapRow(rs, var3++));
         *         }
         *
         *         return results;
         *     }
         */
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", new Object[] {email} ,
            (ResultSet rs, int rowNum) -> {
                return new User(rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"));
            }
        );
    }

    /**
     * 查询：
     * RowMapper不一定返回JavaBean，实际上它可以返回任何Java对象
     * 返回的对象类型由实际返回的对象类型决定
     */
    public long getUsers(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", null,
                (ResultSet rs, int rowNum) -> {
            // SELECT COUNT(*)查询只有一列，取第一列数据:
            return rs.getLong(1);
        });
    }

    /**
     * 查询：
     * 使用query方法查询多行数据
     */
    public List<User> getUsers(int pageIndex){
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?", new Object[]{limit, offset},
                // 如果数据库表的结构恰好和JavaBean的属性名称一致，
                // 那么BeanPropertyRowMapper就可以直接把一行记录按列名转换为JavaBean
                /*new BeanPropertyRowMapper<>(User.class)*/
            (ResultSet rs, int rowNum) -> {
                // SELECT COUNT(*)查询只有一列，取第一列数据:
                return new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"), rs.getString("name"));
        });
    }

    /**
     * 更新，删除
     * 下面只举例更新，删除也是一样的，写好删除的SQL，其他都一样
     */
    public void updateUser(User user){
        if (1 != jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", user.getName(), user.getId())){
            throw new RuntimeException("User not found by id");
        }
    }


    /**
     * 插入,并获取 generated key
     */
    // 这个方法需要支持事务
    @Transactional
    // 默认情况下，如果这个方法内部抛出了 RuntimeException 异常就要回滚事务
    // 如果想要针对其他抛出的异常进行回滚，需要指定异常类
    // @Transactional(rollbackFor = {RuntimeException.class, IOException.class})
    // 一般强烈建议业务异常体系从RuntimeException派生，就不需要指定
    // 
    // 事务传播：
    //    @Transactional让register方法执行前开启了事务，
    //    方法内部的addBonus()方法也有事务注解@Transactional，所以会合并到当前事务还是新开启一个事务？
    //    这和事务传播级别有关，事务传播级别有如下几个：
    //    ( 默认的REQUIRED已经满足绝大部分需求，)
    //    ( SUPPORTS和REQUIRES_NEW在少数情况下会用到 )
    //           1.REQUIRED(默认)：如果当前没有事务，就创建一个新事务，如果当前有事务，就加入到当前事务中执行
    //           2.SUPPORTS：如果有事务，就加入到当前事务，如果没有，那也不开启事务执行，
    //                     可用于查询方法，因为SELECT语句既可以在事务内执行，也可以不需要事务；
    //           3.MANDATORY： 表示必须要存在当前事务并加入执行，否则将抛出异常
    //                     可用于核心更新逻辑，比如用户余额变更，它总是被其他事务方法调用，不能直接由非事务方法调用
    //           4.REQUIRES_NEW：表示不管当前有没有事务，都必须开启一个新的事务执行
    //                        如果当前已经有事务，那么当前事务会挂起，等新事务完成后，再恢复执行
    //           5.NOT_SUPPORTED：不支持事务，如果当前有事务，那么当前事务会挂起，
    //                        等这个方法执行完成后，再恢复执行
    //           6.NEVER：它不但不支持事务，而且在监测到当前有事务时，会抛出异常拒绝执行
    //           7.NESTED：如果当前有事务，则开启一个嵌套级别事务，如果当前没有事务，则开启一个新事务
    // 事务传播的原理:
    // 一个事务方法，是如何获知当前方法是否正在事务背景下的，以便根据当前方法定义的事务传播级别
    // 来决定是否要打开一个新的事务？
    // 例如：addBonus方法，执行到这个方法之前，如果没有事务，要开启一个事务，如果有，要融入到当前事务中。
    // 答案：
    //    使用ThreadLocal，Spring总是把JDBC相关的Connection和TransactionStatus实例绑定到ThreadLocal，
    //    如果一个事务方法从ThreadLocal未取到事务，那么它会打开一个新的JDBC连接，同时开启一个新的事务，
    //    否则，它就直接使用从ThreadLocal获取的JDBC连接以及TransactionStatus
    //    也就是说，在生成的aop代理源码中，会根据ThreadLocal里是否有Connection和TransactionStatus，来执行相应代码
    // 类似于这样
    //    ThreadLocal<TransactionStatus> tlTx = new ThreadLocal<>();
    //    public void addBonus(long userId, int bonus){
    //        if(null == tlTx.get()){
    //            try {
    //                tx = txManager.getTransaction(new DefaultTransactionDefinition());
    //                if (1 != jdbcTemplate.update("UPDATE bonus SET bonus=bonus + ? WHERE user_id = ?", bonus, userId)){
    //                    throw new RuntimeException("add bonus failed");
    //                }
    //                txManager.commit(tx);
    //            } catch (RuntimeException e) {
    //                txManager.rollback(tx);
    //                throw e;
    //            }
    //        }else{
    //            if (1 != jdbcTemplate.update("UPDATE bonus SET bonus=bonus + ? WHERE user_id = ?", bonus, userId)){
    //                throw new RuntimeException("add bonus failed");
    //            }
    //        }
    //    }
    // 所以可以看出：事务只能在当前线程传播，无法跨线程传播
    public User register(String email, String password, String name){
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update(
                (conn) -> {
                    var ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, email);
                    ps.setObject(2, password);
                    ps.setObject(3, name);
                    return ps;
                },
                holder)
        ){
            throw new RuntimeException("insert failed.");
        }
        // addBonus里面的异常因为没有往上抛出，所以这里不需要捕获，也不用再往上抛出？
        bonusService.addBonus(holder.getKey().longValue(), 5);
        return new User(holder.getKey().longValue(), email, password, name);
    }


    /**
     * 总结：
     * 针对简单查询，优选query()和queryForObject()，因为只需提供SQL语句、参数和RowMapper；
     * 针对更新操作，优选update()，因为只需提供SQL语句和参数；
     * 任何复杂的操作，最终也可以通过execute(ConnectionCallback)实现，因为拿到Connection就可以做任何JDBC操作。
     */



}


