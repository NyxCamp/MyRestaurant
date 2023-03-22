package com.myRestaurant.dbControl.DAO;

import com.myRestaurant.utils.DBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BasicDao<T> {
    public int update(String sql, Object... params){
        Connection conn = DBUtils.getConn();
        QueryRunner qr = new QueryRunner();
        try {
            return qr.update(conn,sql,params);
        } catch (SQLException e) {
            System.out.println("数据库更新失败！");
        } finally {
            DBUtils.retrieveConn(conn);
        }
        return 0;
    }

    public List<T> select(String sql, Class<T> clazz, Object... params){
        Connection conn = DBUtils.getConn();
        QueryRunner qr = new QueryRunner();
        try {
            return qr.query(conn,sql,new BeanListHandler<>(clazz),params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.retrieveConn(conn);
        }
    }

    public T selectOne(String sql, Class<T> clazz, Object... params){
        Connection conn = DBUtils.getConn();
        QueryRunner qr = new QueryRunner();
        try {
            return qr.query(conn,sql,new BeanHandler<>(clazz),params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.retrieveConn(conn);
        }
    }

    public Object selectScalar(String sql, Object... params){
        Connection conn = DBUtils.getConn();
        QueryRunner qr = new QueryRunner();
        try {
            return qr.query(conn,sql,new ScalarHandler<>(),params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.retrieveConn(conn);
        }
    }
}
