package com.myRestaurant.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {
    private static final DataSource ds;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config\\druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConn() {
        Connection c;
        try {
            c = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public static void retrieveConn(Connection c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
