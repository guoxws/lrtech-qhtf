package com.lrtech.ibatistools.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 简单的Java数据库连接和关闭工具类
 *
 * @author leizhimin 11-12-20 下午4:32
 */
public class DBTools {
    private static String driverClassName, url, user, password;

    static {
        init();
    }

    private static void init() {
        InputStream in = DBTools.class.getResourceAsStream("/com/lrtech/ibatistools/jdbc.properties");
        Properties preps = new Properties();
        try {
            preps.load(in);
            driverClassName = preps.getProperty("jdbc.driver");
            url = preps.getProperty("jdbc.url");
            user = preps.getProperty("jdbc.user");
            password = preps.getProperty("jdbc.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个JDBC连接
     *
     * @return 一个JDBC连接
     */
    public static Connection makeConnection() {
        Connection conn = null;
        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn) {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void close(ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void close(Statement stmt) {
        if (stmt != null)
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * 关闭所有可关闭资源
     *
     * @param objs 可关闭的资源对象有Connection、Statement、ResultSet，别的类型资源自动忽略
     */
    public static void closeAll(Object... objs) {
        for (Object obj : objs) {
            if (obj instanceof Connection) close((Connection) obj);
            if (obj instanceof Statement) close((Statement) obj);
            if (obj instanceof ResultSet) close((ResultSet) obj);
        }
    }

    public static void main(String[] args) throws SQLException {
//        DataSourceMetaData dbmd = MySQLDataSourceMetaData.instatnce();
//        List<Table> tableList = dbmd.getAllTableMetaData(DBTools.makeConnection());
//        for (Table table : tableList) {
//            System.out.println(table);
//        }

        String ins_sql = "select 1 from dual";
        Connection conn = makeConnection();
        Statement stmt = conn.createStatement();
        for (int i = 0; i < 20000; i++) {
            stmt.execute(ins_sql);
        }
        conn.close();
    }
}
