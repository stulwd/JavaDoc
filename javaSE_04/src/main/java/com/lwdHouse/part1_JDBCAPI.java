package com.lwdHouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC：
         SQL数据类型	    Java数据类型
         BIT, BOOL	    boolean
         INTEGER	    int
         BIGINT	        long
         REAL	        float
         FLOAT, DOUBLE	double
         CHAR, VARCHAR	String
         DECIMAL	    BigDecimal
         DATE	        java.sql.Date, LocalDate
         TIME	        java.sql.Time, LocalTime
 *
 */
public class part1_JDBCAPI
{
    static String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc";
    static String JDBC_USER = "learn";
    static String JDBC_PASSWORD = "learnpassword";
    public static void main(String[] args) throws SQLException {
//        query00();    // 打开连接关闭连接
//        query01();    // 查询
//        query02();    // 查询 使用preparedStatement
//        update01();   // 插入
//        update02();   // 插入
//        update03();     // 修改
//        update04();     // 删除
//        Transaction();    // 事务
        batch();
    }

    // 获得连接、关闭连接
    public static void query00() throws SQLException {
        // DriverManager会自动扫描classpath，找到所有的JDBC驱动，
        // 然后根据我们传入的URL自动挑选一个合适的驱动。
        Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        // 用完之后要关闭资源
        conn.close();
    }

    // JDBC查询
    // 结合try，使资源自动释放
    public static void query01() throws SQLException {
        // connection是一种昂贵的资源，因为Connection是AutoClosable的，可以结合try，自动释放
        try(Connection conn = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD)){
            try (Statement stmt = conn.createStatement()){
                try (ResultSet rs = stmt.executeQuery("SELECT id, grade, name, gender FROM students WHERE gender=1")){
                    while (rs.next()){
                        // 使用索引读
                        long id = rs.getLong(1);
                        int grade = rs.getInt(2);
                        String name = rs.getString(3);
                        int gender = rs.getInt(4);
                        System.out.println(id+grade+name+gender);
                    }
                }
            }
        }
    }

    // JDBC查询
    // 使用PreparedStatement,防止sql注入
    public static void query02() throws SQLException {
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")){
                ps.setObject(1, 1);
                ps.setObject(2, 3);
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()) {
                        // 使用列名读
                        long id = rs.getLong("id");
                        int grade = rs.getInt("grade");
                        String name = rs.getString("name");
                        int gender = rs.getInt("gender");
                        System.out.println(id + grade + name + gender);
                    }
                }
            }
        }
    }

    // JDBC更新: 插入
    public static void update01() throws SQLException {
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO students (id, grade, name, gender) values (?,?,?,?)")){
                ps.setObject(1, 999);
                ps.setObject(2, 1);
                ps.setObject(3, "Bob");
                ps.setObject(4, 0);
                int n = ps.executeUpdate();
            }
        }
    }

    // JDBC更新: 插入并获得主键
    public static void update02() throws SQLException {
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO students (grade, name, gender) values (?,?,?)",
                                                                    Statement.RETURN_GENERATED_KEYS)){
                ps.setObject(1, 1);
                ps.setObject(2, "Bob");
                ps.setObject(3, 0);
                int n = ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()){
                        long id = rs.getLong(1);        // 从索引1开始
                        System.out.println("id="+id);
                    }
                }
            }
        }
    }

    // JDBC更新: 修改
    public static void update03() throws SQLException {
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement("UPDATE students SET name=?, score=? WHERE id=?")){
                ps.setObject(1, "Lisa");
                ps.setObject(2, 98);
                ps.setObject(3, 999);
                int n = ps.executeUpdate();     // 返回更新的行数
            }
        }
    }

    // JDBC更新: 删除
    public static void update04() throws SQLException {
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id=?")){
                ps.setObject(1, 1001);
                int n = ps.executeUpdate();
            }
        }
    }

    // JDBC事务: 获取connection后，自动提交默认是开启的
    public static void Transaction() throws SQLException {
        Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        try {
            // 将自动提交事务关闭
            conn.setAutoCommit(false);
            // 设置事务隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // 执行多条sql语句，例如，把Bob成绩+10，Lisa成绩-10
            try(PreparedStatement ps = conn.prepareStatement("UPDATE students SET score = score - 10 WHERE id=?")){
                ps.setObject(1, 999);
                ps.executeUpdate();
            }
            try(PreparedStatement ps = conn.prepareStatement("UPDATE students SET score = score + 10 WHERE id=?")){
                ps.setObject(1, 1000);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // JDBC批处理：batch
    public static void batch() throws SQLException {
        List<Student> stuList = new ArrayList<>();
        stuList.add(new Student("张三", 0, 3, 40));
        stuList.add(new Student("李四", 1, 4, 50));
        stuList.add(new Student("王五", 1, 5, 60));
        stuList.add(new Student("钱六", 0, 6, 70));
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO students (name, gender, grade, score) values (?,?,?,?)",
                                                                Statement.RETURN_GENERATED_KEYS)){
                for (Student stu : stuList) {
                    ps.setObject(1, stu.getName());
                    ps.setObject(2, stu.getGender());
                    ps.setObject(3, stu.getGrade());
                    ps.setObject(4, stu.getScore());
                    ps.addBatch();  // 添加到batch
                }
                int[] ns = ps.executeBatch();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    while (rs.next()){
                        System.out.println("key: " + rs.getLong(1));
                    }
                }
            }
        }
    }

}

class Student{
    private String name;
    private int gender;
    private int grade;
    private int score;

    public Student(String name, int gender, int grade, int score) {
        this.name = name;
        this.gender = gender;
        this.grade = grade;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
