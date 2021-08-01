package com.lwdHouse;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 使用连接池：HikariCP
 */
public class part2_ConnectionPool {
    static String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc";
    static String JDBC_USER = "learn";
    static String JDBC_PASSWORD = "learnpassword";
    static DataSource ds;

    // 启动的时候直接创建好连接池
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USER);
        config.setPassword(JDBC_PASSWORD);
        // 连接超时
        config.addDataSourceProperty("connectionTimeout", "1000");
        // 空闲超时
        config.addDataSourceProperty("idleTimeout", "6000");
        // 最大连接数
        config.addDataSourceProperty("maximumPoolSize", "10");
        ds = new HikariDataSource(config);
    }

    public static void main(String[] args) throws SQLException {
        //当我们调用conn.close()方法时（在try(resource){...}结束处），
        // 不是真正“关闭”连接，而是释放到连接池中，以便下次获取连接时能直接返回
        try(Connection conn = ds.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM students")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String name = rs.getString("name");
                    int score = rs.getInt("score");
                    System.out.println(name + ": " + score);
                }
            }
        }
    }
}
